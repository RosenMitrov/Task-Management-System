package app.taskmanagementsystem.domain.dto.view;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDetailsViewDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String creatorName;
    private ClassificationBasicView classification;
    private ProgressBasicView progress;
    private List<UserBasicViewDto> assignedUsers;
    private boolean isAssignedUserInSession;

    public TaskDetailsViewDto() {
    }

    public Long getId() {
        return id;
    }

    public TaskDetailsViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskDetailsViewDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TaskDetailsViewDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public TaskDetailsViewDto setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public TaskDetailsViewDto setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public TaskDetailsViewDto setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public ClassificationBasicView getClassification() {
        return classification;
    }

    public TaskDetailsViewDto setClassification(ClassificationBasicView classification) {
        this.classification = classification;
        return this;
    }

    public ProgressBasicView getProgress() {
        return progress;
    }

    public TaskDetailsViewDto setProgress(ProgressBasicView progress) {
        this.progress = progress;
        return this;
    }

    public List<UserBasicViewDto> getAssignedUsers() {
        return assignedUsers;
    }

    public TaskDetailsViewDto setAssignedUsers(List<UserBasicViewDto> assignedUsers) {
        this.assignedUsers = assignedUsers;
        return this;
    }

    public boolean isAssignedUserInSession() {
        return isAssignedUserInSession;
    }

    public TaskDetailsViewDto setAssignedUserInSession(boolean assignedUserInSession) {
        isAssignedUserInSession = assignedUserInSession;
        return this;
    }
}
