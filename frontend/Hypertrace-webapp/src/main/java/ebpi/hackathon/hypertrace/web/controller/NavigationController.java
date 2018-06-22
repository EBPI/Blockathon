package ebpi.hackathon.hypertrace.web.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.User;
import ebpi.hackathon.hypertrace.web.rest.HyperledgerRestService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.codec.Charsets.UTF_8;

@Controller
public class NavigationController {

    @Autowired
    HyperledgerRestService ledgerService;

    /**
     * Homepage for non logged-in user
     *
     * @param model models to insert into the thymeleaf template
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
     *
     * @param model Model that thymeleaf will connect to a User object
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
     *
     * @param user username and password from user
     * @param type role selection from login
     * @param model redirect model for thymeleaf
     * @param response HttpServletResponse used for setting cookie values
     * @return logged in home or same page
     */
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute(value = "user") User user, @RequestParam("type") String type, Map<String, Object> model, HttpServletResponse response) throws IOException, URISyntaxException {
        // clear cookies
        user = loginCheck(user);

        if (user != null) {
            user.setType(type);
            ledgerService.getParticipant(user);
            response.addCookie(new Cookie("username", user.getUsername()));
            response.addCookie(new Cookie("fullName", user.getFullName()));
            response.addCookie(new Cookie("type", user.getType()));
            return loggedIn(model, user);
        } else {
            return home(model);
        }
    }

    /**
     * Check usersfile for correct login (local solution for PoC, NOT FOR PRODUCTION!)
     * @param user Usercredentials from login
     * @return boolean that indicates a correct login
     * @throws IOException exception that can normally occur during json parsing, nothing of interest here
     */
    private User loginCheck(User user) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/users.json");
        String usersJson = IOUtils.toString(inputStream, UTF_8);
        Type listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        List<User> users = new Gson().fromJson(usersJson, listType);
        for (User existing : users) {
            if (existing.getFullName() != null && existing.getUsername().equals(user.getUsername()) && (existing.getPassword().equals(user.getPassword()))) {
                return user;
            }
        }
        return null;
    }

    /**
     * Homepage for logged-in user
     *
     * @param model models to insert into the thymeleaf template
     * @return homepage logged-in user
     */
    @RequestMapping("/loggedIn")
    public String loggedIn(Map<String, Object> model, User user) {
        model.put("loggedInMessage", user.getType() + " " + user.getUsername() + "!");
        return "homeLoggedIn";
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
     *
     * @param model models to insert into the thymeleaf template
     * @return homepage without cookies
     */
    @RequestMapping("/logout")
    public String logout(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        removeCookies(request, response);
        return home(model);
    }

    /**
     * Remove all cookies from session
     * @param request used for getting all cookies in session
     * @param response used for setting all cookies ready for deletion in response
     */
    private void removeCookies(HttpServletRequest request, HttpServletResponse response) {
        for(Cookie cookie : request.getCookies()) {
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
