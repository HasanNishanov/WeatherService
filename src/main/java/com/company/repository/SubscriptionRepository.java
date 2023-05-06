package com.company.repository;


import com.company.model.entity.SubscriptionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends R2dbcRepository<SubscriptionEntity, String> {
}
