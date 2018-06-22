package nl.ebpi.hypertrace.backend.application;

import java.util.List;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.generated.domain.OrderConfirmation;
import nl.ebpi.hypertrace.backend.service.OrderService;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ShipmentService shipmentService;

	@RequestMapping(value = "/order", method = RequestMethod.POST)

	public @ResponseBody OrderConfirmation placeOrder(@RequestBody Order order) {
		String orderId = orderService.createOrder(order);
		List<String> createShipments = shipmentService.createShipments(order);

		return null;
	}
}