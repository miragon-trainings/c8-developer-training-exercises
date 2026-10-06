package io.miragon.example.adapter.inbound.zeebe.newsletter;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.newsletter.SendConfirmationMailUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendConfirmationMailWorker {
	
	private static final Logger log = LoggerFactory.getLogger(SendConfirmationMailWorker.class);
	
	private final SendConfirmationMailUseCase useCase;
	
	public SendConfirmationMailWorker(SendConfirmationMailUseCase useCase) {
		this.useCase = useCase;
	}
	
	@JobWorker(type = ServiceTasks.NEWSLETTER_SEND_CONFIRMATION_MAIL)
	public void handle(@Variable String subscriptionId) {
		log.debug("Received job to send confirmation mail for subscriptionId: {}", subscriptionId);
		useCase.sendConfirmationMail(new SubscriptionId(UUID.fromString(subscriptionId)));
	}
	
}
