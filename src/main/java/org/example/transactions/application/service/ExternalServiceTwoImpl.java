package org.example.transactions.application.service;

import org.example.transactions.domain.port.model.Example;
import org.example.transactions.domain.port.service.ExternalServiceTwo;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalServiceTwoImpl implements ExternalServiceTwo {

	@Override
	public Mono<Example> logic() {
		log.info("Logic 2 {}", Thread.currentThread().getName());
		var name = "Example Service Two";
		var description = "Example Description Service Two";
		return Mono.just(new Example(null, name, description));
	}
}
