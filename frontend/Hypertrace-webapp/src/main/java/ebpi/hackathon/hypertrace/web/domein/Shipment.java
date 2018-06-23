package ebpi.hackathon.hypertrace.web.domein;

public class Shipment {
    private String ShipmentId;
    private String GivingPartnerId;

    public String getShipmentId() {
        return ShipmentId;
    }

    public void setShipmentId(String shipmentId) {
        ShipmentId = shipmentId;
    }

    public String getGivingPartnerId() {
        return GivingPartnerId;
    }

    public void setGivingPartnerId(String givingPartnerId) {
        GivingPartnerId = givingPartnerId;
    }
}
