package nl.ebpi.hypertrace.backend.application;

import nl.ebpi.hypertrace.backend.generated.domain.TransporterResponse;
import nl.ebpi.hypertrace.backend.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShipmentController {

	private static final Logger LOG = LoggerFactory.getLogger(ShipmentController.class);

	@Autowired
	private ShipmentService shipmentService;

	@RequestMapping(value = "/shipment", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TransporterResponse findShipments(@RequestParam String transporterId) {
		LOG.debug("findShipments {}", transporterId);

		TransporterResponse transporterResponse = shipmentService.findShipments(transporterId);
		return transporterResponse;
	}
}