package app.taskmanagementsystem.configs;

import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.services.security.ApplicationUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum.ADMIN;
import static app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private static final String[] ANONYMOUS_ENDPOINTS = {
            "/",
            "/users/login",
            "/users/register",
            "/users/login-error",
            "/error",
            "/test-session"
    };
    private static final String[] ROLE_ADMIN_ENDPOINTS = {
            "/admin/**"
//            "/admin/all-users",
//            "/admin/user-details/**",
//            "/admin/user-edit/**",
//            "/admin/all-departments",
//            "/admin/department-details/**",
//            "/admin/user-delete/**"
    };

    private static final String[] ROLE_USER_ENDPOINTS = {
            "/users/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                .requestMatchers(ANONYMOUS_ENDPOINTS).permitAll()

                .requestMatchers(ROLE_ADMIN_ENDPOINTS).hasRole(ADMIN.name())

                .requestMatchers(ROLE_USER_ENDPOINTS).permitAll()

                .anyRequest()
                .authenticated()
                .and()

                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureForwardUrl("/users/login-error")


                .and()
                .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return httpSecurity
                .build();
    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new ApplicationUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
