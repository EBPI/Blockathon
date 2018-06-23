package nl.ebpi.hypertrace.backend.service.impl;

import java.net.URISyntaxException;
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
	public void testSendShipments() throws Exception {

		mockServer.expect(MockRestRequestMatchers.requestTo(BlockchainRestUrl.SEND_SHIPMENT.getUrl()))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentHash\":\"hashing\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentLocation\":\"hier\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"ShippedProducts\":[\"auto001\",\"auto002\"]")))
				.andExpect(
						MockRestRequestMatchers.content().string(StringContains.containsString("\"transporterList\":[\"TID2109\",\"TID4412\",\"POSTNL001\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Destination\":\"Autoworld - Brussel\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Customer\":\"OrdererID3108\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Weight\":\"10KG\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"ClientReference\":\"1234\"")))
				.andRespond(MockRestResponseCreators.withSuccess(
						"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"92a3ed0b-495b-45ca-b95d-d3c629339824\",\n" +
								"    \"ShipmentID\": \"1bbe97d0-7238-4d4f-9eb3-e5da2b60ebf4\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#auto002\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#NIKE001\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"92a3ed0b-495b-45ca-b95d-d3c629339824\",\n" +
								"    \"ShipmentID\": \"f17c96ec-9248-46c4-a779-0f257613a5a5\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#auto001\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#NIKE001\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  }",
						MediaType.APPLICATION_JSON));
		mockServer.expect(MockRestRequestMatchers.requestTo(BlockchainRestUrl.SEND_SHIPMENT.getUrl()))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentHash\":\"hashing\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentLocation\":\"hier\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"ShippedProducts\":[\"auto003\",\"auto004\"]")))
				.andExpect(
						MockRestRequestMatchers.content().string(StringContains.containsString("\"transporterList\":[\"TID2109\",\"TID4412\",\"POSTNL001\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Destination\":\"Autoworld - Brussel\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Customer\":\"OrdererID3108\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Weight\":\"10KG\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"ClientReference\":\"1234\"")))
				.andRespond(MockRestResponseCreators.withSuccess(
						"{\n" +
								"  \"$class\": \"org.ebpi.blockathon.sendShipment\",\n" +
								"  \"DocumentHash\": \"hashing\",\n" +
								"  \"DocumentLocation\": \"hier\",\n" +
								"  \"ShippedProducts\": [\n" +
								"    \"auto001\"\n" +
								"  ],\n" +
								"  \"transporterList\": [\n" +
								"    \"TID2109\",\n" +
								"    \"TID4412\",\n" +
								"    \"POSTNL001\"\n" +
								"  ],\n" +
								"  \"Destination\": \"Autoworld - Brussel\",\n" +
								"  \"Customer\": \"OrdererID3108\",\n" +
								"  \"Weight\": \"10KG\",\n" +
								"  \"transactionId\": \"7233daa4279a13a2f1bf4a2039da3ff37273164d3bad6270993cef699e7d80f3\"\n" +
								"}",
						MediaType.APPLICATION_JSON));

		mockServer
				.expect(MockRestRequestMatchers
						.requestTo(getUrlWithFilter(BlockchainRestUrl.QUERY_SHIPMENT.getUrl(), "%7B%22where%22:%7B%22ClientReference%22:%221234%22%7D%7D")))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(
						"[\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"92a3ed0b-495b-45ca-b95d-d3c629339824\",\n" +
								"    \"ShipmentID\": \"1bbe97d0-7238-4d4f-9eb3-e5da2b60ebf4\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#auto002\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#NIKE001\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"92a3ed0b-495b-45ca-b95d-d3c629339824\",\n" +
								"    \"ShipmentID\": \"f17c96ec-9248-46c4-a779-0f257613a5a5\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#auto001\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#NIKE001\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  }\n" +
								"]",
						MediaType.APPLICATION_JSON));

		Order order = new Order();
		order.setManufacturer("MID2386");
		order.setOrderer("OrdererID3108");
		order.getProducts().add("auto001");
		order.getProducts().add("auto002");
		order.getProducts().add("auto003");
		order.getProducts().add("auto004");

		shipmentService.sendShipments(order, "1234");
	}

	@Test
	public void testSendShipmentOneProduct() throws Exception {

		mockServer.expect(MockRestRequestMatchers.requestTo(BlockchainRestUrl.SEND_SHIPMENT.getUrl()))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentHash\":\"hashing\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"DocumentLocation\":\"hier\"")))
				.andExpect(MockRestRequestMatchers.content()
						.string(StringContains.containsString("\"ShippedProducts\":[\"auto001\"]")))
				.andExpect(
						MockRestRequestMatchers.content().string(StringContains.containsString("\"transporterList\":[\"TID2109\",\"TID4412\",\"POSTNL001\"]")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Destination\":\"Autoworld - Brussel\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Customer\":\"OrdererID3108\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"Weight\":\"10KG\"")))
				.andExpect(MockRestRequestMatchers.content().string(StringContains.containsString("\"ClientReference\":\"1234\"")))
				.andRespond(MockRestResponseCreators.withSuccess(
						"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"92a3ed0b-495b-45ca-b95d-d3c629339824\",\n" +
								"    \"ShipmentID\": \"1bbe97d0-7238-4d4f-9eb3-e5da2b60ebf4\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#auto001\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#NIKE001\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  }",
						MediaType.APPLICATION_JSON));

		mockServer
				.expect(MockRestRequestMatchers
						.requestTo(getUrlWithFilter(BlockchainRestUrl.QUERY_SHIPMENT.getUrl(), "%7B%22where%22:%7B%22ClientReference%22:%221234%22%7D%7D")))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(
						"[\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"92a3ed0b-495b-45ca-b95d-d3c629339824\",\n" +
								"    \"ShipmentID\": \"1bbe97d0-7238-4d4f-9eb3-e5da2b60ebf4\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#auto002\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID2109\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TID4412\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#POSTNL001\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID3108\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#NIKE001\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  }" +
								"]",
						MediaType.APPLICATION_JSON));

		Order order = new Order();
		order.setManufacturer("MID2386");
		order.setOrderer("OrdererID3108");
		order.getProducts().add("auto001");

		shipmentService.sendShipments(order, "1234");
	}

	private String getUrlWithFilter(String url, String where) throws URISyntaxException {
		return url.replace("{query}", where);
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
