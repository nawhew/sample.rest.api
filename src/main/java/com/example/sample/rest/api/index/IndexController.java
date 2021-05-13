package com.example.sample.rest.api.index;

import com.example.sample.rest.api.auth.LoginUser;
import com.example.sample.rest.api.auth.dto.SessionUser;
import com.example.sample.rest.api.events.web.EventController;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
public class IndexController {

    private final EventController eventController;

    public IndexController(EventController eventController) {
        this.eventController = eventController;
    }

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        // event list add attribute
        if(user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/api")
    public @ResponseBody RepresentationModel index() {
        // java10이후로 지역변수에 한하여 타입을 지정하지 않고 var로 사용 가능
        var index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
