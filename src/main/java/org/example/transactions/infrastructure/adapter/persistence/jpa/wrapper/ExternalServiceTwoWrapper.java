package org.example.transactions.infrastructure.adapter.persistence.jpa.wrapper;

import org.example.transactions.domain.port.service.ExternalServiceTwo;
import org.example.transactions.infrastructure.adapter.persistence.jpa.mapper.ExampleMapper;
import org.example.transactions.infrastructure.adapter.persistence.jpa.model.ExampleModel;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ExternalServiceTwoWrapper {

	private final ExternalServiceTwo serviceTwo;
	private final ExampleMapper mapper;

	public Mono<ExampleModel> logic() {
		return serviceTwo.logic().map(mapper::toModel);
	}

}
