package com.im.announcement.service;

import com.im.announcement.dto.mapper.AnnouncementContactMapper;
import com.im.announcement.dto.model.AnnouncementContactDto;
import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.dto.model.ContactDto;
import com.im.announcement.model.Announcement;
import com.im.announcement.model.AnnouncementContact;
import com.im.announcement.model.Contact;
import com.im.announcement.repository.AnnouncementContactRepository;
import com.im.announcement.repository.AnnouncementRepository;
import com.im.announcement.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnnouncementContactServiceImpl implements AnnouncementContactService {

    @Autowired
    AnnouncementContactRepository announcementContactRepository;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    AnnouncementContactMapper announcementContactMapper;

    @Override
    public AnnouncementContactDto save(AnnouncementDto baseAnnouncementDto, ContactDto contactDto) {
        AnnouncementContact exist = announcementContactRepository.findByAnnouncementIdAndContactId(
                baseAnnouncementDto.getId(), contactDto.getId()
        );
        Announcement announcement = announcementRepository.findById(baseAnnouncementDto.getId()).get();
        Contact contact = contactRepository.findById(contactDto.getId()).get();
        AnnouncementContact announcementContact = exist != null ? exist
                : new AnnouncementContact(announcement, contact);
        AnnouncementContact savedAnnouncementContact = announcementContactRepository.saveAndFlush(announcementContact);
        return announcementContactMapper.toDto(savedAnnouncementContact);
    }

    @Override
    public List<UUID> convertToListContactUUID(Collection<AnnouncementContact> collection) {
        if (collection != null)
            return collection.stream().map(item ->
                    item.getContact().getId()).collect(Collectors.toList());
        return List.of();
    }
}
