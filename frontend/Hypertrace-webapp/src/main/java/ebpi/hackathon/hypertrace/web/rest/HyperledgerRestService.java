package ebpi.hackathon.hypertrace.web.rest;

import ebpi.hackathon.hypertrace.web.domein.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HyperledgerRestService {

    private static final String HYPERLEDGER_REST = "http://192.168.0.103:3001/api/org.ebpi.blockathon.%s";
    private static final String ORDERER_PARAM = "Orderer";
    private static final String MANUFACTURER_PARAM = "Manufacturer";
    private static final String TRANSPORTER_PARAM = "Transporter";
    private static final String CUSTOMS_PARAM = "Douane";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get participantId from Hyperledger
     * @param user user to get ID
     * @throws URISyntaxException syntax exception that occurs when parcing URI's, nothing of interest here
     */
    public void getParticipant(User user) throws URISyntaxException {
        URI url;
        switch (user.getType()) {
            case "orderer":
                url = new URI(String.format(HYPERLEDGER_REST, ORDERER_PARAM));
                break;
            case "manufacturer":
                url = new URI(String.format(HYPERLEDGER_REST, MANUFACTURER_PARAM));
                break;
            case "transporter":
                url = new URI(String.format(HYPERLEDGER_REST, TRANSPORTER_PARAM));
                break;
            case "customs":
                url = new URI(String.format(HYPERLEDGER_REST, CUSTOMS_PARAM));
                break;
            default:
                throw new RuntimeException("Participant unknown");
        }
        System.out.println(restTemplate.getForEntity(url, String.class));
    }
}
