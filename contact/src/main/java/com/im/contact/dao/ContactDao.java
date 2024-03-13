package com.im.contact.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ContactDao {
    List<?> findByDynamicAttributes(List<String> attributes, LocalDateTime startDate, LocalDateTime endDate, UUID tenantId);
}
