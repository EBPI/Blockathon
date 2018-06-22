package ebpi.hackathon.hypertrace.web.controller;

import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    /**
     * Homepage Submits a loggedIn user
     * @param user
     * @return logged in home or same page
     */
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute(value="user") User user, @RequestParam("type") String type, Map<String, Object> model, HttpServletResponse response) {
        // clear cookies
        user.setType(type);
        System.out.println(new Gson().toJson(user));

        response.addCookie(new Cookie("username", user.getUsername()));
        response.addCookie(new Cookie("type", user.getType()));


        return loggedIn(model, user);
    }

    /**
     * Homepage for logged-in user
     * @param model
     * @return homepage logged-in user
     */
    @RequestMapping("/loggedIn")
    public String loggedIn(Map<String, Object> model, User user) {
        model.put("loggedInMessage", user.getType() + " " + user.getUsername() + "!");
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
