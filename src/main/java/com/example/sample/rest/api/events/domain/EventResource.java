package com.example.sample.rest.api.events.domain;

import com.example.sample.rest.api.events.domain.Event;
import com.example.sample.rest.api.events.web.EventController;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/*if use generics, RepresentationModel change EntityModel<T> */
public class EventResource extends EntityModel<Event> {

    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
        // self link는 매번 추가하기때문에 여기서 추가하면 좋음.
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        //equals : add(new Link("http://localhost:8080/api/events/" + event.getId()));
    }

    /*
    * use embedded*/
    public static EntityModel<Event> of(Event event) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        return EntityModel.of(event, links);
    }

    public Event getEvent() {
        return event;
    }
}
