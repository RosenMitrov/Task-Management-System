package app.taskmanagementsystem.domain.dto.view;

public class TaskBasicViewDto {

    private Long id;
    private String title;
    private String description;

    public TaskBasicViewDto() {
    }

    public Long getId() {
        return id;
    }

    public TaskBasicViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskBasicViewDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TaskBasicViewDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
