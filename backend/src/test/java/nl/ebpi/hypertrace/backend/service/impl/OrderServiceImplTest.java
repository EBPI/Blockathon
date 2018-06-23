package nl.ebpi.hypertrace.backend.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.service.OrderService;
import org.junit.Assert;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@Transactional
public class OrderServiceImplTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testCreateOrder() throws Exception {
		Order order = new Order();
		order.setManufacturer("MID2386");
		order.setOrderer("OrdererID3108");
		order.getProducts().add("auto001");
		order.getProducts().add("auto002");

		RowMapper<String> rowMapper = new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("MANUFACTURER");
			}
		};
		String orderId = orderService.createOrder(order);
		List<String> result = jdbcTemplate.query("select * from ORDER_BASE where id = ?", Collections.singletonList(orderId).toArray(), rowMapper);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("MID2386", result.get(0));
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
