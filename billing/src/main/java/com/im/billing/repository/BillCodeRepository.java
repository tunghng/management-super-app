package com.im.billing.repository;

import com.im.billing.model.BillCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillCodeRepository extends JpaRepository<BillCode, String> {
}
