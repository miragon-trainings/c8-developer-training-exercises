package io.miragon.example.adapter.inbound.zeebe.bike

import io.miragon.example.adapter.process.generated.ServiceTasks
import io.miragon.example.application.port.inbound.bike.CheckBikeAvailabilityUseCase
import io.miragon.example.domain.bike.BikeSubscriptionId
import io.camunda.client.annotation.JobWorker
import io.camunda.client.annotation.Variable
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CheckBikeAvailabilityWorker(
    private val useCase: CheckBikeAvailabilityUseCase
) {
    private val log = KotlinLogging.logger {}

    @JobWorker(type = ServiceTasks.BIKE_CHECK_AVAILABILITY)
    fun handle(@Variable subscriptionId: String): Map<String, Any> {
        log.info { "Checking bike availability for subscription: $subscriptionId" }
        val available = useCase.checkAvailability(BikeSubscriptionId(UUID.fromString(subscriptionId)))
        return mapOf("bikeAvailable" to available)
    }
}
