package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.view.CommentDetailsViewDto;
import app.taskmanagementsystem.domain.entity.CommentEntity;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.repositories.CommentRepository;
import app.taskmanagementsystem.services.CommentService;
import app.taskmanagementsystem.services.PostService;
import app.taskmanagementsystem.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostService postService,
                              UserService userService,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void commentsInitialization() {
        UserEntity adminEntityByEmail = this.userService.getUserEntityByEmail("admin@adminov.bg");
        UserEntity moderatorEntityByEmail = this.userService.getUserEntityByEmail("moderator@moderatorov.bg");
        UserEntity userEntityByEmail = this.userService.getUserEntityByEmail("user@userov.bg");
        List<CommentEntity> commentsToBeSaved = List.of(
                new CommentEntity()
                        .setMessage("Comment for task 1")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(adminEntityByEmail.getUsername())
                        .setPost(this.postService.getPostEntityById(1L)),
                new CommentEntity()
                        .setMessage("Another Comment for task 1")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(moderatorEntityByEmail.getUsername())
                        .setPost(this.postService.getPostEntityById(1L)),
                new CommentEntity()
                        .setMessage("Another Comment for task 2")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(userEntityByEmail.getUsername())
                        .setPost(this.postService.getPostEntityById(2L)),
                new CommentEntity()
                        .setMessage("Another Comment for task 2")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(adminEntityByEmail.getUsername())
                        .setPost(this.postService.getPostEntityById(2L)),
                new CommentEntity()
                        .setMessage("Another Comment for task 3")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(adminEntityByEmail.getUsername())
                        .setPost(this.postService.getPostEntityById(3L)),
                new CommentEntity()
                        .setMessage("Another Comment for task 3")
                        .setCreatedDate(LocalDateTime.now())
                        .setCreatorName(moderatorEntityByEmail.getUsername())
                        .setPost(this.postService.getPostEntityById(3L))
        );

        this.commentRepository.saveAllAndFlush(commentsToBeSaved);
    }

    @Override
    public List<CommentDetailsViewDto> findAllCommentsByPostId(Long postId) {
        List<CommentEntity> allCommentsByPostId = this.commentRepository.findAllByPost_Id(postId);
        return allCommentsByPostId
                .stream()
                .map(this::fromCommentEntityToCommentDetailsView)
                .collect(Collectors.toList());
    }

    private CommentDetailsViewDto fromCommentEntityToCommentDetailsView(CommentEntity commentEntity) {
        return this.modelMapper
                .map(commentEntity, CommentDetailsViewDto.class);
    }
}
