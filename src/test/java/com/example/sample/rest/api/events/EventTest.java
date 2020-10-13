package com.example.sample.rest.api.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("hello rest api")
                .description("rest api class!")
                .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // given
        Event event = new Event();
        String name = "hello event";
        String description = "rest api class!";

        // when
        event.setName(name);
        event.setDescription(description);

        // then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }
}