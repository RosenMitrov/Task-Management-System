package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.UserChangePasswordDto;
import app.taskmanagementsystem.security.AppUserDetails;
import app.taskmanagementsystem.services.CredentialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String changePassword(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                 Model model) {
        if (!model.containsAttribute("userChangePasswordDto")) {
            model.addAttribute("userChangePasswordDto", new UserChangePasswordDto()
                    .setEmail(appUserDetails.getUsername())
                    .setFullName(appUserDetails.getFullName()));
            model.addAttribute("passNotMatch", false);

        }
        return "change-password";
    }

    @PostMapping("/change-password")
    public String doChangePassword(@Valid UserChangePasswordDto userChangePasswordDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal AppUserDetails appUserDetails) {
        userChangePasswordDto
                .setEmail(appUserDetails.getUsername())
                .setFullName(appUserDetails.getFullName());

        if (bindingResult.hasErrors()) {
            addFlashAttributesFoUserRegisterDto(userChangePasswordDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userChangePasswordDto", bindingResult);
            return "redirect:/users/change-password";
        }

        if (!this.credentialService.checkIfPasswordsMatch(userChangePasswordDto)) {
            addFlashAttributesFoUserRegisterDto(userChangePasswordDto, redirectAttributes);
            redirectAttributes.addFlashAttribute("passNotMatch", true);
            return "redirect:/users/change-password";
        }

        if (this.credentialService.checkIfPasswordMatchToLastOne(userChangePasswordDto)) {
            addFlashAttributesFoUserRegisterDto(userChangePasswordDto.setEmail(appUserDetails.getUsername()).setFullName(appUserDetails.getFullName()), redirectAttributes);
            redirectAttributes.addFlashAttribute("lastPassMatch", true);
            return "redirect:/users/change-password";
        }

        this.credentialService.changePassword(userChangePasswordDto);

        return "redirect:/";
    }

    private static void addFlashAttributesFoUserRegisterDto(UserChangePasswordDto userChangePasswordDto,
                                                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userChangePasswordDto", userChangePasswordDto);
    }

}
