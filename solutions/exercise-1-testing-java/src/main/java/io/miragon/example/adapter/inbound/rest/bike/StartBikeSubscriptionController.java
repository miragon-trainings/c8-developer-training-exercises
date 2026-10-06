package io.miragon.example.adapter.inbound.rest.bike;

import io.miragon.example.application.port.inbound.bike.StartBikeSubscriptionUseCase;
import io.miragon.example.domain.Email;
import io.miragon.example.domain.Name;
import io.miragon.example.domain.bike.BikeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/bike-subscriptions")
public class StartBikeSubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(StartBikeSubscriptionController.class);

    private final StartBikeSubscriptionUseCase useCase;

    public StartBikeSubscriptionController(StartBikeSubscriptionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> startSubscription(@RequestBody SubscriptionRequest request) {
        log.debug("Received bike subscription request: {}", request);

        var command = new StartBikeSubscriptionUseCase.Command(
            new BikeId(UUID.fromString(request.bikeId())),
            new Email(request.email()),
            new Name(request.name())
        );

        var subscriptionId = useCase.start(command);

        return ResponseEntity.ok(new SubscriptionResponse(subscriptionId.value().toString()));
    }

    public record SubscriptionRequest(
        String bikeId,
        String email,
        String name
    ) {
    }

    public record SubscriptionResponse(
        String subscriptionId
    ) {
    }

}
