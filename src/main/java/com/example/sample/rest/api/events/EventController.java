package com.example.sample.rest.api.events;

import com.example.sample.rest.api.common.ErrorsResource;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
            return badRequest(errors);
        }

        // event validate error retrun Bad-Request
        this.eventValidator.vaildate(eventDto, errors);
        if(errors.hasErrors()) {
            log.error("eventValidation has error.");
            return badRequest(errors);
        }

        /*
        * 입력값을 제한하기 위해 DTO를 덮어 쓰고 파라미터를 변경
        * Event -> EventDto
        * 그리고 Event를 새로 생성 할 때는 ModelMapper를 사용하여 간단하게 EventDto를 Event로 변환
        * 이때 만든 Event는 기존과 다르게 파라미터로 받은 것이 아니기 때문에
        * 테스트부분에서 Mockito로 Mocking한 부분에서 객체가 달라서 처리가 안됨.*/
        Event event = this.modelMapper.map(eventDto, Event.class);
        event.update();
        log.debug("modelmapper eventDto convert event : " + event.toString());
        Event newEvent = this.eventRepository.save(event);


        /*
        * Link를 생성하여 URI로 변환 : /api/events/{id}
        * used link in method (if used link in class : linkTo(EventController.class))
        * ControllerLinkBuilder @Deprecated
        *  : ControllerLinkBuilder replaced WebMvcLinkBuilder.
        * */
//        URI createdUri = linkTo(EventController.class).slash("{id}").toUri();
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        // RepresentationModel을 상속받은 Resource를 사용하면 link 데이터를 추가 할 수 있다.
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        // Deprecated Link(String) constructor
//        eventResource.add(new Link("/docs/index-kr.html#resources-events-create").withRel("profile"));
        eventResource.add(Link.of("/docs/index-kr.html#resources-events-create").withRel("profile"));

//        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));

        // return no body
        //return ResponseEntity.created(createdUri).build();
        // return body
        //return ResponseEntity.created(createdUri).body(event);
        // return body and Hateoas link data
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    // return bad-request ResponseEntity used ErrorResource
    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
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

    /*
    * Page 관련 헤더 값이 오면 해당 컨트롤러가 호출 된다
    *
    * 앞뒤 페이지에 대한 정보가 없을 때 link를 담아 줘야하는데
    * 이전처럼 Resource를 만들지 않고
    * PagedResourcesAssembler<T>를 사용하여 repository에서 받아온 페이지를 Model(before. Resource)로 변경 할 수 있음.
    * */
    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {

        Page<Event> page = this.eventRepository.findAll(pageable);
//        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, entity -> new EventResource(entity));
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, entity -> EventResource.of(entity));
//        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, entity -> EventModel.of(entity));
        pagedModel.add(Link.of("/docs/index-kr.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {

        Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if(optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index-kr.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping
    public @ResponseBody ResponseEntity eventUpdate(@RequestBody @Valid Event event, Errors errors) {

        // request body mapping validate error return Bad-Request
        if(errors.hasErrors()) {
            log.error("event request body mapping validate has error.");
            return badRequest(errors);
        }

        // event validate error retrun Bad-Request
//        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        this.eventValidator.validate(event, errors);
        if(errors.hasErrors()) {
            log.error("eventValidation has error.");
            return badRequest(errors);
        }

        // auth check
        if(!event.getName().equalsIgnoreCase("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // not found event
        if(this.eventRepository.findById(event.getId()).isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        Event updatedEvent = this.eventRepository.save(event);

        EventResource eventResource = new EventResource(updatedEvent);
        eventResource.add(Link.of("/docs/index-kr.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }
}
