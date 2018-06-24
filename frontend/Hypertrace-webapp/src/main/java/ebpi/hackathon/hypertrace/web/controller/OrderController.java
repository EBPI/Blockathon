package ebpi.hackathon.hypertrace.web.controller;

import ebpi.hackathon.hypertrace.web.domein.AcceptHandover;
import ebpi.hackathon.hypertrace.web.domein.Handover;
import ebpi.hackathon.hypertrace.web.domein.Order;
import ebpi.hackathon.hypertrace.web.rest.BackendService;
import ebpi.hackathon.hypertrace.web.rest.HyperledgerRestService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    private HyperledgerRestService ledgerService;

    @Autowired
    private BackendService backendService;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/order")
    public String placeOrder(@RequestParam("productId") String productId, @RequestParam("creator") String creator, @RequestParam("quantity") String quantity,
                             HttpServletRequest request, Map<String, Object> model) {
        String ordererId = userUtils.getUserIdFromCookie(request);
        List<Order> orders = parseOrders(productId, creator, quantity, ordererId);
        String message = backendService.sendOrderToBackend(orders);
        model.put("homeMessage", message);
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
    public String getQr(@RequestParam("givingPartnerId") String partnerId, @RequestParam("shipmentId") String shipmentId, HttpServletRequest request,
                        Map<String, Object> model) {
        String id = userUtils.getUserIdFromCookie(request);
        String url = backendService.getQrForReceiver(id, shipmentId, partnerId);
        System.out.println("url for receiving shipment from " + partnerId + ": " + url);
        model.put("handoverMessage", "Please let the deliverer scan and accept the order.");
        model.put("url", url);
        return "handover";
    }

    @RequestMapping("/manufacturer")
    public String getQrSelect(@RequestParam("receiver") String receiver, @RequestParam("shipment") String shipment, @RequestParam("deliverer") String deliverer,
                              HttpServletRequest request, Map<String, Object> model) {
        model.put("receiverId", receiver);
        model.put("shipmentId", shipment);
        model.put("delivererId", deliverer);
        return "handoverSelectReceiver";
    }

    @RequestMapping("/manufacturerselect")
    public String acceptHandover(@RequestParam("receiver") String receiver, @RequestParam("shipment") String shipment, @RequestParam("deliverer") String deliverer, HttpServletRequest request,
                                 Map<String, Object> model) {
        Handover handover = new Handover();
        handover.setFinalHandover(false);
        handover.setNextHandler(receiver);
        handover.setShipment(shipment);

        ledgerService.startHandoverReceiver(handover);

        model.put("receiverId", receiver);
        model.put("shipmentId", shipment);
        model.put("delivererId", deliverer);
        return "handoverSelectDeliverer";
    }

    @RequestMapping("/handoverdonereceiver")
    public String handoverDoneReceiver(@RequestParam("receiver") String receiver, @RequestParam("shipment") String shipment, @RequestParam("deliverer") String deliverer, HttpServletRequest request,
                                       Map<String, Object> model) {
        AcceptHandover handover = new AcceptHandover();
        handover.setFinalHandover(false);
        handover.setPreviousHandler(deliverer);
        handover.setShipment(shipment);

        ledgerService.startHandoverDeliverer(handover);
        String url = backendService.getQrForDeliverer(shipment, deliverer);
        model.put("handoverMessage", "Please let the receiver scan and accept the order.");
        model.put("url", url);

        return "handover";
    }

    @RequestMapping("/reveiver")
    public String getQrSelectDeliverer(@RequestParam("shipment") String shipment, @RequestParam("deliverer") String deliverer,
                                       HttpServletRequest request, Map<String, Object> model) {
        model.put("shipmentId", shipment);
        model.put("delivererId", deliverer);
        return "handoverSelectDeliverer";
    }

    @RequestMapping("/handoverdoneDeliverer")
    public String handoverDoneDeliverer(@RequestParam("shipment") String shipment, @RequestParam("deliverer") String deliverer, HttpServletRequest request,
                                        Map<String, Object> model) {
        AcceptHandover handover = new AcceptHandover();
        handover.setFinalHandover(false);
        handover.setPreviousHandler(deliverer);
        handover.setShipment(shipment);

        ledgerService.startHandoverDeliverer(handover);

        model.put("homeMessage", "Handover completed!");
        return "home";
    }
}