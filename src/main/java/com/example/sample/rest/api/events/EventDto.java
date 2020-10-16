package com.example.sample.rest.api.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class EventDto {


    private String name;

    private String description;

    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime endEventDateTime;

    private String location;

    private int basePrice;

    private int maxPrice;

    private int limitOfEnrollment;

}
