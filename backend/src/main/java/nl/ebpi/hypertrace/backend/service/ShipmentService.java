package nl.ebpi.hypertrace.backend.service;

import java.util.List;
import nl.ebpi.hypertrace.backend.generated.domain.Order;

public interface ShipmentService {

	List<String> createShipments(Order order);

	List<String> sendShipments(Order order);

}