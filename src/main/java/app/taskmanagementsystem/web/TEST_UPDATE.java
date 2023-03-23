package app.taskmanagementsystem.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Enumeration;

@Controller
public class TEST_UPDATE {

    @GetMapping("/invalidate")
    public String getInvalidate(HttpServletRequest httpServletRequest) throws ServletException {

        httpServletRequest.logout();
//        SecurityContextHolder.getContext().setAuthentication(null);
//        SecurityContextHolder.clearContext();
//
//        HttpSession httpSession = httpServletRequest.getSession();
//        Enumeration<String> e = httpSession.getAttributeNames();
//        httpSession.invalidate();
//
//        while (e.hasMoreElements()) {
//            String attr = e.nextElement();
//            httpSession.setAttribute(attr, null);
//        }
//        removeCookies(httpServletRequest);

        return "redirect:/";
    }

    private static void removeCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Arrays.stream(cookies)
                    .forEach(cookie -> cookie.setMaxAge(0));
        }
    }
}
