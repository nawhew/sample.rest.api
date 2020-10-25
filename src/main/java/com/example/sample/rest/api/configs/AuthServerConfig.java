package com.example.sample.rest.api.configs;

import com.example.sample.rest.api.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    /*has user info*/
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenStore tokenStore;

    /* set password encoder*/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(this.passwordEncoder);
    }

    /* set client */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /* test용 inmemory 설정 : jdbc로 db에서 관리하는게 좋음*/
        clients.inMemory()
                .withClient("we")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write") // app에서 정의하기 나름
                .secret(this.passwordEncoder.encode("pw"))
                .accessTokenValiditySeconds(10 * 60)
                .refreshTokenValiditySeconds(6 * 10 * 60)
                ;
    }

    /* set authenticationManager, token-store, userDetailsService*/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(this.authenticationManager)
                .tokenStore(this.tokenStore)
                .userDetailsService(this.accountService)
                ;
    }
}
