package ebpi.hackathon.hypertrace.web.domein;

public class AcceptHandover {
    private String shipment;
    private String previousHandler;
    private boolean finalHandover;


    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getPreviousHandler() {
        return previousHandler;
    }

    public void setPreviousHandler(String previousHandler) {
        this.previousHandler = previousHandler;
    }

    public boolean isFinalHandover() {
        return finalHandover;
    }

    public void setFinalHandover(boolean finalHandover) {
        this.finalHandover = finalHandover;
    }
}
