package com.im.form.service;

import com.im.form.model.InputTypeTemplate;
import com.im.form.repository.InputTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InputTypeServiceImpl implements InputTypeService {
    private final InputTypeRepository inputTypeRepository;

    @Override
    public List<InputTypeTemplate> getInputTypes() {
        return inputTypeRepository.findAll();
    }
}
