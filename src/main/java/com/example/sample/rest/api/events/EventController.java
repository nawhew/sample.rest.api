package com.example.sample.rest.api.events;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
// Class안의 모든 Handler들은 HAL_JSON_VALUE이라는 content type으로 응답을 보낼것이다.
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@Slf4j
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @Autowired
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    /**
     * 이벤트 생성
     * Header에 location 정보 반환 (/api/events/{id})
     * @return 
     */
    @PostMapping
    public @ResponseBody ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {

        // request body mapping validate error return Bad-Request
        if(errors.hasErrors()) {
            log.error("eventDto request body mapping validate has error.");
            return ResponseEntity.badRequest().build();
        }

        // event validate error retrun Bad-Request
        this.eventValidator.vaildate(eventDto, errors);
        if(errors.hasErrors()) {
            log.error("eventValidation has error.");
            return ResponseEntity.badRequest().build();
        }

        /*
        * 입력값을 제한하기 위해 DTO를 덮어 쓰고 파라미터를 변경
        * Event -> EventDto
        * 그리고 Event를 새로 생성 할 때는 ModelMapper를 사용하여 간단하게 EventDto를 Event로 변환
        * 이때 만든 Event는 기존과 다르게 파라미터로 받은 것이 아니기 때문에
        * 테스트부분에서 Mockito로 Mocking한 부분에서 객체가 달라서 처리가 안됨.*/
        Event event = this.modelMapper.map(eventDto, Event.class);
        log.debug("modelmapper eventDto convert event : " + event.toString());
        Event newEvent = this.eventRepository.save(event);


        /*
        * Link를 생성하여 URI로 변환 : /api/events/{id}
        * used link in method (if used link in class : linkTo(EventController.class))
        * ControllerLinkBuilder @Deprecated
        *  : ControllerLinkBuilder replaced WebMvcLinkBuilder.
        * */
//        URI createdUri = linkTo(EventController.class).slash("{id}").toUri();
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        // id setting
//        event.setId(10); //우선 임의의 값 세팅
        // return no body
        //return ResponseEntity.created(createdUri).build();
        return ResponseEntity.created(createdUri).body(event);
    }

    /**
     * 이벤트 생성 : 변경 전 소스
     * Header에 location 정보 반환 (/api/events/{id})
     * @return
     */
//    @PostMapping("/api/events")
    public /*@ResponseBody*/ ResponseEntity createEvent2(@RequestBody Event event) {

        // id setting
        event.setId(10); //우선 임의의 값 세팅

        /*
         * 클래스에 @RequestMapping 쓰기 전
         * 변경 이유는 아래와 같이 하는 경우 메소드에 파라미터가 추가 될 떄마다 수정해주어야 해서
         * 링크를 클래스에 달아서 변경
         * */
//        URI createdUri = linkTo(methodOn(EventController.class).createEvent(event)).slash("{id}")
        // 컴파일 오류를 방지하기 위해 null 매핑
        URI createdUri = linkTo(methodOn(EventController.class).createEvent(null, null)).slash("{id}")
                .toUri();

        return ResponseEntity.created(createdUri).body(event);
    }
}
