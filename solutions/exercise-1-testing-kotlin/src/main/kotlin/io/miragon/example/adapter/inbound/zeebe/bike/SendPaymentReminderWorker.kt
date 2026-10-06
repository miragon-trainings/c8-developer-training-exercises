package io.miragon.example.adapter.inbound.zeebe.bike

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.bike.SendPaymentReminderUseCase
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SendPaymentReminderWorker(
    private val useCase: SendPaymentReminderUseCase
) {
    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.BIKE_SEND_PAYMENT_REMINDER)
    fun handle(@Variable subscriptionId: String) {
        log.info { "Sending payment reminder for subscription: $subscriptionId" }
        useCase.sendPaymentReminder(BikeSubscriptionId(UUID.fromString(subscriptionId)))
    }
}
