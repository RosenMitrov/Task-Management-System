package app.taskmanagementsystem.domain.dto.view;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailsViewDto {
    private Long id;
    private String title;
    private String information;
    private LocalDateTime createdDate;
    private String creatorName;
    private TaskBasicViewDto task;
    private List<CommentDetailsViewDto> comments;

    public PostDetailsViewDto() {
    }

    public Long getId() {
        return id;
    }

    public PostDetailsViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostDetailsViewDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getInformation() {
        return information;
    }

    public PostDetailsViewDto setInformation(String information) {
        this.information = information;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public PostDetailsViewDto setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public PostDetailsViewDto setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public TaskBasicViewDto getTask() {
        return task;
    }

    public PostDetailsViewDto setTask(TaskBasicViewDto task) {
        this.task = task;
        return this;
    }

    public List<CommentDetailsViewDto> getComments() {
        return comments;
    }

    public PostDetailsViewDto setComments(List<CommentDetailsViewDto> comments) {
        this.comments = comments;
        return this;
    }
}
