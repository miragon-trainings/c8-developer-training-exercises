package io.miragon.example.adapter.inbound.rest.bike;

import io.miragon.example.application.port.outbound.bike.BikeSubscriptionProcess;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/bike-subscriptions/{subscriptionId}")
public class BikeSubscriptionMessageController {

    private static final Logger log = LoggerFactory.getLogger(BikeSubscriptionMessageController.class);

    private final BikeSubscriptionProcess processPort;

    public BikeSubscriptionMessageController(BikeSubscriptionProcess processPort) {
        this.processPort = processPort;
    }

    @PostMapping("/payment-received")
    public ResponseEntity<Void> paymentReceived(@PathVariable UUID subscriptionId) {
        log.debug("Payment received for bike subscription: {}", subscriptionId);
        processPort.sendPaymentReceived(new BikeSubscriptionId(subscriptionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable UUID subscriptionId) {
        log.debug("Bike subscription canceled: {}", subscriptionId);
        processPort.sendRequestCanceled(new BikeSubscriptionId(subscriptionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bike-received")
    public ResponseEntity<Void> bikeReceived(@PathVariable UUID subscriptionId) {
        log.debug("Bike received confirmation for subscription: {}", subscriptionId);
        processPort.sendBikeReceived(new BikeSubscriptionId(subscriptionId));
        return ResponseEntity.ok().build();
    }

}
