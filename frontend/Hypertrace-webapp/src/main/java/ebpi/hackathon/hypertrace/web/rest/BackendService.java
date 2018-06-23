package ebpi.hackathon.hypertrace.web.rest;

import com.google.gson.Gson;
import com.sun.jndi.toolkit.url.Uri;
import ebpi.hackathon.hypertrace.web.domein.Order;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class BackendService {
    private static final String BACKEND_SERVICE_IP = "http://192.168.0.101:8070";
    private static final URI BACKEND_SERVICE_ORDER = URI.create(BACKEND_SERVICE_IP + "/order");

    @Autowired
    private RestTemplate restTemplate;

    public String sendOrderToBackend(List<Order> orders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        boolean error = false;

        for (Order order : orders) {
            String orderJson = new Gson().toJson(order);
            System.out.println(orderJson);
            HttpEntity<String> entity = new HttpEntity<>(orderJson, headers);
            ResponseEntity<String> orderResponse = restTemplate.exchange(BACKEND_SERVICE_ORDER, HttpMethod.POST, entity, String.class);
            if (!orderResponse.getStatusCode().is2xxSuccessful()) {
                error = true;
            }
        }

        if (error) {
            return "There was a problem sending your order.";
        } else {
            return "Order(s) placed successfully!";
        }

    }
}