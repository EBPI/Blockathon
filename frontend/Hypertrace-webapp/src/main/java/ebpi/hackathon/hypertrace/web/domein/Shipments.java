package ebpi.hackathon.hypertrace.web.domein;

import java.util.ArrayList;
import java.util.List;

public class Shipments {
    private List<Shipment> Shipments = new ArrayList<Shipment>();

    public List<Shipment> getShipments() {
        return Shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.Shipments = shipments;
    }
}
