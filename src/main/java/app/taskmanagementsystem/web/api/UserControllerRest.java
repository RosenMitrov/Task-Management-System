package app.taskmanagementsystem.web.api;

import app.taskmanagementsystem.domain.dto.view.rest.UserBasicRestViewDto;
import app.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserControllerRest {

    private final UserService userService;

    @Autowired
    public UserControllerRest(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user-with-role/{roleId}")
    public ResponseEntity<List<UserBasicRestViewDto>> getUsersByRoleId(@PathVariable("roleId") Long roleId) {
        List<UserBasicRestViewDto> allUsersByRoleId = this.userService.findAllUsersRestViewsRoleId(roleId);
        return ResponseEntity
                .ok(allUsersByRoleId);
    }
}
