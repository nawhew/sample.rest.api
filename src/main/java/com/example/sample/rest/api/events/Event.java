package com.example.sample.rest.api.events;

import com.example.sample.rest.api.accounts.Account;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Event {

    @Id @GeneratedValue
    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    /*
    * after spring-boot 2.1 support spring data jpa 2.3
    * spring data jpa 2.3 provide auto type mapping : LocalDataTime */
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;

    @NotNull
    private LocalDateTime closeEnrollmentDateTime;

    @NotNull
    private LocalDateTime beginEventDateTime;

    @NotNull
    private LocalDateTime endEventDateTime;

    private String location; // (optional) 이게 없으면 온라인 모임

    @Min(0)
    private int basePrice; // (optional)

    @Min(0)
    private int maxPrice; // (optional)

    @Min(0)
    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    private Account manager;

    public void update() {
        if(this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        if(this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
