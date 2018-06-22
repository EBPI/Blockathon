package nl.ebpi.hypertrace.backend.service;

import nl.ebpi.hypertrace.backend.generated.domain.Order;

public interface OrderService {

	String createOrder(Order order);

}