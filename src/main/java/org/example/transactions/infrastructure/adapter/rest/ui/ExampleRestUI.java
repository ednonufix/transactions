package org.example.transactions.infrastructure.adapter.rest.ui;

import org.example.transactions.domain.port.domain.Example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;


@RequestMapping("/example")
public interface ExampleRestUI {

	@GetMapping("/")
	Mono<Example> example();

}
