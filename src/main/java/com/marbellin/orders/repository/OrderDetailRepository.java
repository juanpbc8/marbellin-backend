package com.marbellin.orders.repository;

import com.marbellin.orders.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
