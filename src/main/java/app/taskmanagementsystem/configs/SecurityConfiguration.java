package app.taskmanagementsystem.configs;

import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.services.CredentialService;
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


    private static final String[] ANONYMOUS_FOLDERS = {
            "/error"
    };
    private static final String[] ANONYMOUS_AND_AUTHENTICATED_ENDPOINTS = {
            "/"
    };
    private static final String[] ANONYMOUS_ENDPOINTS = {
            "/",
            "/users/login",
            "/users/register",
            "/users/login-error"
    };
    private static final String[] ROLE_ADMIN_ENDPOINTS = {
            "/admin/all-users",
            "/admin/roles",
            "/admin/user-details/**",
            "/admin/user-edit/**",
            "/admin/user-edit-role/**",
            "/admin/user-delete/**",
            "/admin/all-departments",
            "/admin/department-details/**"
    };

    private static final String[] ROLE_USER_ENDPOINTS = {
            "/users/home",
            "/users/profile",
            "/users/tasks/all",
            "/users/tasks/add-task",
            "/users/posts/related-to-task-id/**",
            "/users/posts/create-new-post",
            "/users/posts/create-new-post-to-task/**",
            "/users/comments/to-post/**",
            "/users/change-password",
            "/users/tasks/assign-logged-user-to-task-by-task-id/**",
            "/users/tasks/detach-logged-user-from-task-by-task-id/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                .requestMatchers(ANONYMOUS_FOLDERS).permitAll()
                .requestMatchers(ANONYMOUS_AND_AUTHENTICATED_ENDPOINTS).permitAll()
                .requestMatchers(ANONYMOUS_ENDPOINTS).anonymous()

                .requestMatchers(ROLE_ADMIN_ENDPOINTS).hasRole(ADMIN.name())

                .requestMatchers(ROLE_USER_ENDPOINTS).hasRole(USER.name())


                .anyRequest()
                .authenticated()
                .and()

                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .failureForwardUrl("/users/login-error")

                .and()
                .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")

                .and()
                .rememberMe()
                .key("uniqueKeyHasToBePlacedHere123456")
                .tokenValiditySeconds(5 * 24 * 60 * 60);

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
