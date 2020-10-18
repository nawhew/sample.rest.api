package com.example.sample.rest.api.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void vaildate(EventDto eventDto, Errors errors) {
        // 입찰이 시작되면 안되는 로직
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()
                && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong value");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong value");
        }

        /* 이벤트 종료가 이벤트 시작 시간보다 이전이거나
        *  이벤트 종료가 이벤트 등록 마감 시간보다 이전이거나
        *  이벤트 종료가 이벤트 등록 시작 시간보다 이전이면 안됨 */
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
            || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
            || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong value");
        }

        /*  이벤트 시작이 이벤트 등록 마감 시간보다 이전이거나
         *  이벤트 시작이 이벤트 등록 시작 시간보다 이전이면 안됨 */
        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        if(beginEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
            || beginEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("beginEventDateTime", "wrongValue", "beginEventDateTime is wrong value");
        }

        /*  이벤트 등록 마감이 이벤트 등록 시작 시간보다 이전이면 안됨 */
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if(closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "closeEnrollmentDateTime is wrong value");
        }

    }
}
