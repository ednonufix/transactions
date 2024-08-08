package org.example.transactions.infrastructure.adapter.persistence.jpa.transaction;

import reactor.core.publisher.Mono;

public interface JpaTransactionPersistenceProvider {

	<T> Mono<T> persist(T value);

	<T> Mono<T> commit(T value);

	Mono<Void> rollback(Throwable ex);
}
