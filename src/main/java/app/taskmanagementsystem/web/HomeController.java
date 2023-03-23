package app.taskmanagementsystem.web;

import app.taskmanagementsystem.security.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(@AuthenticationPrincipal
                       AppUserDetails appUserDetails,
                       Model model) {
        if (appUserDetails != null) {
            model.addAttribute("username", appUserDetails.getFullName());
        }
        return "index";
    }
}
