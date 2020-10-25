package com.example.sample.rest.api.configs;

import com.example.sample.rest.api.accounts.Account;
import com.example.sample.rest.api.accounts.AccountRole;
import com.example.sample.rest.api.accounts.AccountService;
import com.example.sample.rest.api.common.BaseContorllerTest;
import com.example.sample.rest.api.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseContorllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // given
        String username = "we_hwan@naver.com";
        String password = "pass";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account);

        String clientId = "we";
        String clientPw = "pw";

        this.mockMvc.perform(post("/oauth/token")
                            .with(httpBasic(clientId, clientPw)) //create basic auth header
                            .param("username", username)
                            .param("password", password)
                            .param("grant_type", "password")
                            )
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("access_token").exists())
                            .andDo(print())
                            ;
    }

}