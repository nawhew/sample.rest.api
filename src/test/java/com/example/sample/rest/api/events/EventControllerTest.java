package com.example.sample.rest.api.events;

import com.example.sample.rest.api.common.RestDocsConfiguration;
import com.example.sample.rest.api.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
/*
* @SpringBootTest의 webEnvironment값이 SpringBootTest.WebEnvironment.MOCK)가 default이기 때문에
* 설정을 안해도 Mock을 계속 사용 가능
* 대신 아래와 같이 AutoConfigure 어노테이션을 달아 주어야 한다.
* */
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /*@MockBean
    EventRepository eventRepository;*/

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        // given
        // Event -> EventDto 변경
        EventDto eventDto = EventDto.builder()
                .name("spring we")
                .description("REST API Class")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 15, 16, 55))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 10, 16, 16, 55))
                .beginEventDateTime(LocalDateTime.of(2020, 10, 17, 16, 55))
                .endEventDateTime(LocalDateTime.of(2020, 10, 18, 16, 55))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("my home")
                .build();

        /* Mockbean을 사용하면 Null값이 리턴되기 때문에
         * 임의의 ID를 세팅해주고 Mockito로 어떤 메소드가 호출 되었을 때 (when)
         * 어떤 값을 리턴해주어라. (thenReturn) */
//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);


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
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON) // 어떤 타입의 응답을 원하는지 AcceptHeader 통해 알려 줌
                        .content(objectMapper.writeValueAsString(eventDto))
                        )
                .andExpect(status().isCreated()) // response code 201
                .andExpect(jsonPath("id").exists()) // id가 있는지 여부 체크
//                .andExpect(header().exists("Location")) // Header에 Location이라는 값이 있는지
//                .andExpect(header().string("Content-Type", "application/hal+json")) // 헤더의 키값이 밸류로 들어 있는지
                //위에 두줄을 상수를 사용하여 조금 더 타입 세이프 하게 사용하길 권장.
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
//                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(Matchers.not(false)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                // HATEOAS link
                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_link.profile").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(print())
                // Spring RestDocs
                .andDo(document("create-event" // document name
                        , links( // link snippet 추가 (links.adoc)
                                linkWithRel("self").description("link to self")
                                , linkWithRel("query-events").description("link to query-events")
                                , linkWithRel("update-event").description("link to update an existing event")
                        )
                        , requestHeaders( // request header snippet 추가
                                headerWithName(HttpHeaders.ACCEPT).description("accept header")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        )
                        , requestFields( // request field snippet 추가
                                fieldWithPath("name").description("event name")
                                , fieldWithPath("description").description("event desc")
                                , fieldWithPath("beginEnrollmentDateTime").description("event begin enrollment date time")
                                , fieldWithPath("closeEnrollmentDateTime").description("event close enrollment date time")
                                , fieldWithPath("beginEventDateTime").description("event begin date time")
                                , fieldWithPath("endEventDateTime").description("event end date time")
                                , fieldWithPath("location").description("event location")
                                , fieldWithPath("basePrice").description("event base price")
                                , fieldWithPath("maxPrice").description("event max price")
                                , fieldWithPath("limitOfEnrollment").description("event limit enrollment (max)")
                        )
                        , responseHeaders( // response header snippet 추가
                                headerWithName(HttpHeaders.LOCATION).description("location")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        )
                        , responseFields( // response field snippet 추가
                                fieldWithPath("id").description("identifier of new event")
                                , fieldWithPath("name").description("event name")
                                , fieldWithPath("description").description("event desc")
                                , fieldWithPath("beginEnrollmentDateTime").description("event begin enrollment date time")
                                , fieldWithPath("closeEnrollmentDateTime").description("event close enrollment date time")
                                , fieldWithPath("beginEventDateTime").description("event begin date time")
                                , fieldWithPath("endEventDateTime").description("event end date time")
                                , fieldWithPath("location").description("event location")
                                , fieldWithPath("basePrice").description("event base price")
                                , fieldWithPath("maxPrice").description("event max price")
                                , fieldWithPath("limitOfEnrollment").description("event limit enrollment (max)")
                                , fieldWithPath("free").description("it tells if this event is free or not")
                                , fieldWithPath("offline").description("it tells if this event is offline or not(online)")
                                , fieldWithPath("eventStatus").description("event status")
                                /* 위에 links에서 체크했지만 현재 Restdocs에서는 어쩔수가 없음.
                                 * responseFields를 relaxedResponseFields로 변경하여 일부만 체크하게 해도 되지만,
                                 * 전체를 체크하지 않고 일부만 문서화 하기 때문에... 비추!*/
                                , fieldWithPath("self").description("link to self")
                                , fieldWithPath("query-events").description("link to query-events")
                                , fieldWithPath("update-event").description("link to update an existing event")
                        )
                    ))
                ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 오류가 발생하는 테스트")
    public void createEvent_BadRequest() throws Exception {

        // given
        Event event = Event.builder()
                .id(100)
                .name("spring we")
                .description("REST API Class - Bad Request Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 15, 16, 55))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 10, 16, 16, 55))
                .beginEventDateTime(LocalDateTime.of(2020, 10, 15, 16, 55))
                .endEventDateTime(LocalDateTime.of(2020, 10, 16, 16, 55))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("my home")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 오류가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring we")
                .description("REST API Class")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 10, 15, 16, 55))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 10, 14, 16, 55))
                .beginEventDateTime(LocalDateTime.of(2020, 10, 13, 16, 55))
                .endEventDateTime(LocalDateTime.of(2020, 10, 12, 16, 55))
                .basePrice(1100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("my home")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                        )
                .andExpect(status().isBadRequest())
                // 응답(오류)메세지에 있는 json의 배열 안에 아래의 값들이 있기를 바람
                // 아래의 값들은 기본적으로 Errors에 들어 있는 값들!
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectValue").exists()) // only field error
                .andDo(print())
                ;
    }

}
