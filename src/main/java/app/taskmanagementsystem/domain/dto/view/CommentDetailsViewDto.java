package app.taskmanagementsystem.domain.dto.view;

import java.time.LocalDateTime;

public class CommentDetailsViewDto {

    private String message;
    private LocalDateTime createdDate;
    private String creatorName;

    public CommentDetailsViewDto() {
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
