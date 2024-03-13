package com.im.form.config;

import com.im.form.model.InputTypeTemplate;
import com.im.form.model.enums.InputType;
import com.im.form.repository.InputTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;


@Configuration
public class WebConfig {
    private final InputTypeRepository inputTypeRepository;

    public WebConfig(InputTypeRepository inputTypeRepository) {
        this.inputTypeRepository = inputTypeRepository;
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            inputTypeRepository.saveAll(
                    Arrays.stream(InputType.values()).map(
                            t -> new InputTypeTemplate(t.name())).collect(Collectors.toList()));
        };
    }

    ;
}
