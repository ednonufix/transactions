package org.example.transactions.domain.port;

import org.example.transactions.domain.port.model.Example;

import reactor.core.publisher.Mono;

public interface ExampleDefinitions {

	Mono<Example> callMultipleLogic();
}
