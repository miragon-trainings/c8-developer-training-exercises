package io.miragon.example.adapter.outbound.db.bike;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BikeSubscriptionJpaRepository extends JpaRepository<BikeSubscriptionEntity, UUID> {
}
