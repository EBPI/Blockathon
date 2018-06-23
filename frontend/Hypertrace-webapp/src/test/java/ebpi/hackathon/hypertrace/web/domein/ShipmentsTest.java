package ebpi.hackathon.hypertrace.web.domein;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class ShipmentsTest {

	@Test
	public void testGson() {

		String jsonString = "{\"Shipments\":[{\"ShipmentId\":\"0150f5c2-dcf8-4078-996c-6853f0cda8fa\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"064b88c0-c579-44db-8ad6-b86c6da1a5fd\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"08a663cf-2483-42fb-a8b9-9fad714e4caa\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"0f969c21-c6ee-4651-9542-bc6db5afe14b\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"1ec39be9-e8b0-46de-9a0c-3296e0d50005\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"2399462b-ab99-4955-9fe5-d87fd41a1ed5\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"31d1bbd9-040a-4f1c-bee5-5a56f7b3ca3c\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"3f43d72f-d2d5-4369-ba32-5933f05a73cd\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"4d057a15-f517-4eb4-887e-e4dd3cbde1b6\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"a204fa53-16bd-4fcb-88f3-d94bb571e93c\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"ce1d0ace-da88-44a8-bc28-1a2ad0b966ad\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"e324676a-2d60-473f-b767-6eb89391fd84\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"e64cf4b9-cced-4e36-9540-2fea31cc6999\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"e7d7861a-333a-4bad-9250-76df5141a08c\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"ebc5c8a8-13e4-4fe9-9b06-851d78810e44\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"},{\"ShipmentId\":\"f65388bc-a29c-4ebd-b05d-c575ab5fb472\",\"GivingPartnerId\":\"resource:org.ebpi.blockathon.Manufacturer#MID0015\"}]}\n"
				+
				"";
		Shipments shipments2 = new Gson().fromJson(jsonString, Shipments.class);
		Assert.assertEquals(16, shipments2.getShipments().size());
	}

}
