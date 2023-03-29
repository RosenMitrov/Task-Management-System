package app.taskmanagementsystem.domain.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDetailsDto {

    private Long id;
    private Long postId;
    @NotBlank
    @Size(min = 5)
    private String message;
    private LocalDateTime createdDate;
    private String creatorName;

    public CommentDetailsDto() {
    }

    public Long getId() {
        return id;
    }

    public CommentDetailsDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getPostId() {
        return postId;
    }

    public CommentDetailsDto setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentDetailsDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public CommentDetailsDto setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public CommentDetailsDto setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }
}
