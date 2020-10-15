package com.example.sample.rest.api.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
//@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {

        // given
        Event event = Event.builder()
                .name("spring we")
                .description("REST API Class")
                .beginEventDateTime(LocalDateTime.of(2020, 10, 15, 16, 55))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 10, 16, 16, 55))
                .beginEventDateTime(LocalDateTime.of(2020, 10, 15, 16, 55))
                .endEventDateTime(LocalDateTime.of(2020, 10, 16, 16, 55))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("my home")
                .build();

//        System.out.println("data : " + objectMapper.writeValueAsString(event));

        /*
         * MediaType.APPLICATION_JSON_UTF8 @Deprecated
         * after spring-framework 5.2 : UTF-8 default charset
         * after spring-boot 2.2.0 : delete Charset in Content-Type
         *  - before : Content-Type: application/json; charset=UTF8;
         *  - after  : Content-Type: application/json;
        */
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON) // 어떤 타입의 응답을 원하는지 AcceptHeader 통해 알려 줌
                        .content(objectMapper.writeValueAsString(event))
                        )
                .andExpect(status().isCreated()) // response code 201
                .andExpect(jsonPath("id").exists()) // id가 있는지 여부 체크
                .andDo(print())
                ;
    }

}
