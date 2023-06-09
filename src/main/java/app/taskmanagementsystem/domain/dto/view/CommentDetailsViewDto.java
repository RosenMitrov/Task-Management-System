package app.taskmanagementsystem.domain.dto.view;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDetailsViewDto {

    private Long id;
    private Long postId;

    private String message;
    private LocalDateTime createdDate;
    private String creatorName;

    public CommentDetailsViewDto() {
    }

    public Long getId() {
        return id;
    }

    public CommentDetailsViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getPostId() {
        return postId;
    }

    public CommentDetailsViewDto setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentDetailsViewDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public CommentDetailsViewDto setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public CommentDetailsViewDto setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }
}
