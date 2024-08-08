package org.example.transactions.application.usecase;

import org.example.transactions.domain.port.ExampleDefinitions;
import org.example.transactions.domain.port.domain.Example;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExampleUseCase {

	private final ExampleDefinitions exampleDefinitions;

	public Mono<Example> exampleUseCase() {
		return exampleDefinitions.callMultipleLogic().subscribeOn(Schedulers.boundedElastic());
	}

}
