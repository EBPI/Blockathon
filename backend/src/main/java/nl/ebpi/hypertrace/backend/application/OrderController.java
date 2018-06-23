package nl.ebpi.hypertrace.backend.application;

import java.util.List;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.generated.domain.OrderConfirmation;
import nl.ebpi.hypertrace.backend.service.OrderService;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ShipmentService shipmentService;

	@PostMapping(value = "/order", consumes = "application/json", produces = { "application/json" })
	public @ResponseBody OrderConfirmation placeOrder(@RequestBody Order order) {
		LOG.debug("creating order {}", order);
		String orderId = orderService.createOrder(order);
		List<String> createdShipments = shipmentService.sendShipments(order, orderId);

		OrderConfirmation orderConfirmation = new OrderConfirmation();
		orderConfirmation.setOrderId(orderId);
		orderConfirmation.setShipmentIds(createdShipments);
		return orderConfirmation;
	}
}