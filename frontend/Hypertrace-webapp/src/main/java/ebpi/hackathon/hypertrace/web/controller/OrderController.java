package ebpi.hackathon.hypertrace.web.controller;

import ebpi.hackathon.hypertrace.web.domein.Order;
import ebpi.hackathon.hypertrace.web.rest.BackendService;
import ebpi.hackathon.hypertrace.web.rest.HyperledgerRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    private HyperledgerRestService ledgerService;

    @Autowired
    private BackendService backendService;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/order")
    public String placeOrder(@RequestParam("productId") String productId, @RequestParam("creator") String creator, @RequestParam("quantity") String quantity, HttpServletRequest request, Map<String, Object> model) {
        String ordererId = userUtils.getUserIdFromCookie(request);
        List<Order> orders = parseOrders(productId, creator, quantity, ordererId);
        String message = backendService.sendOrderToBackend(orders);
        model.put("loggedInMessage", message);
        return "ordererLoggedIn";
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
            boolean filledOrder = false;
            for (Order order : orders) {
                if (!order.getManufacturer().equals(manufacturer)) {
                    orderManufacturer.setManufacturer(manufacturer);
                    orderManufacturer.setOrderer(order.getOrderer());
                    if (orderManufacturer.getProducts() == null) {
                        orderManufacturer.setProducts(new ArrayList<>());
                    }
                    orderManufacturer.getProducts().addAll(order.getProducts());
                    filledOrder = true;
                }
            }
            if (filledOrder) {
                ordersByManufacturer.add(orderManufacturer);
            }
        }
        return ordersByManufacturer;
    }

    @RequestMapping("/handoverqr")
    public String getQr(@RequestParam("givingPartnerId") String partnerId, @RequestParam("shipmentId") String shipmentId, HttpServletRequest request, Map<String, Object> model) {
        String id = userUtils.getUserIdFromCookie(request);
        String url = backendService.getQr(id, shipmentId);
        System.out.println("url for receiving shipment from " + partnerId + ": " + url);
        model.put("url", url);
        return "handover";
    }

    @RequestMapping("/manufacturer")
    public String getQr(@RequestParam("receiver") String receiver, @RequestParam("shipment") String shipment, HttpServletRequest request, Map<String, Object> model) {
        String id = userUtils.getUserIdFromCookie(request);
        String url = backendService.getQr(id, shipmentId);
        System.out.println("url for receiving shipment from " + partnerId + ": " + url);
        model.put("url", url);
        return "handover";
    }
}