package io.miragon.example.adapter.inbound.zeebe.bike;

import io.miragon.example.adapter.process.generated.ServiceTasks;
import io.miragon.example.application.port.inbound.bike.SendPaymentReminderUseCase;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendPaymentReminderWorker {

    private static final Logger log = LoggerFactory.getLogger(SendPaymentReminderWorker.class);

    private final SendPaymentReminderUseCase useCase;

    public SendPaymentReminderWorker(SendPaymentReminderUseCase useCase) {
        this.useCase = useCase;
    }

    @JobWorker(type = ServiceTasks.BIKE_SEND_PAYMENT_REMINDER)
    public void handle(@Variable String subscriptionId) {
        log.info("Sending payment reminder for subscription: {}", subscriptionId);
        useCase.sendPaymentReminder(new BikeSubscriptionId(UUID.fromString(subscriptionId)));
    }

}
