package com.im.support.repository;

import com.im.support.model.TicketCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketCodeRepository extends JpaRepository<TicketCode, String> {
}
