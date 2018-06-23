package ebpi.hackathon.hypertrace.web.domein;

import java.util.ArrayList;
import java.util.List;

public class Shipments {
    private List<Shipment> shipments = new ArrayList<Shipment>();

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }
}
