package nl.ebpi.hypertrace.backend.service.impl;

import javax.sql.DataSource;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class OrderServiceImplTest {

	@Autowired
	private OrderService orderService;

	@Test
	public void testCreateOrder() throws Exception {
		Order order = new Order();
		order.setManufacturer("MID2386");
		order.setOrderer("OrdererID3108");
		order.getProducts().add("auto001");
		order.getProducts().add("auto002");

		orderService.createOrder(order);
	}

	@Configuration
	@ComponentScan("nl.ebpi.hypertrace.backend.*")
	public static class SpringConfig {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}

		@Bean
		@LiquibaseDataSource
		public DataSource dataSourceLiquibase() {
			return DataSourceBuilder
					.create()
					.username("test1")
					.password("pass1")
					.url("jdbc:derby:memory:HyperTraceDB;create=true;user=test1;password=pass1")
					.build();
		}

		@Bean
		@Primary
		public DataSource dataSource() {
			return DataSourceBuilder
					.create()
					.username("test1")
					.password("pass1")
					.url("jdbc:derby:memory:HyperTraceDB;create=true;user=test1;password=pass1")
					.build();
		}
	}
}
