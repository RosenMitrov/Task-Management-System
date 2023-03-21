package app.taskmanagementsystem.init;

import app.taskmanagementsystem.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitialization implements CommandLineRunner {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final DepartmentService departmentService;
    private final ProgressService progressService;
    private final ClassificationService classificationService;
    private final TaskService taskService;
    private final PostService postService;
    private final CommentService commentService;


    public DatabaseInitialization(UserService userService,
                                  UserRoleService userRoleService,
                                  DepartmentService departmentService,
                                  ProgressService progressService,
                                  ClassificationService classificationService,
                                  TaskService taskService,
                                  PostService postService,
                                  CommentService commentService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.departmentService = departmentService;
        this.progressService = progressService;
        this.classificationService = classificationService;
        this.taskService = taskService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedProgress();
        seedClassifications();
        seedDepartments();

        seedUsers();
        seedTasks();
        seedPosts();
        seedComments();

    }

    private void seedComments() {
        this.commentService.commentsInitialization();
    }

    private void seedPosts() {
        this.postService.postsInitialization();
    }

    private void seedClassifications() {
        this.classificationService.classificationInitialization();
    }


    private void seedTasks() {
        this.taskService.taskInitialization();
    }


    private void seedProgress() {
        this.progressService.progressInitialization();
    }

    private void seedRoles() {
        this.userRoleService.userRolesInitialization();
    }

    private void seedUsers() {
        this.userService.userInitialization();
    }

    private void seedDepartments() {
        this.departmentService.departmentsInitialization();
    }
}
