package app.taskmanagementsystem.domain.dto.model;

import jakarta.validation.constraints.Size;

public class PostAddDto {

    @Size(min = 3)
    private String title;
    private Long taskId;
    public PostAddDto() {
    }

    public String getTitle() {
        return title;
    }

    public PostAddDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public Long getTaskId() {
        return taskId;
    }

    public PostAddDto setTaskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }
}
