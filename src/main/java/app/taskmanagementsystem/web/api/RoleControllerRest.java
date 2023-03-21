package app.taskmanagementsystem.web.api;

import app.taskmanagementsystem.domain.dto.view.rest.RoleRestViewDto;
import app.taskmanagementsystem.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleControllerRest {

    private final UserRoleService userRoleService;

    @Autowired
    public RoleControllerRest(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleRestViewDto>> getAllRoles() {
        List<RoleRestViewDto> allRolesAsView = this.userRoleService.allRolesStats();
        return ResponseEntity
                .ok(allRolesAsView);
    }
}
