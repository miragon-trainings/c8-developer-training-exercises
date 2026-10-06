package io.miragon.example.adapter.inbound.rest.bike

import io.miragon.example.application.port.outbound.bike.BikeSubscriptionProcess
import io.miragon.example.domain.bike.BikeSubscriptionId
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/bike-subscriptions/{subscriptionId}")
class BikeSubscriptionMessageController(
    private val processPort: BikeSubscriptionProcess
) {
    private val log = KotlinLogging.logger {}

    @PostMapping("/payment-received")
    fun paymentReceived(@PathVariable subscriptionId: UUID): ResponseEntity<Void> {
        log.debug { "Payment received for bike subscription: $subscriptionId" }
        processPort.sendPaymentReceived(BikeSubscriptionId(subscriptionId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/cancel")
    fun cancelRequest(@PathVariable subscriptionId: UUID): ResponseEntity<Void> {
        log.debug { "Bike subscription canceled: $subscriptionId" }
        processPort.sendRequestCanceled(BikeSubscriptionId(subscriptionId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/bike-received")
    fun bikeReceived(@PathVariable subscriptionId: UUID): ResponseEntity<Void> {
        log.debug { "Bike received confirmation for subscription: $subscriptionId" }
        processPort.sendBikeReceived(BikeSubscriptionId(subscriptionId))
        return ResponseEntity.ok().build()
    }
}
