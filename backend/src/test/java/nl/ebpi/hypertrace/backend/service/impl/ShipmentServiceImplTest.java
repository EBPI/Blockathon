package nl.ebpi.hypertrace.backend.service.impl;

import java.net.URISyntaxException;
import javax.sql.DataSource;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.generated.domain.TransporterResponse;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
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
						MockRestRequestMatchers.content()
								.string(StringContains.containsString("\"transporterList\":[\"TransID009\",\"TransID010\",\"TransID003\"]")))
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
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
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
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
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
						MockRestRequestMatchers.content()
								.string(StringContains.containsString("\"transporterList\":[\"TransID009\",\"TransID010\",\"TransID003\"]")))
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
								"    \"TransID009\",\n" +
								"    \"TransID010\",\n" +
								"    \"TransID003\"\n" +
								"  ],\n" +
								"  \"Destination\": \"Autoworld - Brussel\",\n" +
								"  \"Customer\": \"OrdererID3108\",\n" +
								"  \"Weight\": \"10KG\",\n" +
								"  \"transactionId\": \"7233daa4279a13a2f1bf4a2039da3ff37273164d3bad6270993cef699e7d80f3\"\n" +
								"}",
						MediaType.APPLICATION_JSON));

		mockServer
				.expect(MockRestRequestMatchers
						.requestTo(getUrlWithFilter(BlockchainRestUrl.QUERY_SHIPMENT_ON_ORDERID.getUrl(),
								"%7B%22where%22:%7B%22ClientReference%22:%221234%22%7D%7D")))
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
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
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
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
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
						MockRestRequestMatchers.content()
								.string(StringContains.containsString("\"transporterList\":[\"TransID009\",\"TransID010\",\"TransID003\"]")))
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
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
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
						.requestTo(getUrlWithFilter(BlockchainRestUrl.QUERY_SHIPMENT_ON_ORDERID.getUrl(),
								"%7B%22where%22:%7B%22ClientReference%22:%221234%22%7D%7D")))
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
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
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

	@Test
	public void testFindShipments() throws Exception {
		mockServer
				.expect(MockRestRequestMatchers.requestTo(
						getUrlWithFilter(BlockchainRestUrl.QUERY_SHIPMENT_ON_TRANSPORTERID.getUrl(), "resource:org.ebpi.blockathon.Transporter%23TransID003")))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(
						" [\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"941356b9-9590-4db5-a1ca-b20fab9c2d4a\",\n" +
								"    \"ShipmentID\": \"427860fd-6df2-4c2e-aef6-a99036289663\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID4685\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#Zalando\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [\n" +
								"      {\n" +
								"        \"$class\": \"org.ebpi.blockathon.Handover\",\n" +
								"        \"HandoverID\": \"29e9e0c1-1274-4a6f-81d0-950fd982f2fb\",\n" +
								"        \"giving\": \"resource:org.ebpi.blockathon.TradePartner#TransID003\",\n" +
								"        \"reciever\": \"resource:org.ebpi.blockathon.TradePartner#TransID003\",\n" +
								"        \"confirmed\": false,\n" +
								"        \"stateGiving\": \"MINT\",\n" +
								"        \"stateReciever\": \"undefined\",\n" +
								"        \"final\": false\n" +
								"      }\n" +
								"    ],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"d9d4a232-48c0-4d80-b1c1-1f52404dcfe4\",\n" +
								"    \"ShipmentID\": \"63593f26-3ce1-4ea5-a29c-e093c9a441db\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID1027\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID5725\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"b035ec4c-7ee3-4d3d-a703-d5c0bf6694cb\",\n" +
								"    \"ShipmentID\": \"767bb7f0-f016-437b-901d-9bccdb67b996\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#Shirt001\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID5725\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"b542298e-0e5e-4cda-a295-6965c232be6a\",\n" +
								"    \"ShipmentID\": \"7d53cfa6-a452-43cf-a97b-be14016cd280\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID4685\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#Zalando\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"941356b9-9590-4db5-a1ca-b20fab9c2d4a\",\n" +
								"    \"ShipmentID\": \"a13529e2-e475-437c-8636-f211da003069\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID4685\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#Zalando\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"d9d4a232-48c0-4d80-b1c1-1f52404dcfe4\",\n" +
								"    \"ShipmentID\": \"a3b2d0d9-882f-4948-a41f-660eab381267\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID5076\",\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID5076\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#OrdererID5725\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  },\n" +
								"  {\n" +
								"    \"$class\": \"org.ebpi.blockathon.Shipment\",\n" +
								"    \"ClientReference\": \"b542298e-0e5e-4cda-a295-6965c232be6a\",\n" +
								"    \"ShipmentID\": \"b8429f17-249e-4e23-8faf-a2bd6561b863\",\n" +
								"    \"DocumentHash\": \"hashing\",\n" +
								"    \"DocumentLocation\": \"hier\",\n" +
								"    \"ShippedProducts\": [\n" +
								"      \"resource:org.ebpi.blockathon.Product#ProductID4685\"\n" +
								"    ],\n" +
								"    \"transporterList\": [\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID009\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID010\",\n" +
								"      \"resource:org.ebpi.blockathon.Transporter#TransID003\"\n" +
								"    ],\n" +
								"    \"Destination\": \"Autoworld - Brussel\",\n" +
								"    \"Customer\": \"resource:org.ebpi.blockathon.Orderer#Zalando\",\n" +
								"    \"Supplier\": \"resource:org.ebpi.blockathon.Manufacturer#Nike\",\n" +
								"    \"Weight\": \"10KG\",\n" +
								"    \"handoverArray\": [],\n" +
								"    \"completed\": false\n" +
								"  }\n" +
								"]\n" +
								"\n",
						MediaType.APPLICATION_JSON));
		TransporterResponse shipments = shipmentService.findShipments("TransID003");
		Assert.assertEquals(6, shipments.getShipments().size());
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
