package nl.ebpi.hypertrace.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainHandoverArray;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainSendShipmentQuery;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainSendShipmentRequest;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainSendShipmentReturn;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainShipment;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainShipmentQueryResponse;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainWhere;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.generated.domain.Shipment;
import nl.ebpi.hypertrace.backend.generated.domain.TransporterResponse;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@PropertySource("classpath:backend.properties")
@Service
public class ShipmentServiceImpl implements ShipmentService {

	private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${transporter_1}")
	private String transporter1;
	@Value("${transporter_2}")
	private String transporter2;
	@Value("${transporter_3}")
	private String transporter3;

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
		shipment.getTransporterList().add(transporter1);
		shipment.getTransporterList().add(transporter2);
		shipment.getTransporterList().add(transporter3);
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
			ResponseEntity<List<BlockChainShipment>> exchange = restTemplate.exchange(BlockchainRestUrl.QUERY_SHIPMENT_ON_ORDERID.getUrl(), HttpMethod.GET,
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

	@Override
	public TransporterResponse findShipments(String transporterId) {
		String queryParam = "resource:org.ebpi.blockathon.Transporter#" + transporterId;
		LOG.debug("findShipments with queryParam {}", queryParam);

		String url = BlockchainRestUrl.QUERY_SHIPMENT_ON_TRANSPORTERID.getUrl().replace("{query}",
				queryParam);
		LOG.debug("findShipments with url {}", url);
		ParameterizedTypeReference<List<BlockChainShipmentQueryResponse>> returnBean = new ParameterizedTypeReference<List<BlockChainShipmentQueryResponse>>() {
		};

		ResponseEntity<List<BlockChainShipmentQueryResponse>> blockchainResponse = restTemplate.exchange(
				BlockchainRestUrl.QUERY_SHIPMENT_ON_TRANSPORTERID.getUrl(),
				HttpMethod.GET, null, returnBean, queryParam);

		LOG.debug("queryresponse length {}", blockchainResponse.getBody().size());

		TransporterResponse response = filterBlockchainResponse(blockchainResponse, transporterId);
		return response;
	}

	private TransporterResponse filterBlockchainResponse(ResponseEntity<List<BlockChainShipmentQueryResponse>> blockchainResponse, String transporterId) {
		List<BlockChainShipmentQueryResponse> list = blockchainResponse.getBody();
		List<Shipment> shipments = list.stream().filter(shipment -> isNotHandover(shipment, transporterId)).map(shipment -> toResponseShipment(shipment))
				.collect(Collectors.toList());

		TransporterResponse response = new TransporterResponse();
		response.setShipments(shipments);
		return response;
	}

	private Shipment toResponseShipment(BlockChainShipmentQueryResponse blockchainShipment) {
		Shipment shipment = new Shipment();
		if (blockchainShipment.getHandoverArray() == null || blockchainShipment.getHandoverArray().size() == 0) {
			shipment.setGivingPartnerId(blockchainShipment.getSupplier());
		} else {
			BlockChainHandoverArray blockChainHandover = blockchainShipment.getHandoverArray().get(blockchainShipment.getHandoverArray().size() - 1);
			blockChainHandover.getReciever();
			shipment.setGivingPartnerId(blockChainHandover.getReciever());
		}
		shipment.setShipmentId(blockchainShipment.getShipmentID());
		return shipment;
	}

	private boolean isNotHandover(BlockChainShipmentQueryResponse shipment, String transporterId) {
		List<BlockChainHandoverArray> handovers = shipment.getHandoverArray();
		LOG.debug("Handovers: {}", handovers.size());

		for (BlockChainHandoverArray handoverArray : handovers) {
			LOG.debug("handover giving / receiver: {} / {}", handoverArray.getGiving(), handoverArray.getReciever());

			if (handoverArray.getGiving().contains(transporterId) || handoverArray.getReciever().contains(transporterId)) {
				return false;
			}
		}

		return true;
	}
}
