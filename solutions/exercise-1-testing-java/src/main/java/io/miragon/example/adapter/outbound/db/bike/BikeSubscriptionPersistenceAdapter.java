package io.miragon.example.adapter.outbound.db.bike;

import io.miragon.example.application.port.outbound.bike.BikeSubscriptionRepository;
import io.miragon.example.domain.bike.BikeSubscription;
import io.miragon.example.domain.bike.BikeSubscriptionId;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class BikeSubscriptionPersistenceAdapter implements BikeSubscriptionRepository {

    private final BikeSubscriptionJpaRepository jpaRepository;
    private final BikeSubscriptionEntityMapper mapper;

    public BikeSubscriptionPersistenceAdapter(
        BikeSubscriptionJpaRepository jpaRepository,
        BikeSubscriptionEntityMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BikeSubscription save(BikeSubscription subscription) {
        var entity = mapper.toEntity(subscription);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public BikeSubscription find(BikeSubscriptionId id) {
        var entity = jpaRepository.findById(id.value())
            .orElseThrow(() -> new NoSuchElementException("BikeSubscription with id " + id.value() + " not found"));
        return mapper.toDomain(entity);
    }

    @Override
    public BikeSubscription search(BikeSubscriptionId id) {
        return jpaRepository.findById(id.value())
            .map(mapper::toDomain)
            .orElse(null);
    }

}
