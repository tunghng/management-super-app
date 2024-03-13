package com.im.billing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.billing.dto.mapper.AppUserMapper;
import com.im.billing.dto.mapper.BillMapper;
import com.im.billing.dto.mapper.ContactMapper;
import com.im.billing.dto.model.AppUserDto;
import com.im.billing.dto.model.BillDto;
import com.im.billing.dto.model.BillSaveDto;
import com.im.billing.dto.request.Notification;
import com.im.billing.dto.request.NotificationComponent;
import com.im.billing.dto.response.page.PageData;
import com.im.billing.dto.response.page.PageLink;
import com.im.billing.exception.BadRequestException;
import com.im.billing.exception.NotFoundException;
import com.im.billing.exception.UnAuthorizedException;
import com.im.billing.kafka.NotificationProducer;
import com.im.billing.model.*;
import com.im.billing.model.enums.BillState;
import com.im.billing.model.enums.RoleType;
import com.im.billing.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    @Autowired
    private ContactService contactService;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillCodeRepository billCodeRepository;
    @Autowired
    private BillTypeRepository billTypeRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private NotificationProducer notificationProducer;


    @Override
    public PageData<BillDto> findBills(
            PageLink pageLink,
            List<BillState> stateList,
            List<UUID> typeIdList,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Long dueDateStartTs,
            Long dueDateEndTs,
            Long updatedAtStartTs,
            Long updatedAtEndTs,
            AppUserDto currentUser,
            List<UUID> contactIdList,
            Boolean isDeleted,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(),
                pageLink.toSort(pageLink.getSortOrder()));

        isTimeStampValid(createdAtStartTs, createdAtEndTs);
        isTimeStampValid(dueDateStartTs, dueDateEndTs);
        isTimeStampValid(updatedAtStartTs, updatedAtEndTs);

        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<Bill> bills = !RoleType.CUSTOMER.equals(RoleType.lookup(currentUser.getRole()))
                ? billRepository.findBills(
                searchText,
                isSearchMatchCase,
                stateList,
                currentUser.getTenantId(),
                contactIdList,
                typeIdList,
                isDeleted,
                convertTimestampToDateTime(createdAtStartTs),
                convertTimestampToDateTime(createdAtEndTs),
                convertTimestampToDateTime(dueDateStartTs),
                convertTimestampToDateTime(dueDateStartTs),
                convertTimestampToDateTime(updatedAtStartTs),
                convertTimestampToDateTime(updatedAtEndTs),
                pageable
        ) : billRepository.findBillsByContactId(
                searchText,
                isSearchMatchCase,
                stateList,
                currentUser.getContactId(),
                typeIdList,
                isDeleted,
                convertTimestampToDateTime(createdAtStartTs),
                convertTimestampToDateTime(createdAtEndTs),
                convertTimestampToDateTime(dueDateStartTs),
                convertTimestampToDateTime(dueDateStartTs),
                convertTimestampToDateTime(updatedAtStartTs),
                convertTimestampToDateTime(updatedAtEndTs),
                pageable
        );
        Page<BillDto> billDetailDtoList = bills.map(bill -> convertToDtoDetail(bill, currentUser));
        return new PageData<>(billDetailDtoList);
    }

    @Override
    public BillDto save(BillSaveDto billDto, AppUserDto currentUser) {
        isTenantAdminOrManager(currentUser);

        UUID currentUserId = currentUser.getId();
        UUID tenantId = currentUser.getTenantId();

        Bill bill = billDto.getId() != null ?
                billRepository.findById(billDto.getId()).get() :
                new Bill();
        BeanUtils.copyProperties(billDto, bill);

        Contact contact = contactMapper.toModel(
                contactService.findById(billDto.getContactId())
        );

        if (billDto.getId() == null) {
            bill.setCreatedBy(currentUserId);
            String code = billCodeRepository.save(new BillCode()).getId();
            bill.setCode(code);
        }

        BillType type = billDto.getType() != null ?
                billTypeRepository.findByNameAndTenantId(billDto.getType(), tenantId).orElse(null)
                : null;
        bill.setType(type);

        bill.setPrice(billDto.getPrice());
        bill.setDueDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(billDto.getDueDateTs()), TimeZone
                .getDefault().toZoneId()));

        bill.setState(billDto.getState() != null ?
                BillState.lookup(billDto.getState()) :
                BillState.UNPAID);

        bill.setContact(contact);
        bill.setUpdatedBy(currentUserId);
        bill.setTenantId(tenantId);
        bill.setIsDeleted(false);

        Bill savedBill = billRepository.saveAndFlush(bill);

        notificationProducer.sendNotificationMessage(generateNotification(savedBill));
        return billMapper.toDto(savedBill);
    }

    public Notification generateNotification(Bill bill) {
        Collection<UUID> toUsersId = new ArrayList<>();

        //TODO: add all toUsers
        toUsersId.addAll(appUserRepository.findByTenantIdAndContactId(
                bill.getTenantId(),
                bill.getContact().getId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));

        return Notification.builder()
                .component(new NotificationComponent(
                        "billing",
                        "bill",
                        bill.getId()
                ))
                .message("New Bill")
                .description(String.format("Billing created by %s", bill.getCreatedBy()))
                .createdAt(bill.getCreatedAt())
                .toUserIds(toUsersId)
                .createdBy(bill.getCreatedBy())
                .build();
    }

    @Override
    public BillDto findById(UUID billId, UUID tenantId) {
        Bill bill = findBillByIdAndTenantId(billId, tenantId);
        return bill != null ? billMapper.toDto(bill) : null;
    }

    @Override
    public BillDto closeBill(UUID billId, AppUserDto currentUser) {
        isTenantAdminOrManager(currentUser);
        Bill bill = findBillByIdAndTenantId(billId, currentUser.getTenantId());
        isBillUnpaid(bill);
        bill.setState(BillState.CLOSED);

        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        bill.setClosedAt(localDateTime);
        bill.setUpdatedBy(currentUser.getId());
        return convertToDtoDetail(billRepository.save(bill), currentUser);
    }

    @Override
    public BillDto payBill(UUID billId, AppUserDto currentUser) {
        isTenantAdminOrManager(currentUser);
        Bill bill = findBillByIdAndTenantId(billId, currentUser.getTenantId());
        isBillUnpaid(bill);
        bill.setState(BillState.PAID);

        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        bill.setPaidAt(localDateTime);
        bill.setUpdatedBy(currentUser.getId());
        return convertToDtoDetail(billRepository.save(bill), currentUser);
    }

    @Override
    public ResponseEntity<Object> exportData(
            List<String> attributeList,
            String fileName,
            String language,
            Long createdAtStartTs,
            Long createdAtEndTs,
            AppUserDto currentUser,
            UUID contactId,
            BillState state,
            UUID typeId,
            UUID fromContactId
    ) {
        List<Bill> bills = null;
        if (createdAtStartTs == null || createdAtEndTs == null) {
            bills = billRepository.findBillsOrderByCreatedAtAsc(
                    currentUser.getTenantId(),
                    contactId,
                    state,
                    typeId,
                    fromContactId);
        } else {
            isTimeStampValid(createdAtStartTs, createdAtEndTs);
            bills = billRepository.findBillsFromStartToEndOrderByCreatedAtAsc(
                    Instant.ofEpochMilli(createdAtStartTs)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(),
                    Instant.ofEpochMilli(createdAtEndTs)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime(),
                    currentUser.getTenantId(),
                    contactId,
                    state,
                    typeId,
                    fromContactId);
        }
        int numOfBills = bills.size();
        List<Map<String, String>> filteredBills = bills.stream().map(bill -> {
            Map<String, String> responseBill = new HashMap<>();
            if (attributeList.contains("code")) {
                responseBill.put("code", bill.getCode());
            }
            if (attributeList.contains("title")) {
                responseBill.put("title", bill.getTitle());
            }
            if (attributeList.contains("contactName")) {
                responseBill.put("contactName", bill.getContact() != null ? bill.getContact().getName() : null);
            }
            if (attributeList.contains("state")) {
                responseBill.put("state", bill.getState().toString());
            }
            if (attributeList.contains("type")) {
                responseBill.put("type", bill.getType() != null ? bill.getType().getName() : null);
            }
            if (attributeList.contains("price")) {
                responseBill.put("price", String.valueOf(bill.getPrice()));
            }
            if (attributeList.contains("createdAt")) {
                responseBill.put("createdAt", bill.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString());
            }
            if (attributeList.contains("dueDate")) {
                responseBill.put("dueDate", bill.getDueDate() != null ? bill.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString() : "");
            }
            if (attributeList.contains("closedAt")) {
                responseBill.put("closedAt", bill.getClosedAt() != null ? bill.getClosedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString() : "");
            }
            if (attributeList.contains("closedBy")) {
                responseBill.put("closedBy", bill.getClosedAt() != null ? currentUser.getFirstName() : "");
            }
            return responseBill;
        }).collect(Collectors.toList());

        if (fileName == null || fileName.isBlank()) {
            LocalDateTime currentTime = Instant.ofEpochMilli(Instant.now().toEpochMilli()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            fileName = "Bills_" + currentTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".xlsx";
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
            valueTotalContactCell.setCellValue(numOfBills);
            cellWidth[1] = Math.max(String.valueOf(numOfBills).length(), cellWidth[0]);
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
            if (createdAtStartTs == null && createdAtEndTs == null && numOfBills > 0) {
                String startDate = bills.get(0).getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String endDate = bills.get(numOfBills - 1).getCreatedAt()
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
            for (Map<String, String> bill : filteredBills) {
                columnNum = 0;
                Row ticketDataRow = sheet.createRow(rowNum++);
                for (String attribute : attributeList) {
                    Cell ticketDataCell = ticketDataRow.createCell(columnNum);
                    String ticketDataCellValue = bill.getOrDefault(attribute, "");
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
    public BillDto findDetailById(UUID billId, AppUserDto currentUser) {
        Bill bill = findBillByIdAndTenantId(billId, currentUser.getTenantId());
        return convertToDtoDetail(bill, currentUser);
    }

    @Override
    public void delete(UUID billId, AppUserDto currentUser) {
        Bill bill = findBillByIdAndTenantId(billId, currentUser.getTenantId());
        if (bill.getIsDeleted())
            throw new BadRequestException(
                    String.format("Bill with id [%s] has already deleted", billId)
            );
        bill.setIsDeleted(true);
        bill.setUpdatedBy(currentUser.getId());
        billRepository.save(bill);
    }

    @Override
    public void restore(UUID billId, AppUserDto currentUser) {
        Bill bill = findBillByIdAndTenantId(billId, currentUser.getTenantId());
        if (!bill.getIsDeleted())
            throw new BadRequestException(
                    String.format("Bill with id [%s] has already displayed", billId)
            );
        bill.setIsDeleted(false);
        bill.setUpdatedBy(currentUser.getId());
        billRepository.save(bill);
    }

    @Override
    public long toMilliseconds(Date date) {
        return date.getTime();
    }

    private void isTimeStampValid(Long startTs, Long endTs) {
        if (startTs != null && endTs != null) {
            if (!(startTs >= 0 && endTs >= 0 && startTs <= endTs)) {
                throw new BadRequestException("Start time and end time must be valid");
            }
        }
    }

    private BillDto convertToDtoDetail(Bill bill, AppUserDto currentUser) {
        if (bill == null) return null;
        BillDto billDto = billMapper.toDto(bill);
        billDto.setContact(contactMapper.toDto(bill.getContact()));
        AppUser userCreated = findUserById(bill.getCreatedBy());
        AppUser userUpdated = findUserById(bill.getUpdatedBy());
        billDto.setCreatedBy(appUserMapper.toDto(userCreated));
        billDto.setUpdatedBy(appUserMapper.toDto(userUpdated));
        if (bill.getClosedAt() != null)
            billDto.setClosedBy(currentUser);
        return billDto;
    }

    private Bill findBillByIdAndTenantId(UUID billId, UUID tenantId) {
        return billRepository.findByIdAndTenantId(billId, tenantId).orElseThrow(
                () -> new NotFoundException(String.format("Bill with id [%s] is not found", billId))
        );
    }

    private PaymentMethod findPaymentMethodById(UUID methodId) {
        return paymentMethodRepository.findById(methodId).orElseThrow(
                () -> new NotFoundException(String.format("Payment method with id [%s] not found", methodId))
        );
    }

    private AppUser findUserById(UUID userId) {
        return appUserRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id [%s] not found", userId))
        );
    }

    private void isTenantAdminOrManager(AppUserDto currentUser) {
        if (currentUser.getRole().equals(RoleType.SYS_ADMIN.toString())
                || currentUser.getRole().equals(RoleType.CUSTOMER.toString()))
            throw new UnAuthorizedException("You does not have permission to this action");
    }

    private void isCustomer(AppUserDto currentUser) {
        if (!currentUser.getRole().equals(RoleType.CUSTOMER.toString())) {
            throw new UnAuthorizedException("You does not have permission to this action");
        }
    }

    private void isBillUnpaid(Bill bill) {
        if (bill.getState().equals(BillState.PAID))
            throw new BadRequestException(String.format("Bill with id [%s] is paid", bill.getId()));
        if (bill.getState().equals(BillState.CLOSED))
            throw new BadRequestException(String.format("Bill with id [%s] is closed", bill.getId()));
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
}
