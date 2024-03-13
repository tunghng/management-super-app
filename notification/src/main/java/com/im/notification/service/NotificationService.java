package com.im.notification.service;


import com.im.notification.dto.model.AppUserDto;
import com.im.notification.dto.model.NotificationUserResponseDto;
import com.im.notification.dto.page.PageData;
import com.im.notification.dto.page.PageLink;

public interface NotificationService {
    PageData<NotificationUserResponseDto> getNotificationUsers(PageLink pageLink, Boolean isRead, AppUserDto appUserDto);
}
