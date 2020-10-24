package com.example.sample.rest.api.configs;

import com.example.sample.rest.api.accounts.Account;
import com.example.sample.rest.api.accounts.AccountRole;
import com.example.sample.rest.api.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

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

    /* set auth test user at application running */
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account testUser = Account.builder()
                        .email("we_hwan@naver.com")
                        .password("test123")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                this.accountService.saveAccount(testUser);
            }
        };
    }

}
