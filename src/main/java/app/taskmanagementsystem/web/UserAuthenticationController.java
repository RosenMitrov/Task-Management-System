package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.UserLoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserAuthenticationController {


    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginDto")) {
            model.addAttribute("userLoginDto", new UserLoginDto());
            model.addAttribute("badCredentials",false);
        }
        return "login";
    }


    @PostMapping("/login-error")
    public String errorLogin(UserLoginDto userLoginDto,
                             RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("userLoginDto", userLoginDto);
            redirectAttributes.addFlashAttribute("badCredentials", true);
        return "redirect:login";
    }




}
