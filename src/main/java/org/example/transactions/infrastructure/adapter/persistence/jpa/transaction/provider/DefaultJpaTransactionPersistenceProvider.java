package org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.provider;

import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.JpaTransactionPersistenceProvider;

import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.time.Duration;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DefaultJpaTransactionPersistenceProvider implements JpaTransactionPersistenceProvider {

	private final EntityManager em;
	private final EntityTransaction entityTransaction;
	private final Scheduler transactionScheduler;

	public DefaultJpaTransactionPersistenceProvider(EntityManagerFactory emf) {
		this.em = emf.createEntityManager();
		this.entityTransaction = em.getTransaction();
		this.entityTransaction.begin();
		this.transactionScheduler = Schedulers.newSingle("transaction-coordinator-%s".formatted(UUID.randomUUID()));
	}

	@Override
	public <T> Mono<T> persist(T value) {
		return Mono.fromCallable(() -> em.merge(value)).subscribeOn(transactionScheduler);
	}

	@Override
	public <T> Mono<T> commit(T value) {
		return Mono.fromCallable(() -> {
			commitTransaction();
			return value;
		})
			.doAfterTerminate(this::closeTransaction)
			.subscribeOn(transactionScheduler);
	}

	@Override
	public Mono<Void> rollback(Throwable ex) {
		return Mono.fromRunnable(this::rollbackTransaction)
			.doOnError(rollbackEx -> log.error("Error in rollback", rollbackEx))
			.retryWhen(Retry.backoff(3, Duration.ofSeconds(1))).doFinally(signal -> {
			if (!SignalType.ON_COMPLETE.equals(signal)) {
				log.info("Rollback failed after retries");
			}
			closeTransaction();
		}).onErrorResume(e -> {
			log.error("Exception during rollback, EntityManager may be inconsistent", e);
			return Mono.empty();
		}).then().subscribeOn(transactionScheduler);
	}

	private void commitTransaction() {
		if (entityTransaction.isActive()) {
			log.info("Committing transaction");
			entityTransaction.commit();
		}
	}

	private void rollbackTransaction() {
		if (entityTransaction.isActive()) {
			log.info("Rolling back transaction");
			entityTransaction.rollback();
		}
	}

	private void closeTransaction() {
		if (em.isOpen()) {
			log.info("Closing transaction");
			em.close();
		}
	}

}
