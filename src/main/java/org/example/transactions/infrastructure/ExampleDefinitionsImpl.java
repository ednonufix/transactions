package org.example.transactions.infrastructure;

import org.example.transactions.domain.port.ExampleDefinitions;
import org.example.transactions.domain.port.domain.Example;
import org.example.transactions.infrastructure.adapter.persistence.jpa.mapper.ExampleMapper;
import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.JpaTransactionManager;
import org.example.transactions.infrastructure.adapter.persistence.jpa.wrapper.ExternalServiceOneWrapper;
import org.example.transactions.infrastructure.adapter.persistence.jpa.wrapper.ExternalServiceTwoWrapper;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExampleDefinitionsImpl implements ExampleDefinitions {

	private final ExternalServiceOneWrapper externalServiceOne;
	private final ExternalServiceTwoWrapper externalServiceTwo;
	private final JpaTransactionManager jpaTransactionManager;
	private final ExampleMapper exampleMapper;

	@Override
	public Mono<Example> callMultipleLogic() {
		var transactionCoordinator = jpaTransactionManager.initializeTransactionCoordinator();
		return externalServiceOne.logic()
			.flatMap(transactionCoordinator::persist)
			.then(externalServiceTwo.logic())
			.flatMap(transactionCoordinator::persist)
			.flatMap(transactionCoordinator::commit)
			.map(exampleMapper::toDomain)
			.doOnError(transactionCoordinator::rollback)
			.subscribeOn(Schedulers.boundedElastic());
	}
}
