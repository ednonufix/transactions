package org.example.transactions.application.service;

import org.example.transactions.domain.port.model.Example;
import org.example.transactions.domain.port.service.ExternalServiceOne;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalServiceOneImpl implements ExternalServiceOne {

	@Override
	public Mono<Example> logic() {
		log.info("Logic 1 {}", Thread.currentThread().getName());
		var name = "Example Service One";
		var description = "Example Description Service One";
		return Mono.just(new Example(null, name, description));
	}
}
