package ebpi.hackathon.hypertrace.web.domein;

import org.junit.Test;

public class ParticipantTest {
    @Test
    public void testGettersAndSetters(){
        Participant participant = new Participant();
        participant.setName("Rogier");
        participant.setPartnerId("Amanda");
        assert (participant.getPartnerId().equals("Amanda"));
        assert (participant.getName().equals("Rogier"));


    }
}
