package org.example.transactions.infrastructure;

import org.example.transactions.application.service.ExternalServiceOneImpl;
import org.example.transactions.application.service.ExternalServiceTwoImpl;
import org.example.transactions.domain.port.ExampleDefinitions;
import org.example.transactions.infrastructure.adapter.persistence.jpa.mapper.ExampleMapperImpl;
import org.example.transactions.infrastructure.adapter.persistence.jpa.model.ExampleModel;
import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.impl.JpaTransactionManagerImpl;
import org.example.transactions.infrastructure.adapter.persistence.jpa.transaction.provider.DefaultJpaTransactionPersistenceProvider;
import org.example.transactions.infrastructure.adapter.persistence.jpa.wrapper.ExternalServiceOneWrapper;
import org.example.transactions.infrastructure.adapter.persistence.jpa.wrapper.ExternalServiceTwoWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManagerFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataJpaTest
@Import({ExampleDefinitionsImpl.class, ExternalServiceOneWrapper.class, ExternalServiceTwoWrapper.class, ExampleMapperImpl.class})
class ExampleDefinitionsImplTest {

	@SpyBean
	private ExternalServiceOneImpl externalServiceOne;
	@SpyBean
	private ExternalServiceTwoImpl externalServiceTwo;
	@SpyBean
	private JpaTransactionManagerImpl jpaTransactionManager;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private ExampleDefinitions definitions;

	private DefaultJpaTransactionPersistenceProvider mockedCoordinator;

	@BeforeEach
	void setUp() {
		mockedCoordinator = Mockito.spy(new DefaultJpaTransactionPersistenceProvider(emf));
		when(jpaTransactionManager.initializeTransactionCoordinator()).thenReturn(mockedCoordinator);
	}

	@Test
	void ok() {

		var expectedResponse = new ExampleModel();
		expectedResponse.setId(2L);
		expectedResponse.setName("Example Service Two");
		expectedResponse.setDescription("Example Description Service Two");

		definitions.callMultipleLogic().as(StepVerifier::create).assertNext(
			value -> Assertions.assertThat(value).usingRecursiveComparison().ignoringFields("id")
				.isEqualTo(expectedResponse)).expectComplete().verify();

		verify(externalServiceOne, times(1)).logic();
		verify(externalServiceTwo, times(1)).logic();

		verify(mockedCoordinator, times(2)).persist(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).commit(any(ExampleModel.class));
		verify(mockedCoordinator, times(0)).rollback(any(RuntimeException.class));
	}

	@Test
	void fail_logic_1() {

		when(externalServiceOne.logic()).thenReturn(Mono.error(new RuntimeException()));

		definitions.callMultipleLogic().as(StepVerifier::create).verifyError();

		verify(mockedCoordinator, times(0)).persist(any(ExampleModel.class));
		verify(mockedCoordinator, times(0)).commit(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).rollback(any(RuntimeException.class));
	}

	@Test
	void fail_logic_2() {

		when(externalServiceTwo.logic()).thenReturn(Mono.error(new RuntimeException()));

		definitions.callMultipleLogic().as(StepVerifier::create).verifyError();

		verify(mockedCoordinator, times(1)).persist(any(ExampleModel.class));
		verify(mockedCoordinator, times(0)).commit(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).rollback(any(RuntimeException.class));
	}

	@Test
	void fail_first_commit() {

		when(mockedCoordinator.persist(any(ExampleModel.class))).thenReturn(Mono.error(new RuntimeException()));

		definitions.callMultipleLogic().as(StepVerifier::create).verifyError();

		verify(mockedCoordinator, times(1)).persist(any(ExampleModel.class));
		verify(mockedCoordinator, times(0)).commit(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).rollback(any(RuntimeException.class));

	}

	@Test
	void fail_second_persist() {

		AtomicInteger callCount = new AtomicInteger(0);

		when(mockedCoordinator.persist(any(ExampleModel.class))).thenAnswer(invocation -> {
			callCount.incrementAndGet();
			if (callCount.get() == 2) {
				throw new RuntimeException("Error on second call");
			}
			else {
				return invocation.callRealMethod();
			}
		});

		definitions.callMultipleLogic().as(StepVerifier::create).verifyError();

		verify(mockedCoordinator, times(2)).persist(any(ExampleModel.class));
		verify(mockedCoordinator, times(0)).commit(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).rollback(any(RuntimeException.class));

	}

	@Test
	void fail_commit() {

		when(mockedCoordinator.commit(any(ExampleModel.class))).thenReturn(Mono.error(new RuntimeException()));

		definitions.callMultipleLogic().as(StepVerifier::create).verifyError();

		verify(mockedCoordinator, times(2)).persist(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).commit(any(ExampleModel.class));
		verify(mockedCoordinator, times(1)).rollback(any(RuntimeException.class));

	}

}