package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.CommentDetailsDto;
import app.taskmanagementsystem.domain.dto.view.CommentDetailsViewDto;

import java.util.List;

public interface CommentService {
    void commentsInitialization();

    List<CommentDetailsViewDto> findAllCommentsDetailsViewByPostId(Long postId);

    void createCommentToPostByPostId(Long postId, CommentDetailsDto commentDetailsDto);

    Long deleteCommentById(Long commentId);

    boolean checkIfDeleteOwnComment(Long commentId,
                                 String nickname);

    Long getPostIdByCommentId(Long commentId);
}
