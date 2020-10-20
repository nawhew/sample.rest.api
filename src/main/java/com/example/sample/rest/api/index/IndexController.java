package com.example.sample.rest.api.index;

import com.example.sample.rest.api.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index() {
        // java10이후로 지역변수에 한하여 타입을 지정하지 않고 var로 사용 가능
        var index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
