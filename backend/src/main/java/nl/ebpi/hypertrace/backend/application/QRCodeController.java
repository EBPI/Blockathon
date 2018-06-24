package nl.ebpi.hypertrace.backend.application;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import nl.ebpi.hypertrace.backend.utils.FrontEndUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QRCodeController {

	private static final Logger LOG = LoggerFactory.getLogger(QRCodeController.class);

	@RequestMapping(value = "/receiver/qrcode", consumes = MediaType.ALL_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] createQRForReceiver(@RequestParam String receiver, @RequestParam String shipment, @RequestParam String deliverer) {
		LOG.debug("creating qr for receiver {} - {}", receiver, shipment);

		String url = FrontEndUrl.RECEIVER_QRCODE.getUrl().replace("{receiver}", receiver).replace("{shipment}", shipment).replace("{deliverer}",
				deliverer.substring(deliverer.lastIndexOf("#") + 1));

		return QRCode.from(url).to(ImageType.JPG).stream().toByteArray();
	}

	@RequestMapping(value = "/deliverer/qrcode", consumes = MediaType.ALL_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] createQRForDeliverer(@RequestParam String deliverer, @RequestParam String shipment) {
		LOG.debug("creating qr for deliverer {} - {}", deliverer, shipment);

		String url = FrontEndUrl.DELIVERER_QRCODE.getUrl().replace("{deliverer}", deliverer).replace("{shipment}",
				shipment);
		return QRCode.from(url).to(ImageType.JPG).stream().toByteArray();
	}
}