package com.example.sample.rest.api.configs;

import com.example.sample.rest.api.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /* UserDetailsService*/
    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /* add Bean : O-Auth Token store */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    /* create bean by override configures (under methods)*/
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    /* override authenticationManager configure */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder)
                ;
    }

    /* security-filter apply webSecurity (checked before spring-security)*/
    @Override
    public void configure(WebSecurity web) throws Exception {
        /* ignoring index*/
        web.ignoring().mvcMatchers("/docs/index*.html");

        /* ignoring static resources
         * PathRequest는 Selvelt package에 있는 것을 사용*/
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /* check request (checked in spring-security)*/
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/docs/index*.html").anonymous()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
                ;
    }*/
}
