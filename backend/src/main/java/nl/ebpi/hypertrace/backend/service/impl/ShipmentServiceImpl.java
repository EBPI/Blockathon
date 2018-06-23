package nl.ebpi.hypertrace.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
		if (products != null) {
			shipmentIds.addAll(sendShipmentToBlockchain(new OrderInformation(order, orderId), products));
		}
		return shipmentIds;
	}

	private List<String> sendShipmentToBlockchain(OrderInformation orderInfo, List<String> products) {
		List<String> shipmentIds = new ArrayList<>();
		int size = products.size();
		if (size == 1) {
			createShipmentRequest(orderInfo, products);
		} else if (size > 1) {
			splitShipment(orderInfo, products);
		}
		shipmentIds.addAll(getShipmentIdsFromBlockchain(orderInfo.orderId));
		return shipmentIds;
	}

	private void splitShipment(OrderInformation orderInfo, List<String> products) {
		int size = products.size();
		int part = size / 2;
		createShipmentRequest(orderInfo, products.subList(0, part));
		createShipmentRequest(orderInfo, products.subList(part, size));
	}

	private void createShipmentRequest(OrderInformation orderInfo, List<String> products) {
		BlockChainSendShipmentRequest shipment = new BlockChainSendShipmentRequest();
		shipment.setCustomer(orderInfo.order.getOrderer());
		shipment.setDocumentHash("hashing");
		shipment.setDocumentLocation("hier");
		shipment.setDestination("Autoworld - Brussel");
		products.stream().forEach(prod -> shipment.getShippedProducts().add(prod));
		shipment.getTransporterList().add("TID2109");
		shipment.getTransporterList().add("TID4412");
		shipment.getTransporterList().add("POSTNL001");
		shipment.setWeight("10KG");
		shipment.setClientReference(orderInfo.orderId);

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
			BlockChainSendShipmentQuery query = createQuery(orderId);

			// HttpEntity requestEntity = new HttpEntity();
			ParameterizedTypeReference<List<BlockChainShipment>> returnBean = new ParameterizedTypeReference<List<BlockChainShipment>>() {
			};
			LOG.debug(objectMapper.writeValueAsString(query));
			ResponseEntity<List<BlockChainShipment>> exchange = restTemplate.exchange(BlockchainRestUrl.QUERY_SHIPMENT.getUrl(), HttpMethod.GET,
					null, returnBean, objectMapper.writeValueAsString(query));
			return exchange.getBody().stream().map(shipment -> shipment.getShipmentID()).collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Problem calling the blockchain", e);
			throw new RuntimeException(e);
		}
	}

	private BlockChainSendShipmentQuery createQuery(String orderId) {
		BlockChainSendShipmentQuery query = new BlockChainSendShipmentQuery();
		BlockChainWhere where = new BlockChainWhere();
		where.setClientReference(orderId);
		query.setWhere(where);
		return query;
	}

	private static class OrderInformation {
		public Order order;
		public String orderId;

		public OrderInformation(Order order, String orderId) {
			this.order = order;
			this.orderId = orderId;
		}
	}
}
