package com.example.sample.rest.api.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

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

    public Event getEvent() {
        return event;
    }
}
