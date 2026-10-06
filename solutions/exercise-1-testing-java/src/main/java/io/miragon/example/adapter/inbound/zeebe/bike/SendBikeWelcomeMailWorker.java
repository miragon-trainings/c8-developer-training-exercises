package io.miragon.example.adapter.inbound.zeebe.bike;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.bike.SendBikeWelcomeMailUseCase;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendBikeWelcomeMailWorker {

    private static final Logger log = LoggerFactory.getLogger(SendBikeWelcomeMailWorker.class);

    private final SendBikeWelcomeMailUseCase useCase;

    public SendBikeWelcomeMailWorker(SendBikeWelcomeMailUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.BIKE_SEND_WELCOME_MAIL)
    public void handle(@Variable String subscriptionId) {
        log.info("Sending welcome mail for subscription: {}", subscriptionId);
        useCase.sendWelcomeMail(new BikeSubscriptionId(UUID.fromString(subscriptionId)));
    }

}
