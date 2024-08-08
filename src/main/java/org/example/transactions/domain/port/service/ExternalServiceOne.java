package org.example.transactions.domain.port.service;

import org.example.transactions.domain.port.domain.Example;

import reactor.core.publisher.Mono;

public interface ExternalServiceOne {

	Mono<Example> logic();

}
