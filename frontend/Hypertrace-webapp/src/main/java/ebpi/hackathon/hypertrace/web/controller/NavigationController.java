package ebpi.hackathon.hypertrace.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class NavigationController {
    /**
     * Homepage for non logged-in user
     * @param model
     * @return homepage non logged-in user
     */
    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        String message = "This text is inserted from within the code with Thymeleaf!";
        model.put("homeMessage", message);
        return "home";
    }

    /**
     * Homepage Page where users can sign in
     * @param model
     * @return loginpage
     */
    @RequestMapping("/login")
    public String login(Map<String, Object> model) {
        String message1 = "This is just a boring login page...";
        String message2 = "Hyper42 is the best team ever!";
        model.put("loginMessage1", message1);
        model.put("loginMessage2", message2);
        return "login";
    }

    /**
     * Homepage for logged-in user
     * @param model
     * @return homepage logged-in user
     */
    @RequestMapping("/loggedIn")
    public String loggedIn(Map<String, Object> model) {
        String loggedInMessage = "{{username}}";
        model.put("loggedInMessage", loggedInMessage);
        return "homeLoggedIn";
    }

    /**
     * Check if user type and decide what kind of status is returned (order status or transport status)
     * @param model
     * @return status page for orders or transport
     */
    @RequestMapping("/status")
    public String status(Map<String, Object> model) {
        return orderStatus(model);
    }

    /**
     * Order status for customer
     * @param model
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
     * @param model
     * @return status page for transport
     */
    @RequestMapping("/statusTransport")
    public String transportStatus(Map<String, Object> model) {
        String transportStatusMessage = "{{transport_status}}";
        model.put("transportStatusMessage", transportStatusMessage);
        return "statusTransport";
    }

    /**
     * Logout page
     * @param model
     * @return homepage
     */
    @RequestMapping("/logout")
    public String logout(Map<String, Object> model) {
        return home(model);
    }
}
