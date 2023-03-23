package app.taskmanagementsystem.configs;

import app.taskmanagementsystem.services.CredentialService;
import app.taskmanagementsystem.web.interceptor.ExpiredCredentialsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorLoginConfig implements WebMvcConfigurer {
    private final CredentialService credentialService;

    @Autowired
    public InterceptorLoginConfig(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ExpiredCredentialsInterceptor(credentialService)).addPathPatterns("/home");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
