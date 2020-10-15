package com.example.sample.rest.api.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
    // Class안의 모든 Handler들은 HAL_FORMS_JSON_VALUE이라는 content type으로 응답을 보낼것이다.
public class EventController {

    /**
     * 이벤트 생성
     * Header에 location 정보 반환 (/api/events/{id})
     * @return 
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {

        /*
        * Link를 생성하여 URI로 변환 : /api/events/{id}
        * used link in method (if used link in class : linkTo(EventController.class))
        * ControllerLinkBuilder @Deprecated
        *  : ControllerLinkBuilder replaced WebMvcLinkBuilder.
        * */
        URI createdUri = linkTo(EventController.class).slash("{id}").toUri();
        /*
        * 클래스에 @RequestMapping 쓰기 전
        * 변경 이유는 아래와 같이 하는 경우 메소드에 파라미터가 추가 될 떄마다 수정해주어야 해서
        * 링크를 클래스에 달아서 변경
        * */
        /*URI createdUri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}")
                .toUri();*/

        // id setting
        event.setId(10); //우선 임의의 값 세팅

        // return no body
        //return ResponseEntity.created(createdUri).build();
        return ResponseEntity.created(createdUri).body(event);
    }
}
