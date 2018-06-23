package nl.ebpi.hypertrace.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainSendShipmentQuery;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainSendShipmentRequest;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainSendShipmentReturn;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainShipment;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainWhere;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShipmentServiceImpl implements ShipmentService {

	private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public ShipmentServiceImpl() {
	}

	@Override
	public List<String> sendShipments(Order order, String orderId) {
		List<String> shipmentIds = new ArrayList<>();

		List<String> products = order.getProducts();
		if (products == null || products.size() == 0) {
			return shipmentIds;
		}
		int size = products.size();
		if (size == 1) {
			createShipmentRequest(order, orderId, products);
		} else {
			int part = size / 2;
			createShipmentRequest(order, orderId, products.subList(0, part));
			createShipmentRequest(order, orderId, products.subList(part, size));
		}

		shipmentIds.addAll(getShipmentIdsFromBlockchain(orderId));
		return shipmentIds;
	}

	private void createShipmentRequest(Order order, String orderId, List<String> products) {
		BlockChainSendShipmentRequest shipment = new BlockChainSendShipmentRequest();
		shipment.setCustomer(order.getOrderer());
		shipment.setDocumentHash("hashing");
		shipment.setDocumentLocation("hier");
		shipment.setDestination("Autoworld - Brussel");
		products.stream().forEach(prod -> shipment.getShippedProducts().add(prod));
		shipment.getTransporterList().add("TID2109");
		shipment.getTransporterList().add("TID4412");
		shipment.getTransporterList().add("POSTNL001");
		shipment.setWeight("10KG");
		shipment.setClientReference(orderId);

		sendToBlockchain(shipment);
	}

	private void sendToBlockchain(BlockChainSendShipmentRequest shipment) {
		HttpEntity<BlockChainSendShipmentRequest> requestEntity = new HttpEntity<>(shipment);
		ParameterizedTypeReference<BlockChainSendShipmentReturn> returnBean = new ParameterizedTypeReference<BlockChainSendShipmentReturn>() {
		};
		try {
			LOG.debug(objectMapper.writeValueAsString(shipment));
			restTemplate.exchange(BlockchainRestUrl.SEND_SHIPMENT.getUrl(), HttpMethod.POST, requestEntity, returnBean, "");
		} catch (Exception e) {
			LOG.error("Problem calling the blockchain", e);
			throw new RuntimeException(e);
		}
	}

	private Collection<? extends String> getShipmentIdsFromBlockchain(String orderId) {
		try {
			BlockChainSendShipmentQuery query = new BlockChainSendShipmentQuery();
			BlockChainWhere where = new BlockChainWhere();
			where.setClientReference(orderId);
			query.setWhere(where);

			// HttpEntity requestEntity = new HttpEntity();
			ParameterizedTypeReference<List<BlockChainShipment>> returnBean = new ParameterizedTypeReference<List<BlockChainShipment>>() {
			};
			LOG.debug(objectMapper.writeValueAsString(query));
			ResponseEntity<List<BlockChainShipment>> exchange = restTemplate.exchange(BlockchainRestUrl.QUERY_SHIPMENT.getUrl(), HttpMethod.GET,
					null, returnBean, objectMapper.writeValueAsString(query));
			List<BlockChainShipment> shipments = exchange.getBody();
			return shipments.stream().map(shipment -> shipment.getShipmentID()).collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Problem calling the blockchain", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> createShipments(Order order) {
		List<String> shipmentIds = new ArrayList<>();
		List<String> products = order.getProducts();
		if (products == null || products.size() == 0) {
			return shipmentIds;
		}
		int size = products.size();
		if (size == 1) {
			shipmentIds.add(createShipment(order, products));
		} else {
			int part = size / 2;
			shipmentIds.add(createShipment(order, products.subList(0, part)));
			shipmentIds.add(createShipment(order, products.subList(part, size)));
		}
		return shipmentIds;
	}

	private String createShipment(Order order, List<String> products) {
		BlockChainShipment shipment = new BlockChainShipment();
		String id = UUID.randomUUID().toString();
		shipment.setShipmentID(id);
		products.stream().forEach(product -> addProductToShipment(shipment, product));
		shipment.setDocumentHash(Hashing.sha256().hashBytes((id + shipment.getShippedProducts().toString()).getBytes()).toString());
		shipment.setDocumentLocation("doclocation");
		shipment.getTransporterList().add("resource:org.ebpi.blockathon.Transporter#TID2109");
		shipment.getTransporterList().add("resource:org.ebpi.blockathon.Transporter#TID4412");
		shipment.getTransporterList().add("resource:org.ebpi.blockathon.Transporter#POSTNL001");
		shipment.setDestination("autoworld - Brussel");
		shipment.setCustomer("resource:org.ebpi.blockathon.Orderer#" + order.getOrderer());
		shipment.setSupplier("resource:org.ebpi.blockathon.Manufacturer#" + order.getManufacturer());
		shipment.setWeight("10KG");

		HttpEntity<BlockChainShipment> requestEntity = new HttpEntity<>(shipment);
		ParameterizedTypeReference<BlockChainShipment> returnBean = new ParameterizedTypeReference<BlockChainShipment>() {
		};
		try {
			LOG.debug(objectMapper.writeValueAsString(shipment));
			restTemplate.exchange(BlockchainRestUrl.ADD_SHIPMENT.getUrl(), HttpMethod.POST, requestEntity, returnBean, "");
		} catch (Exception e) {
			LOG.error("Problem calling the blockchain", e);
			throw new RuntimeException(e);
		}
		return id;
	}

	private void addProductToShipment(BlockChainShipment shipment, String product) {
		shipment.getShippedProducts().add("resource:org.ebpi.blockathon.Product#" + product);
	}

}
