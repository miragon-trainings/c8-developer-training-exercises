package io.miragon.example.adapter.inbound.zeebe.bike

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.bike.SendBikeConfirmationMailUseCase
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SendBikeConfirmationMailWorker(
    private val useCase: SendBikeConfirmationMailUseCase
) {
    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.BIKE_SEND_CONFIRMATION_MAIL)
    fun handle(@Variable subscriptionId: String) {
        log.info { "Sending bike confirmation mail for subscription: $subscriptionId" }
        useCase.sendConfirmationMail(BikeSubscriptionId(UUID.fromString(subscriptionId)))
    }
}
