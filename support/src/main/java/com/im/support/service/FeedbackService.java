package com.im.support.service;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.FeedbackDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;

public interface FeedbackService {

    PageData<FeedbackDto> findFeedbacksByTicketId(PageLink pageLink, TicketDto ticketDto, AppUserDto currentUser);

    FeedbackDto save(FeedbackDto dto, TicketDto ticketDto, AppUserDto currentUser);
}
