package ebpi.hackathon.hypertrace.web.domein;

import org.junit.Test;

public class ShipmentTest {
    @Test
    public void testShipment(){
        Shipment shpiment = new Shipment();
        shpiment.setShipmentId("aidee");
        shpiment.setGivingPartnerId("partneraidee");
        assert(shpiment.getShipmentId().equals("aidee"));
        assert(shpiment.getGivingPartnerId().equals("partneraidee"));
    }
}
