package ebpi.hackathon.hypertrace.web.domein;

public class Handover {
    private String shipment;
    private String nextHandler;
    private boolean finalHandover;


    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(String nextHandler) {
        this.nextHandler = nextHandler;
    }

    public boolean isFinalHandover() {
        return finalHandover;
    }

    public void setFinalHandover(boolean finalHandover) {
        this.finalHandover = finalHandover;
    }
}
