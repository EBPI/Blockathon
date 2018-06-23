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
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    private HyperledgerRestService ledgerService;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/order")
    public String placeOrder(@RequestParam("productId") String productId, @RequestParam("creator") String creator, @RequestParam("quantity") String quantity, HttpServletRequest request) {
        String ordererId = userUtils.getUserIdFromCookie(request);
        List<Order> orders = parseOrders(productId, creator, quantity, ordererId);
        System.out.println(new Gson().toJson(orders));
        return "home";
    }

    private List<Order> parseOrders(String productId, String manufacturer, String quantity, String ordererId) {
        List<Order> orderList = new ArrayList<>();
        String[] ids = productId.split(",");
        String[] manufacturers = manufacturer.split(",");
        String[] quantities = quantity.split(",");
        for (int i = 0; i < ids.length; i++) {
            if (quantities[i].equals("0")) {
                continue;
            }
            Order order = new Order();
            order.setOrderer(ordererId);
            order.setManufacturer(manufacturers[i]);
            List<String> products = new ArrayList<>();
            for (int j = 0; j < Integer.parseInt(quantities[i]); j++) {
                products.add(ids[i]);
            }
            order.setProducts(products);
            orderList.add(order);
        }
        return orderByManufacturer(orderList, new HashSet<>(Arrays.asList(manufacturers)));
    }

    private List<Order> orderByManufacturer(List<Order> orders, Set<String> manufacturers) {
        List<Order> ordersByManufacturer = new ArrayList<>();
        for (String manufacturer : manufacturers) {
            Order orderManufacturer = new Order();
            for (Order order : orders) {
                if (order.getManufacturer().equals(manufacturer)) {
                    orderManufacturer.setManufacturer(manufacturer);
                    orderManufacturer.setOrderer(order.getOrderer());
                    if (orderManufacturer.getProducts() == null) {
                        orderManufacturer.setProducts(new ArrayList<>());
                    }
                    orderManufacturer.getProducts().addAll(order.getProducts());
                }
            }
            ordersByManufacturer.add(orderManufacturer);
        }
        return ordersByManufacturer;
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
