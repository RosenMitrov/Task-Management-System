package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.view.DepartmentAdminViewDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all-users")
    public String adminPanelAllUsers(Model model) {
        final List<UserBasicViewDto> listOfAllUsers = this.adminService.findAllUserAdminBasicViewsDto();
        model.addAttribute("listOfAllUsers", listOfAllUsers);
        return "admin-users-all";
    }

    @GetMapping("/roles")
    public String getAllRoles() {
        return "roles-users-all-api";
    }

    @GetMapping("/user-details/{userId}")
    public String adminPanelGetUserById(@PathVariable("userId") Long userId,
                                        Model model) {
        final UserDetailsViewDto userById = this.adminService.getUserAdminDetailsViewDto(userId);
        model.addAttribute("userAdminDetailsViewDto", userById);
        return "admin-users-details";
    }

    @GetMapping("/user-edit/{userId}")
    public String edit(@PathVariable("userId") Long userId,
                       Model model) {
        final UserDetailsViewDto userById = this.adminService.getUserAdminDetailsViewDto(userId);
        model.addAttribute("userById", userById);
        return "admin-users-edit";
    }

    @PatchMapping("/user-edit/{userId}")
    public String updateUser(@PathVariable("userId") Long userId,
                             UserDetailsViewDto userDetailsViewDto) {
        this.adminService.updateUser(userId, userDetailsViewDto);
        return "redirect:/admin/all-users";
    }


    @PatchMapping("/user-edit-role/{userId}")
    public String updateUserRole(@PathVariable("userId") Long userId,
                                 RoleTypeEnum role) {
        this.adminService.updateUserRole(userId, role);
        return "redirect:/admin/user-edit/" + userId;
    }


    @GetMapping("/all-departments")
    public String adminPanelAllDepartments(Model model) {
        final List<DepartmentAdminViewDto> allDepartmentsAdminViews = this.adminService.findAllDepartmentsAdminViews();
        model.addAttribute("allDepartmentsAdminViews", allDepartmentsAdminViews);
        return "admin-departments-all";
    }


    @GetMapping("/department-details/{departmentId}")
    public String adminPanelDepartmentById(@PathVariable("departmentId") Long departmentId,
                                           Model model) {
        final DepartmentAdminViewDto departmentViewDto = this.adminService
                .getDepartmentAdminDetailsViewDtoById(departmentId);

        model.addAttribute("departmentViewDto", departmentViewDto);
        return "admin-departments-details";
    }


    @DeleteMapping("/user-delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        this.adminService.deleteUserEntityById(userId);
        return "redirect:/admin/all-users";
    }
}
