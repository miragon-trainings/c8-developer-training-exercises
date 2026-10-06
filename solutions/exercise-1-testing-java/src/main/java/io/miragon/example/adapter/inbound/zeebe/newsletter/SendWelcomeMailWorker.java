package io.miragon.example.adapter.inbound.zeebe.newsletter;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.newsletter.SendWelcomeMailUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendWelcomeMailWorker {
	
	private static final Logger log = LoggerFactory.getLogger(SendWelcomeMailWorker.class);
	
	private final SendWelcomeMailUseCase useCase;
	
	public SendWelcomeMailWorker(SendWelcomeMailUseCase useCase) {
		this.useCase = useCase;
	}
	
	@JobWorker(type = ServiceTasks.NEWSLETTER_SEND_WELCOME_MAIL)
	public void handle(@Variable String subscriptionId) {
		log.debug("Received job to send welcome mail for subscriptionId: {}", subscriptionId);
		useCase.sendWelcomeMail(new SubscriptionId(UUID.fromString(subscriptionId)));
	}
	
}
