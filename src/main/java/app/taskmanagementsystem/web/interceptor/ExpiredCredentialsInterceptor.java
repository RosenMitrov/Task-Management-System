package app.taskmanagementsystem.web.interceptor;

import app.taskmanagementsystem.services.CredentialService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.Principal;

@Component
public class ExpiredCredentialsInterceptor implements HandlerInterceptor {

    private final CredentialService credentialService;

    public ExpiredCredentialsInterceptor(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {
            boolean isExpired = credentialService.isCredentialsExpired(userPrincipal.getName());
            if (isExpired) {
                response.sendRedirect("/users/change-password");
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }



}
