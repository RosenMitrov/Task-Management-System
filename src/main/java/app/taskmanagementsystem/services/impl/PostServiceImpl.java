package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.model.PostAddDto;
import app.taskmanagementsystem.domain.dto.view.PostDetailsViewDto;
import app.taskmanagementsystem.domain.entity.PostEntity;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.PostRepository;
import app.taskmanagementsystem.services.PostService;
import app.taskmanagementsystem.services.TaskService;
import app.taskmanagementsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService, DbInit {
    private final PostRepository postRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           UserService userService,
                           TaskService taskService,
                           ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean isDbInit() {
        return this.postRepository.count() > 0;
    }

    @Override
    public void postsInitialization() {
        if (isDbInit()) {
            return;
        }
        
        List<PostEntity> allPosts = List.of(
                new PostEntity()
                        .setTitle("POST 1")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(this.userService.getUserEntityByEmail("admin@adminov.bg").getUsername())
                        .setTask(this.taskService.getTaskEntityById(3L)),
                new PostEntity()
                        .setTitle("POST 2")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(this.userService.getUserEntityByEmail("moderator@moderatorov.bg").getUsername())
                        .setTask(this.taskService.getTaskEntityById(2L)),
                new PostEntity()
                        .setTitle("POST 3")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(this.userService.getUserEntityByEmail("user@userov.bg").getUsername())
                        .setTask(this.taskService.getTaskEntityById(1L))
        );

        this.postRepository.saveAllAndFlush(allPosts);
    }

    @Override
    public PostEntity getPostEntityById(long postId) {
        Optional<PostEntity> postEntityById = this.postRepository.findById(postId);
        if (postEntityById.isEmpty()) {
            // TODO: 3/16/2023 think about exception
            return null;
        }

        return postEntityById.get();
    }

    @Override
    @Transactional
    public List<PostDetailsViewDto> findAllPostsByTaskId(Long taskId) {
        List<PostEntity> allPostsByTaskId = this.postRepository.findAllByTask_Id(taskId);
        return allPostsByTaskId
                .stream()
                .map(this::fromPostEntityToPostDetailsView)
                .collect(Collectors.toList());
    }

    @Override
    public void createNewPost(PostAddDto postAddDto,
                              String username) {
        TaskEntity taskEntityById = this.taskService.getTaskEntityById(postAddDto.getTaskId());

        PostEntity newPostEntity = new PostEntity()
                .setTitle(postAddDto.getTitle())
                .setTask(taskEntityById)
                .setCreatorName(username)
                .setCreatedDate(LocalDateTime.now());

        this.postRepository.saveAndFlush(newPostEntity);
    }

    private PostDetailsViewDto fromPostEntityToPostDetailsView(PostEntity postEntity) {
        return this.modelMapper
                .map(postEntity, PostDetailsViewDto.class);
    }
}
