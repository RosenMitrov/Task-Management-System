package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.helper.HelperCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HelperCreator helperCreator;

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_adminPanelAllUsers_ShouldRenderThemSuccessfully_ADMIN() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/all-users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allUserBasicViewsDto"))
                .andExpect(MockMvcResultMatchers.view().name("admin-users-all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_adminPanelAllUsers_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/all-users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_adminPanelAllUsers_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/all-users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_getAllRoles_ShouldReturnThemSuccessfully_ADMIN() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/roles"))
                .andExpect(MockMvcResultMatchers.view().name("roles-users-all-api"));
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_getAllRoles_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/roles"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_getAllRoles_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/roles"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_adminPanelGetUserById_ShouldReturnItSuccessfully_ADMIN() throws Exception {
        long existingUserId = 1L;

        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/user-details/" + existingUserId))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userDetailsViewDto"))
                .andExpect(MockMvcResultMatchers.view().name("admin-users-details"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_adminPanelGetUserById_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        long existingUserId = 1L;

        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/user-details/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_adminPanelGetUserById_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        long existingUserId = 1L;

        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/user-details/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_edit_ShouldBeSuccessful_ADMIN() throws Exception {
        long existingUserId = 1L;

        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/user-edit/" + existingUserId))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userDetailsViewDto"))
                .andExpect(MockMvcResultMatchers.view().name("admin-users-edit"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_edit_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        long existingUserId = 1L;

        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/user-edit/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_edit_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        long existingUserId = 1L;

        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/user-edit/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_updateUser_ShouldBeSuccessfullyUpdated_ADMIN() throws Exception {
        long existingUserId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/admin/user-edit/" + existingUserId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("department", DepartmentTypeEnum.SALES_TEAM.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/all-users"));
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_updateUser_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        long existingUserId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/admin/user-edit/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_updateUser_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        long existingUserId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/admin/user-edit/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_updateUserRole_ShouldUpdateRolesSuccessfully_ADMIN() throws Exception {
        long existingUserId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/admin/user-edit-role/" + existingUserId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("role", RoleTypeEnum.USER.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user-edit/" + existingUserId));
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_updateUserRole_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        long existingUserId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/admin/user-edit-role/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_updateUserRole_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        long existingUserId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/admin/user-edit-role/" + existingUserId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_adminPanelAllDepartments_ShouldReturnThemSuccessfully_ADMIN() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/all-departments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allDepartmentViews"))
                .andExpect(MockMvcResultMatchers.view().name("admin-departments-all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_adminPanelAllDepartments_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/all-departments"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_adminPanelAllDepartments_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/all-departments"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = "ADMIN")
    void test_adminPanelDepartmentById_ShouldReturnItSuccessfully_ADMIN() throws Exception {
        long departmentId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/department-details/" + departmentId))
                .andExpect(MockMvcResultMatchers.model().attributeExists("departmentViewDto"))
                .andExpect(MockMvcResultMatchers.view().name("admin-departments-details"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testModerator", roles = "MODERATOR")
    void test_adminPanelDepartmentById_ShouldBeForbiddenAsTheRoleIsNotAppropriate_MODERATOR() throws Exception {
        long departmentId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/department-details/" + departmentId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void test_adminPanelDepartmentById_ShouldBeForbiddenAsTheRoleIsNotAppropriate_USER() throws Exception {
        long departmentId = 2L;
        mockMvc
                .perform(MockMvcRequestBuilders.get("/admin/department-details/" + departmentId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails(
            value = "admin@adminov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_deleteUser_ShouldDeleteSuccessfully_ADMIN() throws Exception {
        long existingId = 1L;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/admin/user-delete/" + existingId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.flash().attributeExists( "deletedSuccessfully"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists( "selfDelete"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/all-users"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }


    @Test
    @WithUserDetails(
            value = "moderator@moderatorov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_deleteUser_ShouldDeleteSuccessfully_MODERATOR() throws Exception {
        long existingId = 1L;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/admin/user-delete/" + existingId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_deleteUser_ShouldDeleteSuccessfully_USER() throws Exception {
        long existingId = 1L;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/admin/user-delete/" + existingId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}
