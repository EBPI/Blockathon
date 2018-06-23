package ebpi.hackathon.hypertrace.web.controller;

import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.Order;
import ebpi.hackathon.hypertrace.web.rest.HyperledgerRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private HyperledgerRestService ledgerService;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/order")
    public String placeOrder(@RequestParam("productId") String productId, @RequestParam("manufacturer") String manufacturer, @RequestParam("quantity") String quantity, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        int quantityInt = Integer.parseInt(quantity);
        Order order = new Order();
        order.setManufacturer(manufacturer);
        order.setOrderer(userUtils.getUserIdFromCookie(request));
        List<String> products = new ArrayList<>();
        for (int i = 0; i < quantityInt; i++) {
            products.add(productId);
        }
        order.setProducts(products);
        System.out.println(new Gson().toJson(order));
        return "home";
    }

    /**
     * Check if user type and decide what kind of status is returned (order status or transport status)
     *
     * @param model models to insert into the thymeleaf template
     * @return status page for orders or transport
     */
    @RequestMapping("/status")
    public String status(Map<String, Object> model) {
        return orderStatus(model);
    }

    /**
     * Order status for customer
     *
     * @param model models to insert into the thymeleaf template
     * @return status page for orders
     */
    @RequestMapping("/statusOrder")
    public String orderStatus(Map<String, Object> model) {
        String orderStatusMessage = "{{order_status}}";
        model.put("orderStatusMessage", orderStatusMessage);
        return "statusOrder";
    }

    /**
     * Order status for company
     *
     * @param model models to insert into the thymeleaf template
     * @return status page for transport
     */
    @RequestMapping("/statusTransport")
    public String transportStatus(Map<String, Object> model) {
        String transportStatusMessage = "{{transport_status}}";
        model.put("transportStatusMessage", transportStatusMessage);
        return "statusTransport";
    }
}
