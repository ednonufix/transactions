package org.example.transactions.infrastructure.adapter.persistence.jpa.mapper;

import org.example.transactions.domain.port.domain.Example;
import org.example.transactions.infrastructure.adapter.persistence.jpa.model.ExampleModel;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExampleMapper {

	Example toDomain(ExampleModel exampleModel);

	ExampleModel toModel(Example example);

}
