package io.miragon.example.adapter.inbound.zeebe.bike

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.bike.SendRejectionMailUseCase
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SendRejectionMailWorker(
    private val useCase: SendRejectionMailUseCase
) {
    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.BIKE_SEND_REJECTION_MAIL)
    fun handle(@Variable subscriptionId: String) {
        log.info { "Sending rejection mail for subscription: $subscriptionId" }
        useCase.sendRejectionMail(BikeSubscriptionId(UUID.fromString(subscriptionId)))
    }
}
