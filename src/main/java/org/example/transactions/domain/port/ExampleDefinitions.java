package org.example.transactions.domain.port;

import org.example.transactions.domain.port.domain.Example;

import reactor.core.publisher.Mono;

public interface ExampleDefinitions {

	Mono<Example> callMultipleLogic();
}
