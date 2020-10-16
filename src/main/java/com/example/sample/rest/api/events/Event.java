package com.example.sample.rest.api.events;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    /*
    * after spring-boot 2.1 support spring data jpa 2.3
    * spring data jpa 2.3 provide auto type mapping : LocalDataTime */
    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime endEventDateTime;

    private String location; // (optional) 이게 없으면 온라인 모임

    private int basePrice; // (optional)

    private int maxPrice; // (optional)

    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    private EventStatus eventStatus;

}
