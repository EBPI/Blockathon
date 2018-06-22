package nl.ebpi.hypertrace.backend.service.impl;

import java.util.UUID;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public OrderServiceImpl() {
	}

	@Override
	public String createOrder(Order order) {
		String id = UUID.randomUUID().toString();
		// order
		jdbcTemplate.update("insert into ORDER_BASE (ID, MANUFACTURER) values (?, ?)", id, order.getManufacturer());
		// order_product
		order.getProducts().stream()
				.forEach(productID -> jdbcTemplate.update("insert into ORDER_PRODUCTS (ORDER_ID, PRODUCT_ID) values (?, ?)", id, productID));

		return id;
	}

}
