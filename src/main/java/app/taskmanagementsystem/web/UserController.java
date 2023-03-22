package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.security.AppUserDetails;
import app.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal
                             AppUserDetails appUserDetails,
                             Model model) {
        UserDetailsViewDto userView = this.userService.getUserViewProfileByEmail(appUserDetails.getUsername());
        model.addAttribute("userProfile", userView);
        return "user-profile";
    }
}
