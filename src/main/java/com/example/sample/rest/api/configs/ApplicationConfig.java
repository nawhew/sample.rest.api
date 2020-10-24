package com.example.sample.rest.api.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    /* add bean : model mapper bean */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /* add bean : spring-security Password encoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /* Delegating password-encoder
        *  spring-security 최신 버전에서 생긴 패스워드인코더로
        *  - 다양한 encoding type을 지원한다.
        *  - 인코딩 된 문자열 앞에 MD5, SHA-256등의 prefix를 붙여준다.
        *     : 암호화 방식에 맞는 (prefix와 일치하는) 적절한 패스워드 인코더를 매핑해준다.*/
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
