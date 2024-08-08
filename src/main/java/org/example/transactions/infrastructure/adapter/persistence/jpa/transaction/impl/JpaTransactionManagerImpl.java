package org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.impl;

import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.JpaTransactionManager;
import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.JpaTransactionPersistenceProvider;
import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.provider.DefaultJpaTransactionPersistenceProvider;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JpaTransactionManagerImpl implements JpaTransactionManager {

	private final EntityManagerFactory emf;

	public JpaTransactionPersistenceProvider initializeTransactionCoordinator() {
		return new DefaultJpaTransactionPersistenceProvider(emf);
	}
}
