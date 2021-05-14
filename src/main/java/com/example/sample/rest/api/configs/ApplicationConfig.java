package com.example.sample.rest.api.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    /* add bean : model mapper bean */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
