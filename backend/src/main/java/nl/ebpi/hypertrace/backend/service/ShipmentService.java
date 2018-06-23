package nl.ebpi.hypertrace.backend.service;

import java.util.List;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.generated.domain.TransporterResponse;

public interface ShipmentService {

	List<String> sendShipments(Order order, String orderId);

	TransporterResponse findShipments(String transporterId);

}