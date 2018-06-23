package ebpi.hackathon.hypertrace.web.domein;


import org.junit.Test;

public class HandoverTest {

    @Test
    public void testGettersAndSetters() {
        Handover handover = new Handover();
        handover.setHandoverId("uniek");
        handover.setConfirmed("ja");
        handover.setFinalHandover(true);
        TradePartner gever = new TradePartner();
        gever.setPartnerId("435");
        TradePartner nemer = new TradePartner();
        nemer.setPartnerId("777");
        handover.setGiving(gever);
        handover.setReceiver(nemer);
        handover.setStateGiving("prachtigMooi");
        handover.setStateReceiver("kapot");
        assert (handover.getConfirmed().equals("ja"));
        assert (handover.getGiving().getPartnerId().equals("435"));
        assert (handover.getReceiver().getPartnerId().equals("777"));
        assert (handover.getStateGiving().equals("prachtigMooi"));
        assert (handover.getStateReceiver().equals("kapot"));
        assert (handover.getHandoverId().equals("uniek"));
    }
}
