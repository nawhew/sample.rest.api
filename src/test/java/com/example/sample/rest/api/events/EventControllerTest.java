package com.example.sample.rest.api.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception {

        /*
         * MediaType.APPLICATION_JSON_UTF8 @Deprecated
         * after spring-framework 5.2 : UTF-8 default charset
         * after spring-boot 2.2.0 : delete Charset in Content-Type
         *  - before : Content-Type: application/json; charset=UTF8;
         *  - after  : Content-Type: application/json;
        */
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON) // 어떤 타입의 응답을 원하는지 AcceptHeader 통해 알려 줌
                        )
                .andExpect(status().isCreated()) // response 201
                ;
    }

}
