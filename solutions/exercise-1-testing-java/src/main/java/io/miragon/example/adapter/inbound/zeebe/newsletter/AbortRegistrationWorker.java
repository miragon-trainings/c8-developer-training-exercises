package io.miragon.example.adapter.inbound.zeebe.newsletter;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.newsletter.AbortSubscriptionUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AbortRegistrationWorker {
	
	private static final Logger log = LoggerFactory.getLogger(AbortRegistrationWorker.class);
	
	private final AbortSubscriptionUseCase useCase;
	
	public AbortRegistrationWorker(AbortSubscriptionUseCase useCase) {
		this.useCase = useCase;
	}
	
	@JobWorker(type = ServiceTasks.NEWSLETTER_ABORT_REGISTRATION)
	public void handle(@Variable String subscriptionId) {
		log.debug("Received job to abort registration for subscriptionId: {}", subscriptionId);
		useCase.abort(new SubscriptionId(UUID.fromString(subscriptionId)));
	}
	
}
