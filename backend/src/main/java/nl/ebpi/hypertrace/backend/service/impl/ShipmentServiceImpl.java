package nl.ebpi.hypertrace.backend.service.impl;

import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.ebpi.hypertrace.backend.generated.blockchain.BlockChainShipment;
import nl.ebpi.hypertrace.backend.generated.domain.Order;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import nl.ebpi.hypertrace.backend.utils.BlockchainRestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShipmentServiceImpl implements ShipmentService {

	private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	public ShipmentServiceImpl() {
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
		HttpEntity<BlockChainShipment> requestEntity = new HttpEntity<>(shipment);
		ParameterizedTypeReference<BlockChainShipment> returnBean = new ParameterizedTypeReference<BlockChainShipment>() {
		};
		restTemplate.exchange(BlockchainRestUrl.ADD_SHIPMENT.getUrl(), HttpMethod.POST, requestEntity, returnBean, "");
		return id;
	}

	private void addProductToShipment(BlockChainShipment shipment, String product) {
		shipment.getShippedProducts().add("resource:org.ebpi.blockathon.Product#" + product);
	}

}
