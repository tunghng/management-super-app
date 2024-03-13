package com.im.form.repository;

import com.im.form.model.Form;
import com.im.form.model.FormTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, UUID> {

    @Query("SELECT COUNT(f) FROM Form f " +
            "WHERE f.template.id = :formTemplateId")
    Integer countByFormTemplateId(
            @Param("formTemplateId") UUID formTemplateId);

    @Query("SELECT COUNT(f) FROM Form f " +
            "WHERE f.template.id = :formTemplateId " +
            "AND f.contactId = :contactId")
    Integer countByFormTemplateIdAndContactId(
            @Param("formTemplateId") UUID formTemplateId,
            @Param("contactId") UUID contactId);

    @Query("SELECT COUNT(f) FROM Form f " +
            "WHERE f.template.id = :formTemplateId " +
            "AND f.isRead = :isRead")
    Integer countByFormTemplateIdAndIsRead(
            @Param("formTemplateId") UUID formTemplateId,
            @Param("isRead") Boolean isRead);

    @Query("SELECT COUNT(f) FROM Form f " +
            "WHERE f.template.id = :formTemplateId " +
            "AND f.contactId = :contactId " +
            "AND f.isRead = :isRead")
    Integer countByFormTemplateIdAndContactIdAndIsRead(
            @Param("formTemplateId") UUID formTemplateId,
            @Param("contactId") UUID contactId,
            @Param("isRead") Boolean isRead);

    @Query("SELECT MAX(f.createdAt) FROM Form f " +
            "WHERE f.template.id = :formTemplateId")
    LocalDateTime getLatestSendForm(
            @Param("formTemplateId") UUID formTemplateId);

    Page<Form> findByTemplate(FormTemplate formTemplate, Pageable pageable);
}
