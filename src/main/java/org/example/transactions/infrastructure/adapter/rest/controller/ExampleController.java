package org.example.transactions.infrastructure.adapter.rest.controller;

import org.example.transactions.application.usecase.ExampleUseCase;
import org.example.transactions.domain.port.model.Example;
import org.example.transactions.infrastructure.adapter.rest.ui.ExampleRestUI;

import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ExampleController implements ExampleRestUI {

	private final ExampleUseCase useCase;

	@Override
	public Mono<Example> example() {
		return useCase.exampleUseCase();
	}

}
