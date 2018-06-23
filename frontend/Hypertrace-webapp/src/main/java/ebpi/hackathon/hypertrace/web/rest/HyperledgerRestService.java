package ebpi.hackathon.hypertrace.web.rest;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.Participant;
import ebpi.hackathon.hypertrace.web.domein.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
     * Get participantId from Hyperledger by type and name
     *
     * @param user user needed to gather ID from the ledger
     * @return The ID of the user on the ledger
     * @throws URISyntaxException syntax exception that occurs when parcing URI's, nothing of interest here
     */
    public String getParticipant(User user) throws URISyntaxException {
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

        // Not yet implemented in REST interface from Hyperledger. This is for PoC purposes only and NOT for production!
        String participantJson = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(participantJson);
        Type listType = new TypeToken<ArrayList<Participant>>() {
        }.getType();
        List<Participant> participants = new Gson().fromJson(participantJson, listType);
        if (participants != null) {
            for (Participant participant : participants) {
                if (participant.getName().equals(user.getUsername())) {
                    return participant.getPartnerId();
                }
            }
        }
        return null;
    }
}
