package nl.ebpi.hypertrace.backend.service.impl;

import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class ShipmentServiceImplTest {
	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;

	@Before
	public void setUp() throws Exception {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void testCreateShipments() throws Exception {

		mockServer.expect(MockRestRequestMatchers.requestTo(BlockchainRestUrl.ADD_SHIPMENT.getUrl()))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"ShipmentID\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentHash\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentLocation\":\"doclocation\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"ShippedProducts\":[\"resource:org.ebpi.blockathon.Product#auto001\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString(
						"\"transporterList\":[\"resource:org.ebpi.blockathon.Transporter#TID2109\",\"resource:org.ebpi.blockathon.Transporter#TID4412\","
								+ "\"resource:org.ebpi.blockathon.Transporter#POSTNL001\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Destination\":\"autoworld - Brussel\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"Customer\":\"resource:org.ebpi.blockathon.Orderer#OrdererID3108\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"Supplier\":\"resource:org.ebpi.blockathon.Manufacturer#MID2386\"")))
				.andRespond(MockRestResponseCreators.withSuccess(
						"{\n" +
								"  \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"  \"ShipmentID\": \"123456789\",\n" +
								"  \"DocumentHash\": \"string\",\n" +
								"  \"DocumentLocation\": \"string\",\n" +
								"  \"ShippedProducts\": [\n" +
								"    \"resource:org.ebpi.blockathon.Product#auto001\"\n" +
								"  ],\n" +
								"  \"transporterList\": [\n" +
								"    \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"    \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"    \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"  ],\n" +
								"  \"Destination\": \"Brussel\",\n" +
								"  \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"  \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#MID2386\",\n" +
								"  \"Weight\": \"10KG\"\n" +
								"}",
						MediaType.APPLICATION_JSON));
		mockServer.expect(MockRestRequestMatchers.requestTo(BlockchainRestUrl.ADD_SHIPMENT.getUrl()))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"ShipmentID\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentHash\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentLocation\":\"doclocation\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"ShippedProducts\":[\"resource:org.ebpi.blockathon.Product#auto002\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString(
						"\"transporterList\":[\"resource:org.ebpi.blockathon.Transporter#TID2109\",\"resource:org.ebpi.blockathon.Transporter#TID4412\","
								+ "\"resource:org.ebpi.blockathon.Transporter#POSTNL001\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Destination\":\"autoworld - Brussel\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"Customer\":\"resource:org.ebpi.blockathon.Orderer#OrdererID3108\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"Supplier\":\"resource:org.ebpi.blockathon.Manufacturer#MID2386\"")))
				.andRespond(MockRestResponseCreators.withSuccess(
						"{\n" +
								"  \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"  \"ShipmentID\": \"123456789\",\n" +
								"  \"DocumentHash\": \"string\",\n" +
								"  \"DocumentLocation\": \"string\",\n" +
								"  \"ShippedProducts\": [\n" +
								"    \"resource:org.ebpi.blockathon.Product#auto002\"\n" +
								"  ],\n" +
								"  \"transporterList\": [\n" +
								"    \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"    \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"    \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"  ],\n" +
								"  \"Destination\": \"Brussel\",\n" +
								"  \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"  \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#MID2386\",\n" +
								"  \"Weight\": \"10KG\"\n" +
								"}",
						MediaType.APPLICATION_JSON));

		Order order = new Order();
		order.setManufacturer("MID2386");
		order.setOrderer("OrdererID3108");
		order.getProducts().add("auto001");
		order.getProducts().add("auto002");

		shipmentService.createShipments(order);
	}

	@Configuration
	@ComponentScan("nl.ebpi.hypertrace.backend.*")
	public static class SpringConfig {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}

	}

}
