package ebpi.hackathon.hypertrace.web.rest;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.Participant;
import ebpi.hackathon.hypertrace.web.domein.Product;
import ebpi.hackathon.hypertrace.web.domein.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HyperledgerRestService {

    private static final String HYPERLEDGER_REST_IP = "http://192.168.0.103";
    private static final String HYPERLEDGER_REST_ADMIN = HYPERLEDGER_REST_IP + ":3000/api/org.ebpi.blockathon.%s";
    private static final String HYPERLEDGER_REST_MANUFACTURER = HYPERLEDGER_REST_IP + ":3001/api/org.ebpi.blockathon.%s";
    private static final String HYPERLEDGER_REST_TRANSPORTER = HYPERLEDGER_REST_IP + ":3002/api/org.ebpi.blockathon.%s";
    private static final String HYPERLEDGER_REST_ORDERER = HYPERLEDGER_REST_IP + ":3003/api/org.ebpi.blockathon.%s";
    private static final String HYPERLEDGER_REST_CUSTOMS = HYPERLEDGER_REST_IP + ":3004/api/org.ebpi.blockathon.%s";
    private static final String ORDERER_PARAM = "Orderer";
    private static final String MANUFACTURER_PARAM = "Manufacturer";
    private static final String TRANSPORTER_PARAM = "Transporter";
    private static final String CUSTOMS_PARAM = "Douane";
    private static final String PRODUCT_PARAM = "Product";

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
                url = new URI(String.format(HYPERLEDGER_REST_ORDERER, ORDERER_PARAM));
                break;
            case "manufacturer":
                url = new URI(String.format(HYPERLEDGER_REST_MANUFACTURER, MANUFACTURER_PARAM));
                break;
            case "transporter":
                url = new URI(String.format(HYPERLEDGER_REST_TRANSPORTER, TRANSPORTER_PARAM));
                break;
            case "customs":
                url = new URI(String.format(HYPERLEDGER_REST_CUSTOMS, CUSTOMS_PARAM));
                break;
            default:
                throw new RuntimeException("Participant unknown");
        }

        String participantJson = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(participantJson);
        Type listType = new TypeToken<ArrayList<Participant>>() {
        }.getType();
        List<Participant> participants = new Gson().fromJson(participantJson, listType);
        // Filter not yet implemented in REST interface from Hyperledger. This is for PoC purposes only and NOT for production!
        if (participants != null) {
            for (Participant participant : participants) {
                if (participant.getName().equals(user.getUsername())) {
                    return participant.getPartnerId();
                }
            }
        }
        return null;
    }

    /**
     * Get products available from the ledger
     *
     * @return list of products
     * @throws URISyntaxException syntax exception that occurs when parcing URI's, nothing of interest here
     */
    public List<Product> getProducts() {
        try {
            URI url;
            url = new URI(String.format(HYPERLEDGER_REST_ORDERER, PRODUCT_PARAM));
            String productJson = restTemplate.getForEntity(url, String.class).getBody();
            System.out.println(productJson);
            Type listType = new TypeToken<ArrayList<Product>>() {
            }.getType();
            return new Gson().fromJson(productJson, listType);
        }
        catch (Exception e) {
            System.out.println("Error gathering products: " + e);
            return Collections.emptyList();
        }
    }
}