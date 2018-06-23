package ebpi.hackathon.hypertrace.web.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ebpi.hackathon.hypertrace.web.domein.Product;
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
public class LoginController {

    @Autowired
    private HyperledgerRestService ledgerService;

    @Autowired
    private UserUtils userUtils;

    /**
     * Homepage for non logged-in user
     *
     * @param model   models to insert into the thymeleaf template
     * @param request HttpServletRequest used for getting cookie values
     * @return homepage non logged-in user
     */
    @RequestMapping("/")
    public String home(Map<String, Object> model, HttpServletRequest request) {
        if (request.getCookies() == null || request.getCookies().length < 4) {
            String message = "Welcome to the Blockathon Hypertrace web application! You are currently not signed in.";
            model.put("homeMessage", message);
            return "home";
        } else {
            User loggedIn = userUtils.getUserFromCookie(request);
            return loggedIn(model, loggedIn);
        }
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
     * @param user     username and password from user
     * @param type     role selection from login
     * @param model    redirect model for thymeleaf
     * @param request  HttpServletRequest used for getting cookie values
     * @param response HttpServletResponse used for setting cookie values
     * @return logged in home or same page
     */
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute(value = "user") User user, @RequestParam("type") String type, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        // clear cookies
        user = loginCheck(user);

        try {
            if (user != null) {
                user.setType(type);
                String id = ledgerService.getParticipant(user);
                System.out.println("Found ID in ledger: " + id);
                if (id != null) {
                    response.addCookie(new Cookie("id", id));
                    response.addCookie(new Cookie("username", user.getUsername()));
                    response.addCookie(new Cookie("fullName", user.getFullName()));
                    response.addCookie(new Cookie("type", user.getType()));
                    return loggedIn(model, user);
                }
            }
            model.put("loginError", "Something went wrong during sign in, did you use the correct credentials?");
            return "login";
        } catch (Exception e) {
            model.put("loginError", "Network error. Please contact an admin.");
            return "login";
        }
    }

    /**
     * Check usersfile for correct login (local solution for PoC, NOT FOR PRODUCTION!)
     *
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
                return existing;
            }
        }
        return null;
    }

    /**
     * Homepage for logged-in user
     *
     * @param model models to insert into the thymeleaf template
     * @param user  User object for logged in user.
     * @return homepage logged-in user
     */
    @RequestMapping("/loggedIn")
    public String loggedIn(Map<String, Object> model, User user) {
        model.put("loggedInMessage", user.getType() + " " + user.getUsername() + " (" + user.getFullName() + ")!");
        switch (user.getType()) {
            case "orderer":
                List<Product> products = ledgerService.getProducts();
                model.put("products", products);
                return "ordererLoggedIn";
            case "manufacturer":
                return "manufacturerLoggedIn";
            case "transporter":
                return "transporterLoggedIn";
            case "customs":
                return "customsLoggedIn";
            default:
                throw new RuntimeException("Participant unknown");
        }
    }

    /**
     * Logout page
     *
     * @param model    models to insert into the thymeleaf template
     * @param request  HttpServletRequest used for getting cookie values
     * @param response HttpServletResponse used for setting cookie values
     * @return homepage without cookies
     */
    @RequestMapping("/logout")
    public String logout(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        userUtils.removeCookies(request, response);
        String message = "You have succesfully logged out.";
        model.put("homeMessage", message);
        return "home";
    }
}
