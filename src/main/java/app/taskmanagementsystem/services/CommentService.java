package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.CommentDetailsViewDto;

import java.util.List;

public interface CommentService {
    void commentsInitialization();

    List<CommentDetailsViewDto> findAllCommentsByPostId(Long postId);
}
