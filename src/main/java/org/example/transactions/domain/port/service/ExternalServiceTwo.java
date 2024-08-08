package org.example.transactions.domain.port.service;

import org.example.transactions.domain.port.model.Example;

import reactor.core.publisher.Mono;

public interface ExternalServiceTwo {

	Mono<Example> logic();

}
