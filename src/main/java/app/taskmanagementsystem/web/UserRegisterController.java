package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.UserRegisterDto;
import app.taskmanagementsystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserRegisterController {

    private final UserService userService;

    @Autowired
    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("userRegisterDto")) {
            model.addAttribute("userRegisterDto", new UserRegisterDto());
            model.addAttribute("userAlreadyExists", false);
            model.addAttribute("passNotMatch", false);
        }
        return "register";
    }


    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDto userRegisterDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            addFlashAttributesFoUserRegisterDto(userRegisterDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDto", bindingResult);
            return "redirect:register";
        }

        if (!this.userService.checkIfPasswordsMatch(userRegisterDto)) {
            addFlashAttributesFoUserRegisterDto(userRegisterDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("passNotMatch", true);
            return "redirect:/users/register";
        }


        if (!this.userService.registerUserEntity(userRegisterDto)) {
            addFlashAttributesFoUserRegisterDto(userRegisterDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("userAlreadyExists", true);
            return "redirect:/users/register";
        }

        return "redirect:/users/login";
    }

    private static void addFlashAttributesFoUserRegisterDto(UserRegisterDto userRegisterDto,
                                                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userRegisterDto", userRegisterDto);
    }
}
