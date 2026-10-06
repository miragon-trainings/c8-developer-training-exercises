package io.miragon.example.adapter.inbound.zeebe.bike;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.bike.SendRejectionMailUseCase;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendRejectionMailWorker {

    private static final Logger log = LoggerFactory.getLogger(SendRejectionMailWorker.class);

    private final SendRejectionMailUseCase useCase;

    public SendRejectionMailWorker(SendRejectionMailUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.BIKE_SEND_REJECTION_MAIL)
    public void handle(@Variable String subscriptionId) {
        log.info("Sending rejection mail for subscription: {}", subscriptionId);
        useCase.sendRejectionMail(new BikeSubscriptionId(UUID.fromString(subscriptionId)));
    }

}
