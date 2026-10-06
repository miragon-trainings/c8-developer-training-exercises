package io.miragon.example.adapter.inbound.rest.newsletter;

import io.miragon.example.application.port.inbound.newsletter.ConfirmSubscriptionUseCase;
import io.miragon.example.domain.SubscriptionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class ConfirmSubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(ConfirmSubscriptionController.class);

    private final ConfirmSubscriptionUseCase useCase;

    public ConfirmSubscriptionController(ConfirmSubscriptionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/confirm/{subscriptionId}")
    public ResponseEntity<Void> confirmSubscription(@PathVariable String subscriptionId) {
        log.debug("Received REST-request to confirm subscription: {}", subscriptionId);
        useCase.confirm(new SubscriptionId(UUID.fromString(subscriptionId)));
        return ResponseEntity.ok().build();
    }

}
