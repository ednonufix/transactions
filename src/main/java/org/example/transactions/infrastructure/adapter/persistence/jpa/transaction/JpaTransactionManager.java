package org.example.transactions.infrastructure.adapter.persistence.jpa.transaction;

public interface JpaTransactionManager {
	JpaTransactionPersistenceProvider initializeTransactionCoordinator();
}
