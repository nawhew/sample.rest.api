package com.example.sample.rest.api.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* save Account (encode password by spring-security passwordEncoder)*/
    public Account saveAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    /**
     * 입력받은 userName으로 Account를 찾아서
     * 해당 도메인을 스프링시큐리티가 제공하는 인터페이스로 변환하여 반환*/
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = this.accountRepository.findByEmail(userName)
                                    .orElseThrow(() -> new UsernameNotFoundException(userName));
        return new User(account.getEmail()
                    , account.getPassword()
                    /* authority를 주어야하는데 grantAuthority의 role을 변환하여 사용*/
                    , authorities(account.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        /*각각의 롤을 변환하여 반환해준다*/
        return roles.stream().map(role -> {
            return new SimpleGrantedAuthority("ROLE_" + role.name());
        }).collect(Collectors.toSet());
    }
}
