package ebpi.hackathon.hypertrace.web.domein;

public class Handover {
    private String HandoverId;
    private TradePartner giving;
    private TradePartner receiver;
    private String confirmed;
    private String stateGiving;
    private String stateReceiver;
    private boolean finalHandover;

    public String getHandoverId() {
        return HandoverId;
    }

    public void setHandoverId(String handoverId) {
        HandoverId = handoverId;
    }

    public TradePartner getGiving() {
        return giving;
    }

    public void setGiving(TradePartner giving) {
        this.giving = giving;
    }

    public TradePartner getReceiver() {
        return receiver;
    }

    public void setReceiver(TradePartner receiver) {
        this.receiver = receiver;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getStateGiving() {
        return stateGiving;
    }

    public void setStateGiving(String stateGiving) {
        this.stateGiving = stateGiving;
    }

    public String getStateReceiver() {
        return stateReceiver;
    }

    public void setStateReceiver(String stateReceiver) {
        this.stateReceiver = stateReceiver;
    }

    public boolean isFinalHandover() {
        return finalHandover;
    }

    public void setFinalHandover(boolean finalHandover) {
        this.finalHandover = finalHandover;
    }
}
