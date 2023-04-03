package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.UserChangePasswordDto;
import app.taskmanagementsystem.services.CredentialService;
import app.taskmanagementsystem.utils.SessionManagementUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class PasswordController {
    private final CredentialService credentialService;

    @Autowired
    public PasswordController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/change-password")
    public String changePassword(Model model,
                                 HttpServletRequest httpServletRequest) throws ServletException {

        if (!model.containsAttribute("userChangePasswordDto")) {
            model.addAttribute("userChangePasswordDto", new UserChangePasswordDto());
            model.addAttribute("passNotMatch", false);
        }

        SessionManagementUtil.logoutUser(httpServletRequest);
        return "change-password";
    }

    @PatchMapping("/change-password")
    public String doChangePassword(@Valid UserChangePasswordDto userChangePasswordDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest httpServletRequest) throws ServletException {

        if (bindingResult.hasErrors()) {
            addFlashAttributesFoUserRegisterDto(userChangePasswordDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userChangePasswordDto", bindingResult);
            return "redirect:/users/change-password";
        }

        if (!this.credentialService.checkIfNewPasswordsMatch(userChangePasswordDto) || !this.credentialService.checkIfOldPasswordMatchToProvidedPassword(userChangePasswordDto)) {
            addFlashAttributesFoUserRegisterDto(userChangePasswordDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("passNotMatch", true);
            return "redirect:/users/change-password";
        }

        if (this.credentialService.checkIfPasswordMatchToLastOne(userChangePasswordDto)) {
            addFlashAttributesFoUserRegisterDto(userChangePasswordDto.setEmail(userChangePasswordDto.getEmail()), redirectAttributes);
            redirectAttributes.addFlashAttribute("lastPassMatch", true);
            return "redirect:/users/change-password";
        }

        SessionManagementUtil.logoutUser(httpServletRequest);
        this.credentialService.changePassword(userChangePasswordDto);
        return "redirect:/users/login";
    }


    private static void addFlashAttributesFoUserRegisterDto(UserChangePasswordDto userChangePasswordDto,
                                                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userChangePasswordDto", userChangePasswordDto);
    }

}
