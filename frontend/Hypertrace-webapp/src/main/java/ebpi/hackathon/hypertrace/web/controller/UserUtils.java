package ebpi.hackathon.hypertrace.web.controller;

import ebpi.hackathon.hypertrace.web.domein.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserUtils {
    /**
     * Remove all cookies from session
     *
     * @param request  used for getting all cookies in session
     * @param response used for setting all cookies ready for deletion in response
     */
    public void removeCookies(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookie.setValue("");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    /**
     * Construct User from cookie values
     *
     * @param request request needed to read cookies
     * @return User object
     */
    public User getUserFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        User loggedIn = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                loggedIn.setUsername(cookie.getValue());
            }
            if (cookie.getName().equals("fullName")) {
                loggedIn.setFullName(cookie.getValue());
            }
            if (cookie.getName().equals("type")) {
                loggedIn.setType(cookie.getValue());
            }
        }
        return loggedIn;
    }

    /**
     * Return user ID from cookie value
     *
     * @param request request needed to read cookies
     * @return UserId
     */
    public String getUserIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String id = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("id")) {
                id = cookie.getValue();
            }
        }
        if (id == null) {
            throw new RuntimeException("Session expired: userID unknown");
        }
        return id;
    }
}
