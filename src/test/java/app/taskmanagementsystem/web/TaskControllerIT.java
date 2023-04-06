package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import app.taskmanagementsystem.helper.HelperCreator;
import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.repositories.UserRoleRepository;
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

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HelperCreator helperCreator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    @WithUserDetails(
            value = "admin@adminov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getTasks_With_ADMIN_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("tasks-all"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allTasksDetailsViews"));
    }

    @Test
    @WithUserDetails(
            value = "moderator@moderatorov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getTasks_With_MODERATOR_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("tasks-all"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allTasksDetailsViews"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getTasks_With_USER_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("tasks-all"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allTasksDetailsViews"));
    }

    @Test
    @WithUserDetails(
            value = "admin@adminov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getTaskById_With_ADMIN_ROLE() throws Exception {
        TaskEntity task = helperCreator.createdTaskONE("Task ONE");
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/users/tasks/assign-logged-user-to-task-by-task-id/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "moderator@moderatorov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getTaskById_With_MODERATOR_ROLE() throws Exception {
        TaskEntity task = helperCreator.createdTaskONE("Task TWO");
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/users/tasks/assign-logged-user-to-task-by-task-id/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getTaskById_With_USER_ROLE() throws Exception {
        TaskEntity task = helperCreator.createdTaskONE("Task THREE");
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/users/tasks/assign-logged-user-to-task-by-task-id/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "admin@adminov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_declineTaskById_With_ADMIN_ROLE() throws Exception {
        TaskEntity task = helperCreator.createdTaskONE("Task FOUR");
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/users/tasks/detach-logged-user-from-task-by-task-id/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "moderator@moderatorov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_declineTaskById_With_MODERATOR_ROLE() throws Exception {
        TaskEntity task = helperCreator.createdTaskONE("Task FIVE");
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/users/tasks/detach-logged-user-from-task-by-task-id/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_declineTaskById_With_USER_ROLE() throws Exception {
        TaskEntity task = helperCreator.createdTaskONE("Task SIX");
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/users/tasks/detach-logged-user-from-task-by-task-id/" + task.getId())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "USER"})
    void test_addTaskWith_ShouldAddItSuccessfullyADMIN_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/add-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("taskAddDto"))
                .andExpect(MockMvcResultMatchers.view().name("tasks-add"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "moderator", roles = {"MODERATOR", "USER"})
    void test_addTaskWith_ShouldAddItSuccessfullyMODERATOR_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/add-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("taskAddDto"))
                .andExpect(MockMvcResultMatchers.view().name("tasks-add"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_addTaskWith_ShouldAddItSuccessfullyUSER_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/add-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("taskAddDto"))
                .andExpect(MockMvcResultMatchers.view().name("tasks-add"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithUserDetails(
            value = "admin@adminov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldAddSuccessfullyTask_ADMIN_ROLE() throws Exception {
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                            .param("title","Task title")
                            .param("dueDate", LocalDateTime.now().plusDays(1).toString())
                            .param("classification", ClassificationTypeEnum.BUG.toString())
                            .param("description","Some description")
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "moderator@moderatorov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldAddSuccessfullyTask_MODERAOR_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                        .param("title","Task title")
                        .param("dueDate", LocalDateTime.now().plusDays(1).toString())
                        .param("classification", ClassificationTypeEnum.BUG.toString())
                        .param("description","Some description")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldAddSuccessfullyTask_USER_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                        .param("title","Task title")
                        .param("dueDate", LocalDateTime.now().plusDays(1).toString())
                        .param("classification", ClassificationTypeEnum.BUG.toString())
                        .param("description","Some description")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/all"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldNotCreateAsTheTitleIsNotValid_ADMIN_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                        .param("title","")
                        .param("dueDate", LocalDateTime.now().plusDays(1).toString())
                        .param("classification", ClassificationTypeEnum.BUG.toString())
                        .param("description","Some description")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/add-task"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldNotCreateAsDueDateIsNotValid_ADMIN_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                        .param("title","First title")
                        .param("dueDate", LocalDateTime.now().minusDays(1).toString())
                        .param("classification", ClassificationTypeEnum.BUG.toString())
                        .param("description","Some description")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/add-task"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldNotCreateAsClassificationIsNotValid_ADMIN_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                        .param("title","First title")
                        .param("dueDate", LocalDateTime.now().plusDays(1).toString())
                        .param("classification", (String) null)
                        .param("description","Some description")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/add-task"));
    }

    @Test
    @WithUserDetails(
            value = "user@userov.bg",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_createTask_ShouldNotCreateAsDescriptionIsNotValid_ADMIN_ROLE() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/users/tasks/add-task")
                        .param("title","First title")
                        .param("dueDate", LocalDateTime.now().plusDays(1).toString())
                        .param("classification",ClassificationTypeEnum.BUG.toString())
                        .param("description","NO")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/tasks/add-task"));
    }
}
