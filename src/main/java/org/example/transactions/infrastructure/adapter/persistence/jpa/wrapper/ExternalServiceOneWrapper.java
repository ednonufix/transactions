package org.example.transactions.infrastructure.adapter.persistence.jpa.wrapper;

import org.springframework.stereotype.Component;

import org.example.transactions.domain.port.service.ExternalServiceOne;
import org.example.transactions.infrastructure.adapter.persistence.jpa.mapper.ExampleMapper;
import org.example.transactions.infrastructure.adapter.persistence.jpa.model.ExampleModel;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ExternalServiceOneWrapper {

	private final ExternalServiceOne serviceOne;
	private final ExampleMapper mapper;

	public Mono<ExampleModel> logic() {
		return serviceOne.logic().map(mapper::toModel);
	}

}
