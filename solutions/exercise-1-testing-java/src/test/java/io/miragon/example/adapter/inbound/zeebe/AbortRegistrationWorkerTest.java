package io.miragon.example.adapter.inbound.zeebe;

import io.miragon.example.adapter.inbound.zeebe.newsletter.AbortRegistrationWorker;
import io.miragon.example.application.port.inbound.newsletter.AbortSubscriptionUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Unit test for AbortRegistrationWorker.
 * Tests that the worker correctly extracts variables and calls the use case.
 */
class AbortRegistrationWorkerTest {
	
	private AbortSubscriptionUseCase useCase;
	private AbortRegistrationWorker underTest;
	
	@BeforeEach
	void setUp() {
		useCase = mock(AbortSubscriptionUseCase.class);
		underTest = new AbortRegistrationWorker(useCase);
	}
	
	@Test
	void shouldAbortRegistrationWhenJobIsReceived() {
		// Given
		UUID subscriptionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		
		// When
		underTest.handle(subscriptionId.toString());
		
		// Then
		verify(useCase, times(1)).abort(new SubscriptionId(subscriptionId));
		verifyNoMoreInteractions(useCase);
	}
}
