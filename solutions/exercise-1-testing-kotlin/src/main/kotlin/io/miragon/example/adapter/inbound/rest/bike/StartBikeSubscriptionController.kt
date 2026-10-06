package io.miragon.example.adapter.inbound.rest.bike

import io.miragon.example.application.port.inbound.bike.StartBikeSubscriptionUseCase
import io.miragon.example.domain.Email
import io.miragon.example.domain.Name
import io.miragon.example.domain.bike.BikeId
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/bike-subscriptions")
class StartBikeSubscriptionController(
    private val useCase: StartBikeSubscriptionUseCase
) {
    private val log = KotlinLogging.logger {}

    @PostMapping
    fun startSubscription(@RequestBody request: SubscriptionRequest): ResponseEntity<SubscriptionResponse> {
        log.debug { "Received bike subscription request: $request" }

        val command = StartBikeSubscriptionUseCase.Command(
            bikeId = BikeId(UUID.fromString(request.bikeId)),
            email = Email(request.email),
            name = Name(request.name)
        )

        val subscriptionId = useCase.start(command)

        return ResponseEntity.ok(SubscriptionResponse(subscriptionId.value.toString()))
    }

    data class SubscriptionRequest(
        val bikeId: String,
        val email: String,
        val name: String
    )

    data class SubscriptionResponse(
        val subscriptionId: String
    )
}
