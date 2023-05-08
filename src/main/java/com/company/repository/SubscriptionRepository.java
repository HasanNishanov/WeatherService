package com.company.repository;


import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;
import com.company.model.entity.SubscriptionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends R2dbcRepository<SubscriptionEntity, Long> {

    @Modifying
    @Query("INSERT INTO subscription(user_id, city_name) VALUES ($1, $2)")
    Mono<Void> saveSubscription(Integer user_id, String city_name);
}
