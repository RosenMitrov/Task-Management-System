package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.repositories.UserRoleRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;

    private UserEntity userEntity;
    private UserRoleEntity userRoleEntity;

    @BeforeEach
    void setUp() {
        userRoleEntity = new UserRoleEntity().setRole(RoleTypeEnum.USER).setDescription("aaaa");
        userRoleRepository.saveAndFlush(userRoleEntity);

        userEntity = new UserEntity()
                .setFirstName("test first")
                .setLastName("set last")
                .setPassword("123")
                .setEnabled(true)
                .setCreatedOn(LocalDateTime.now())
                .setEmail("adminTest@adminov.bg")
                .setUsername("aaa")
                .setRoles(List.of(userRoleEntity));
        this.userRepository.saveAndFlush(userEntity);
    }

    @Test
    @WithUserDetails(value = "adminTest@adminov.bg")
    void test_getTasks() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/tasks/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("tasks-all"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allTasksDetailsViews"));
    }
}
