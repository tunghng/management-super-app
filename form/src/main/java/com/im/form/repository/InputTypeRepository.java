package com.im.form.repository;

import com.im.form.model.InputTypeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputTypeRepository extends JpaRepository<InputTypeTemplate, String> {

}
