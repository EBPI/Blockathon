package ebpi.hackathon.hypertrace.web.rest;

import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.Order;
import ebpi.hackathon.hypertrace.web.domein.Shipments;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BackendService {
	private static final String BACKEND_SERVICE_IP = "http://192.168.0.101:8070";
	private static final URI BACKEND_SERVICE_ORDER = URI.create(BACKEND_SERVICE_IP + "/order");
	private static final URI BACKEND_SERVICE_SHIPMENT = URI.create(BACKEND_SERVICE_IP + "/shipment");
	private static final URI BACKEND_SERVICE_QR_RECEIVER = URI.create(BACKEND_SERVICE_IP + "/receiver/qrcode");
	private static final URI BACKEND_SERVICE_QR_DELIVERER = URI.create(BACKEND_SERVICE_IP + "/deliverer/qrcode");

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Send a list of orders to the backend in order to create shipments
	 *
	 * @param orders the orders per manufacturer
	 * @return Message success of failure
	 */
	public String sendOrderToBackend(List<Order> orders) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		boolean error = false;

		for (Order order : orders) {
			String orderJson = new Gson().toJson(order);
			System.out.println(orderJson);
			HttpEntity<String> entity = new HttpEntity<>(orderJson, headers);
			ResponseEntity<String> orderResponse = restTemplate.exchange(BACKEND_SERVICE_ORDER, HttpMethod.POST, entity, String.class);
			if (!orderResponse.getStatusCode().is2xxSuccessful()) {
				error = true;
			}
		}

		if (error) {
			return "There was a problem sending your order.";
		} else {
			return "Order(s) placed successfully!";
		}
	}

	/**
	 * Get shipments from the backend for a specific transporter ID
	 *
	 * @param transporterId The id of the logged in transporter
	 * @return List of shipments for the given transporter ID
	 */
	public Shipments getShipmentsForTransporter(String transporterId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		String uri = UriComponentsBuilder.fromHttpUrl(BACKEND_SERVICE_SHIPMENT.toString())
				.queryParam("transporterId", transporterId).toUriString();
		System.out.println(uri);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> shipmentResponse = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		System.out.println(shipmentResponse.getBody());
		return new Gson().fromJson(shipmentResponse.getBody(), Shipments.class);
	}

	/**
	 * Get qr code uri for receiver
	 *
	 * @param receiver receiver of order
	 * @param deliverer deliverer of order
	 * @param shipment shipment to be validated
	 * @return uri for backend call
	 */
	public String getQrForReceiver(String receiver, String shipment, String deliverer) {
		String uri = UriComponentsBuilder.fromHttpUrl(BACKEND_SERVICE_QR_RECEIVER.toString())
				.queryParam("receiver", receiver)
				.queryParam("shipment", shipment)
				.queryParam("deliverer", deliverer)
				.toUriString();
		System.out.println(uri);
		return uri;
	}

	/**
	 * Get qr code uri for deliverer
	 *
	 * @param deliverer deliverer of order
	 * @param shipment shipment to be validated
	 * @return uri for backend call
	 */
	public String getQrForDeliverer(String shipment, String deliverer) {
		String uri = UriComponentsBuilder.fromHttpUrl(BACKEND_SERVICE_QR_DELIVERER.toString())
				.queryParam("shipment", shipment)
				.queryParam("deliverer", deliverer)
				.toUriString();
		System.out.println(uri);
		return uri;
	}
}