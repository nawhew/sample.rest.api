package com.example.sample.rest.api.events;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class EventDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime beginEnrollmentDateTime;

    @NotNull
    private LocalDateTime closeEnrollmentDateTime;

    @NotNull
    private LocalDateTime beginEventDateTime;

    @NotNull
    private LocalDateTime endEventDateTime;

    private String location;

    @Min(0)
    private int basePrice;

    @Min(0)
    private int maxPrice;

    @Min(0)
    private int limitOfEnrollment;

}
