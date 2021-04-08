package com.example.sample.rest.api.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

//public interface AccountRepository extends JpaRepository<Account, Integer> {
public interface AccountRepository {
        // find Account by email
    Optional<Account> findByEmail(String email);
}
