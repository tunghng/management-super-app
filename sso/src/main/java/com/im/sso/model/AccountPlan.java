package com.im.sso.model;

import com.im.sso.model.enums.AccountPlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountPlan extends BaseEntity {

    AccountPlanType name;

    @Column(length = 10485760)
    String description;
}
