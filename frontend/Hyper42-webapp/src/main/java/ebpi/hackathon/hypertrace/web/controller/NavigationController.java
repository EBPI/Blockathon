package ebpi.hackathon.hypertrace.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class NavigationController {
    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        String message = "This text is inserted from within the code with Thymeleaf!";
        model.put("homeMessage", message);
        return "home";
    }

    @RequestMapping("/login")
    public String login(Map<String, Object> model) {
        String message1 = "This is just a boring login page...";
        String message2 = "Hyper42 is the best team ever!";
        model.put("loginMessage1", message1);
        model.put("loginMessage2", message2);
        return "login";
    }
}
