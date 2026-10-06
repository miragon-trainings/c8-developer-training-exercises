package io.miragon.example.adapter.inbound.zeebe.newsletter

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.newsletter.AbortSubscriptionUseCase
import io.miragon.example.domain.SubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*

@Component
class AbortRegistrationWorker(
    private val useCase: AbortSubscriptionUseCase
) {

    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.NEWSLETTER_ABORT_REGISTRATION)
    fun handle(@Variable subscriptionId: String) {
        log.debug { "Received job to abort registration for subscriptionId: $subscriptionId" }
        useCase.abort(SubscriptionId(UUID.fromString(subscriptionId)))
    }
}
