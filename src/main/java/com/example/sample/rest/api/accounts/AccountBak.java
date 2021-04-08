package com.example.sample.rest.api.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

//@Entity
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //@EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class AccountBak {

    @Id @GeneratedValue
    @EqualsAndHashCode.Include
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles;
}
