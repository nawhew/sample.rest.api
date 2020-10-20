package com.example.sample.rest.api.common;

import com.example.sample.rest.api.index.IndexController;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {

    @JsonUnwrapped
    Errors errors;

    public ErrorsResource(Errors errors) {
        this.errors = errors;
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    @Deprecated
    public ErrorsResource(Errors content, Link... links) {
        super(content, links);
    }
}
