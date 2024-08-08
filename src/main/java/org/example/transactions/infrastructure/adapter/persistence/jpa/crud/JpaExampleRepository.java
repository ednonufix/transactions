package org.example.transactions.infrastructure.adapter.persistence.jpa.crud;

import org.example.transactions.infrastructure.adapter.persistence.jpa.model.ExampleModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaExampleRepository extends JpaRepository<ExampleModel, Long> {
}
