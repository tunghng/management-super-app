package com.im.support.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.mapper.AppUserMapper;
import com.im.support.dto.mapper.ContactMapper;
import com.im.support.dto.mapper.TicketMapper;
import com.im.support.dto.model.*;
import com.im.support.dto.request.Notification;
import com.im.support.dto.request.NotificationComponent;
import com.im.support.dto.request.NotificationUser;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.exception.BadRequestException;
import com.im.support.exception.NotFoundException;
import com.im.support.kafka.NotificationProducer;
import com.im.support.model.*;
import com.im.support.model.enums.*;
import com.im.support.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final ContactService contactService;

    private final TicketRepository ticketRepository;

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketCodeRepository ticketCodeRepository;

    private final ReviewRepository reviewRepository;

    private final AppUserRepository appUserRepository;

    private final TicketUserRepository ticketUserRepository;

    private final LogService logService;

    private final TicketMapper ticketMapper;

    private final ContactMapper contactMapper;

    private final AppUserMapper appUserMapper;

    private final NotificationProducer notificationProducer;

    @Override
    public PageData<TicketDto> findTickets(
            PageLink pageLink,
            TicketState state,
            List<UUID> typeIdList,
            List<UUID> contactIdList,
            Boolean isDeleted,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Long updatedAtStartTs,
            Long updatedAtEndTs,
            AppUserDto currentUser,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(
                pageLink.getPage(), pageLink.getPageSize(),
                pageLink.toSort(pageLink.getSortOrder())
        );

        isTimeStampValid(createdAtStartTs, createdAtEndTs);
        isTimeStampValid(updatedAtStartTs, updatedAtEndTs);

        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<Ticket> tickets = !RoleType.CUSTOMER.equals(RoleType.lookup(currentUser.getRole()))
                ? ticketRepository.findTickets(
                searchText,
                isSearchMatchCase,
                state,
                currentUser.getTenantId(),
                contactIdList,
                typeIdList,
                isDeleted,
                convertTimestampToDateTime(createdAtStartTs),
                convertTimestampToDateTime(createdAtEndTs),
                convertTimestampToDateTime(updatedAtStartTs),
                convertTimestampToDateTime(updatedAtEndTs),
                pageable
        ) : ticketRepository.findTicketsByContactId(
                searchText,
                isSearchMatchCase,
                state,
                currentUser.getContactId(),
                typeIdList,
                isDeleted,
                convertTimestampToDateTime(createdAtStartTs),
                convertTimestampToDateTime(createdAtEndTs),
                convertTimestampToDateTime(updatedAtStartTs),
                convertTimestampToDateTime(updatedAtEndTs),
                pageable
        );
        Page<TicketDto> ticketDtoList = tickets.map(ticket -> convertToTicketDetail(ticket, currentUser));
        return new PageData<>(ticketDtoList);
    }

    @Override
    public Long countUnreadTickets(
            AppUserDto currentUser,
            Boolean isRead,
            TicketState state
    ) {
        return ticketUserRepository.countByUserIdAndIsRead(
                currentUser.getId(), isRead
        );
    }

    @Override
    @Transactional
    public TicketDto save(TicketSaveDto ticketDto, AppUserDto currentUser) {

        ActionType actionType = ticketDto.getId() == null ? ActionType.CREATED : ActionType.UPDATED;

        Ticket ticket = ticketDto.getId() != null
                ? ticketRepository.findById(ticketDto.getId()).orElse(new Ticket()) : new Ticket();

        boolean isUpdating = ticketDto.getId() != null;

        BeanUtils.copyProperties(ticketDto, ticket);
        if (ticketDto.getContactId() != null) {
            Contact contact = contactMapper.toModel(
                    contactService.findById(
                            ticketDto.getContactId() != null ? ticketDto.getContactId() : currentUser.getContactId()
                    )
            );
            ticket.setContact(contact);
        }
        if (ticketDto.getId() == null) {
            ticket.setCreatedBy(currentUser.getId());
            String code = ticketCodeRepository.save(new TicketCode()).getId();
            ticket.setCode(code);
        }

        TicketType type = ticket.getType();
        if (ticketDto.getTypeId() != null) {
            type = ticketTypeRepository.findByIdAndTenantId(ticketDto.getTypeId(), currentUser.getTenantId());
        }
        ticket.setType(type);

        ticket.setPriority(ticketDto.getPriority() != null ?
                PriorityType.lookup(ticketDto.getPriority())
                : PriorityType.MEDIUM);

        ticket.setState(ticketDto.getState() != null ?
                TicketState.lookup(ticketDto.getState().toString()) :
                TicketState.PROCESSING);


        ticket.setUpdatedBy(currentUser.getId());
        ticket.setTenantId(currentUser.getTenantId());

        if (actionType.equals(ActionType.CREATED))
            ticket.setIsDeleted(false);

        Ticket savedTicket = ticketRepository.saveAndFlush(ticket);
        TicketDto savedTicketDto = ticketMapper.toDto(savedTicket);
        savedTicketDto.setIsRead(false);
        Notification notification = generateNotification(savedTicket);
        notificationProducer.sendNotificationMessage(notification);
        for (UUID userId : notification.getToUserIds()) {
            TicketUser ticketUser = ticketUserRepository
                    .findByTicketAndUserId(savedTicket, userId)
                    .orElse(TicketUser.builder()
                            .ticket(savedTicket)
                            .userId(userId)
                            .isRead(false)
                            .build()
                    );
            ticketUserRepository.save(ticketUser);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                        .entityType(EntityType.TICKET)
                        .entityId(savedTicketDto.getId())
                        .actionStatus(ActionStatus.SUCCESS)
                        .actionData(objectMapper.valueToTree(ticketDto))
                        .actionType(isUpdating ? ActionType.UPDATED : ActionType.CREATED).build(),
                currentUser.getId(), currentUser.getTenantId());

        return savedTicketDto;
    }

    @Override
    public TicketDto closed(TicketDto ticketDto, AppUserDto currentUser) {
        ticketDto.setState(TicketState.CLOSED.name());
        ticketDto.setClosedAt(LocalDateTime.now());
        TicketSaveDto ticketSaveDto = ticketMapper.toSaveDto(ticketDto);
        return save(ticketSaveDto, currentUser);
    }

    @Override
    public TicketDto findById(UUID ticketId, UUID tenantId) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(ticketId, tenantId).orElse(null);
        return ticket != null ? ticketMapper.toDto(ticket) : null;
    }

    @Override
    public TicketDto findDetailById(UUID ticketId, AppUserDto currentUser) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(ticketId, currentUser.getTenantId()).orElseThrow(
                () -> new BadRequestException(String.format("Ticket with id [%s] is not found", ticketId))
        );
        NotificationUser notificationUser = generateNotificationUser(ticket, currentUser.getId(), true);
        notificationProducer.sendNotificationUserMessage(notificationUser);
        TicketUser ticketUser = ticketUserRepository.findByTicketIdAndUserId(ticket.getId(), currentUser.getId())
                .orElse(new TicketUser());
        if (ticketUser.getId() == null) {
            generateNotificationUser(ticket);
            ticketUser = ticketUserRepository.findByTicketIdAndUserId(ticket.getId(), currentUser.getId())
                    .orElse(new TicketUser());
        }
        ticketUser.setIsRead(true);
        ticketUserRepository.save(ticketUser);
        return convertToTicketDetail(ticket, currentUser);
    }

    @Override
    public ResponseEntity<Object> exportData(
            List<String> attributeList,
            String fileName,
            String language,
            Long createdAtStartTs,
            Long createdAtEndTs,
            AppUserDto currentUser,
            List<UUID> contactIdList,
            List<UUID> typeIdList
    ) {
        List<Ticket> tickets = null;

        if (createdAtStartTs == null || createdAtEndTs == null) {
            tickets = ticketRepository.findTicketsOrderByCreatedAtAsc(currentUser.getTenantId(), contactIdList, typeIdList);
        } else {
            isTimeStampValid(createdAtStartTs, createdAtEndTs);
            tickets = ticketRepository.findTicketsFromStartToEndOrderByCreatedAtAsc(
                    Instant.ofEpochMilli(createdAtStartTs)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(),
                    Instant.ofEpochMilli(createdAtEndTs)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(),
                    currentUser.getTenantId(),
                    contactIdList,
                    typeIdList);
        }
        int numOfTickets = tickets.size();
        List<Map<String, String>> filteredTickets = tickets.stream().map(ticket -> {
            Map<String, String> responseTicket = new HashMap<>();
            if (attributeList.contains("code")) {
                responseTicket.put("code", ticket.getCode());
            }
            if (attributeList.contains("title")) {
                responseTicket.put("title", ticket.getTitle());
            }
            if (attributeList.contains("contactName")) {
                responseTicket.put("contactName", ticket.getContact() != null ? ticket.getContact().getName() : null);
            }
            if (attributeList.contains("state")) {
                responseTicket.put("state", ticket.getState().toString());
            }
            if (attributeList.contains("type")) {
                responseTicket.put("type", ticket.getType() != null ? ticket.getType().getName() : null);
            }
            if (attributeList.contains("createdAt")) {
                responseTicket.put("createdAt", ticket.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString());
            }
            if (attributeList.contains("closedAt")) {
                responseTicket.put("closedAt", ticket.getClosedAt() != null ? ticket.getClosedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString() : "");
            }
            if (attributeList.contains("closedBy")) {
                responseTicket.put("closedBy", ticket.getClosedAt() != null ? currentUser.getFirstName() : "");
            }
            if (attributeList.contains("reviewTitle")) {
                Review review = reviewRepository.findByTicketId(ticket.getId()).orElse(new Review());
                responseTicket.put("reviewTitle", review.getId() != null ? review.getTitle().toString() : "");
            }
            if (attributeList.contains("reviewBody")) {
                Review review = reviewRepository.findByTicketId(ticket.getId()).orElse(new Review());
                responseTicket.put("reviewBody", review.getId() != null ? review.getBody() : "");
            }
            return responseTicket;
        }).collect(Collectors.toList());
        if (fileName == null || fileName.isBlank()) {
            LocalDateTime currentTime = Instant.ofEpochMilli(Instant.now().toEpochMilli()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            fileName = "Tickets_" + currentTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".xlsx";
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, String>> languageMap;
        try {
            languageMap = mapper.readValue(getClass().getResourceAsStream("/data/translate.json"), HashMap.class);
        } catch (IOException e) {
            throw new BadRequestException("Server error");
        }
        Map<String, String> translateMap = languageMap.get(language);

        try (Workbook workbook = new XSSFWorkbook()) {
            int[] cellWidth = new int[attributeList.size()];
            log.info("cellWidth: {}", cellWidth);
            Sheet sheet = workbook.createSheet(fileName);
            CellStyle normalCellStyle = workbook.createCellStyle();
            Font normalFont = workbook.createFont();
            normalFont.setFontName("Arial");
            normalCellStyle.setFont(normalFont);
            normalCellStyle.setBorderTop(BorderStyle.MEDIUM);
            normalCellStyle.setBorderBottom(BorderStyle.MEDIUM);
            normalCellStyle.setBorderLeft(BorderStyle.MEDIUM);
            normalCellStyle.setBorderRight(BorderStyle.MEDIUM);

            Row totalContactRow = sheet.createRow(0);
            Cell totalContactCell = totalContactRow.createCell(0);
            String totalContactCellValue = translateMap.get("totalResult");
            totalContactCell.setCellValue(totalContactCellValue);
            cellWidth[0] = Math.max(totalContactCellValue.length(), cellWidth[0]);
            totalContactCell.setCellStyle(normalCellStyle);

            Cell valueTotalContactCell = totalContactRow.createCell(1);
            valueTotalContactCell.setCellValue(numOfTickets);
            cellWidth[1] = Math.max(String.valueOf(numOfTickets).length(), cellWidth[0]);
            valueTotalContactCell.setCellStyle(normalCellStyle);

            Row timeRow = sheet.createRow(1);
            Cell timeCell = timeRow.createCell(0);
            String timeCellValue = translateMap.get("fromTo");
            timeCell.setCellValue(timeCellValue);
            cellWidth[0] = Math.max(String.valueOf(timeCellValue).length(), cellWidth[0]);
            timeCell.setCellStyle(normalCellStyle);

            String valueDate = "";
            if (createdAtStartTs != null && createdAtEndTs != null) {
                String startDate = Instant.ofEpochMilli(createdAtStartTs)
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String endDate = Instant.ofEpochMilli(createdAtEndTs)
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                valueDate = startDate + " - " + endDate;
            }
            if (createdAtStartTs == null && createdAtEndTs == null && numOfTickets > 0) {
                String startDate = tickets.get(0).getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String endDate = tickets.get(numOfTickets - 1).getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                valueDate = startDate + " - " + endDate;
            }

            Cell valueTimeCell = timeRow.createCell(1);
            valueTimeCell.setCellValue(valueDate);
            cellWidth[1] = Math.max(valueDate.length(), cellWidth[1]);
            valueTimeCell.setCellStyle(normalCellStyle);

            CellStyle headerCellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            byte[] lightBlue = {(byte) 213, (byte) 235, (byte) 246};
            headerCellStyle.setFont(font);
            headerCellStyle.setFillForegroundColor(new XSSFColor(lightBlue));
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setBorderTop(BorderStyle.MEDIUM);
            headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerCellStyle.setBorderRight(BorderStyle.MEDIUM);

            // Header
            Row headerRow = sheet.createRow(2);
            int columnNum = 0;
            for (String attribute : attributeList) {
                Cell cell = headerRow.createCell(columnNum);
                String attributeCellValue = translateMap.get(attribute);
                cell.setCellValue(attributeCellValue);
                cellWidth[columnNum] = Math.max(cellWidth[columnNum], attributeCellValue.length());
                cell.setCellStyle(headerCellStyle);
                columnNum++;
            }

            // Data
            int rowNum = 3;
            for (Map<String, String> ticket : filteredTickets) {
                columnNum = 0;
                Row ticketDataRow = sheet.createRow(rowNum++);
                for (String attribute : attributeList) {
                    Cell ticketDataCell = ticketDataRow.createCell(columnNum);
                    String ticketDataCellValue = ticket.getOrDefault(attribute, "");

                    ticketDataCell.setCellValue(ticketDataCellValue);
                    cellWidth[columnNum] = Math.max(cellWidth[columnNum],
                            ticketDataCellValue == null ? 0 : ticketDataCellValue.length());
                    ticketDataCell.setCellStyle(normalCellStyle);
                    columnNum++;
                }
            }
            for (int i = 0; i < cellWidth.length; i++) {
                sheet.setColumnWidth(i, (cellWidth[i] + 5) * 256);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            throw new BadRequestException("Error when export");
        }
    }

    @Override
    public String deleteTicket(UUID ticketId, AppUserDto currentUser) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(ticketId, currentUser.getTenantId()).orElseThrow(
                () -> new NotFoundException(String.format("Ticket with id [%s] is not found", ticketId))
        );
        if (ticket.getIsDeleted()) {
            throw new BadRequestException(String.format("Ticket with id [%s] has already deleted", ticketId));
        }
        ticket.setIsDeleted(true);
        ticket.setUpdatedBy(currentUser.getId());
        ticketRepository.save(ticket);
        return String.format("Ticket with id [%s] move to trash bin", ticket.getId());
    }

    @Override
    public String restoreTicket(UUID ticketId, AppUserDto currentUser) {
        Ticket ticket = ticketRepository.findByIdAndTenantId(ticketId, currentUser.getTenantId()).orElseThrow(
                () -> new NotFoundException(String.format("Ticket with id [%s] is not found", ticketId))
        );
        if (!ticket.getIsDeleted()) {
            throw new BadRequestException(String.format("Ticket with id [%s] has already displayed", ticketId));
        }
        ticket.setIsDeleted(false);
        ticket.setUpdatedBy(currentUser.getId());
        ticketRepository.save(ticket);
        return String.format("Ticket with id [%s] restore from trash bin", ticket.getId());
    }

    private TicketDto convertToTicketDetail(Ticket ticket, AppUserDto currentUser) {
        if (ticket == null) return null;
        TicketDto ticketDetailDto = ticketMapper.toDto(ticket);
        ticketDetailDto.setContact(contactMapper.toDto(ticket.getContact()));
        AppUser userCreated = appUserRepository.findById(ticket.getCreatedBy()).orElseThrow(() -> new NotFoundException("User not found"));
        AppUser userUpdated = appUserRepository.findById(ticket.getUpdatedBy()).orElseThrow(() -> new NotFoundException("User not found"));
        Review review = reviewRepository.findByTicketId(ticket.getId()).orElse(new Review());
        if (review.getId() != null)
            ticketDetailDto.setReview(new ReviewShortDto(review.getTitle().toString(), review.getBody()));
        ticketDetailDto.setCreatedBy(appUserMapper.toDto(userCreated));
        ticketDetailDto.setUpdatedBy(appUserMapper.toDto(userUpdated));
        if (ticket.getClosedAt() != null)
            ticketDetailDto.setClosedBy(currentUser);
        TicketUser ticketUser = ticketUserRepository.findByTicketAndUserId(ticket, currentUser.getId())
                .orElse(new TicketUser());
        ticketDetailDto.setIsRead(ticketUser.getIsRead());
        return ticketDetailDto;
    }

    private void isTimeStampValid(Long startTs, Long endTs) {
        if (startTs != null && endTs != null) {
            if (!(startTs >= 0 && endTs >= 0 && startTs <= endTs)) {
                throw new BadRequestException("Start time and end time must be valid");
            }
        }
    }

    private Notification generateNotification(Ticket ticket) {
        Collection<UUID> toUserIds = new ArrayList<>();
        toUserIds.addAll(appUserRepository.findByTenantIdAndContactId(
                ticket.getTenantId(),
                ticket.getContact().getId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                RoleType.TENANT.name(),
                ticket.getTenantId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                RoleType.MANAGER.name(),
                ticket.getTenantId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));

        return Notification.builder()
                .component(new NotificationComponent(
                        "support",
                        "ticket",
                        ticket.getId()
                ))
                .message("New ticket")
                .description(String.format("Ticket created by %s", ticket.getCreatedBy()))
                .toUserIds(toUserIds)
                .build();
    }

    private NotificationUser generateNotificationUser(Ticket ticket, UUID currentUserId, Boolean isRead) {
        return NotificationUser.builder()
                .component(new NotificationComponent(
                        "support",
                        "ticket",
                        ticket.getId()
                ))
                .userId(currentUserId)
                .isRead(isRead)
                .build();
    }

    private LocalDateTime convertTimestampToDateTime(Long timestamp) {
        return timestamp != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                : null;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }

    private void generateNotificationUser(Ticket ticket) {
        Notification notification = generateNotification(ticket);
        notificationProducer.sendNotificationMessage(notification);
        for (UUID userId : notification.getToUserIds()) {
            TicketUser ticketUser = ticketUserRepository
                    .findByTicketAndUserId(ticket, userId)
                    .orElse(TicketUser.builder()
                            .ticket(ticket)
                            .userId(userId)
                            .isRead(false)
                            .build()
                    );
            ticketUserRepository.save(ticketUser);
        }
    }

}
