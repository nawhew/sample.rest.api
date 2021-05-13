package com.example.sample.rest.api.events.domain;

import com.example.sample.rest.api.events.domain.Event;
import com.example.sample.rest.api.events.web.EventController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventModel extends EntityModel<Event> {

    private static WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class);

    public static EntityModel<Event> of(Event event, String profile){
        List<Link> links = getSelfLink(event);
        links.add(Link.of(profile, "profile"));
        return EntityModel.of(event, links);
    }

    public static EntityModel<Event> of(Event event){
        List<Link> links = getSelfLink(event);
        return EntityModel.of(event, links);
    }

    private static List<Link> getSelfLink(Event event) {
        List<Link> links = new ArrayList<>();
        links.add(selfLinkBuilder.slash(event.getId()).withSelfRel());
        return links;
    }

    public static URI getCreatedUri(Event event) {
        return selfLinkBuilder.slash(event.getId()).toUri();
    }
}
