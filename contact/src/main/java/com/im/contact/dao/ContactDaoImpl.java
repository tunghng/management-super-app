package com.im.contact.dao;

import com.im.contact.exception.BadRequestException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class ContactDaoImpl implements ContactDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<?> findByDynamicAttributes(List<String> attributes, LocalDateTime startDate, LocalDateTime endDate, UUID tenantId) {
        StringBuilder attributeText = new StringBuilder();
        if (attributes == null || attributes.isEmpty()) {
            throw new BadRequestException("Attributes cannot be empty");
        } else {
            for (String attribute : attributes) {
                attributeText.append("c.").append(attribute).append(",");
            }
            attributeText.setLength(attributeText.length() - 1);
        }
        String selectClause = "SELECT " + attributeText + " FROM Contact c ";
        String whereClause = "WHERE (CAST(:startDate AS org.hibernate.type.LocalDateTimeType) IS NULL OR c.createdAt >= :startDate) " +
                "AND (CAST(:endDate AS org.hibernate.type.LocalDateTimeType) IS NULL OR c.createdAt <= :endDate) " +
                "AND :tenantId = c.tenantId " +
                "ORDER BY c.createdAt DESC ";
        TypedQuery<Object[]> query = entityManager.createQuery(selectClause + whereClause, Object[].class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("tenantId", tenantId);
        return query.getResultList();
    }
}