package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.entity.*;
import app.taskmanagementsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TEST_CONTROLLER_DEL {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ClassificationRepository classificationRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public TEST_CONTROLLER_DEL(DepartmentRepository departmentRepository,
                               UserRepository userRepository,
                               TaskRepository taskRepository,
                               ClassificationRepository classificationRepository,
                               PostRepository postRepository,
                               CommentRepository commentRepository,
                               UserRoleRepository userRoleRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.classificationRepository = classificationRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable long id) {

        UserEntity userEntity = this.userRepository.findById(id).get();

        userEntity.removeUserFromAllTasks(userEntity);
        this.userRepository.deleteById(id);

        return "test";
    }

    @GetMapping("/delete/task/{id}")
    @Transactional
    public String delTask(@PathVariable long id) {
        TaskEntity taskEntity = this.taskRepository.findById(id).get();

        taskEntity.clearTaskFromAllPosts();

        this.taskRepository.deleteById(id);
        return "test";
    }

    @GetMapping("/delete/role/{id}")
    @Transactional
    public String delRole(@PathVariable long id) {
        UserRoleEntity role = this.userRoleRepository.findById(id).get();

        role.removeRoleFromAllUsers(role);
        this.userRoleRepository.delete(role);

        return "test";
    }

    @GetMapping("/delete/post/{id}")
    @Transactional
    public String deletePost(@PathVariable long id) {

        PostEntity postEntity = this.postRepository.findById(id).get();

        List<CommentEntity> comments =
                postEntity.getComments();

        this.commentRepository.deleteAll(comments);

        this.postRepository.deleteById(id);

        return "test";
    }


    @GetMapping("/sss")
    public String ssss() {



        return "test";
    }

}


