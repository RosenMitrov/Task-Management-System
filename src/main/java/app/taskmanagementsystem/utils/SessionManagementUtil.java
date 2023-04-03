package app.taskmanagementsystem.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Enumeration;

public class SessionManagementUtil {

    public static void logoutUser(HttpServletRequest httpServletRequest) throws ServletException {

        httpServletRequest.logout();

        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();

        HttpSession httpSession = httpServletRequest.getSession();
        Enumeration<String> attributeNames = httpSession.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attr = attributeNames.nextElement();
            httpSession.setAttribute(attr, null);
        }

        removeCookies(httpServletRequest);
        httpSession.invalidate();
    }

    private static void removeCookies(HttpServletRequest httpServletRequest) {
        Cookie[] allCookies = httpServletRequest.getCookies();
        if (allCookies != null && allCookies.length > 0) {
            for (Cookie cookie : allCookies) {
                cookie.setMaxAge(0);
            }
        }
    }
}
