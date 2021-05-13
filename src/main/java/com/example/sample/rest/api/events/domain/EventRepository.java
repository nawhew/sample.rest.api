package com.example.sample.rest.api.events.domain;

import com.example.sample.rest.api.events.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
