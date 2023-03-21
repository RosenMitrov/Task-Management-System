package app.taskmanagementsystem.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class TaskEntity extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "creator_name", nullable = false)
    private String creatorName;
    @ManyToOne
    @JoinColumn(name = "classification_id")
    private ClassificationEntity classification;
    @ManyToOne
    @JoinColumn(name = "progress_id")
    private ProgressEntity progress;
    @ManyToMany
    @JoinTable(name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "assigned_user_id")
    )
    private List<UserEntity> assignedUsers;

    @OneToMany(mappedBy = "task", targetEntity = PostEntity.class)
    private List<PostEntity> posts;

    public TaskEntity() {
    }

    public String getTitle() {
        return title;
    }

    public TaskEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TaskEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public TaskEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public TaskEntity setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public TaskEntity setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public ClassificationEntity getClassification() {
        return classification;
    }

    public TaskEntity setClassification(ClassificationEntity classification) {
        this.classification = classification;
        return this;
    }

    public ProgressEntity getProgress() {
        return progress;
    }

    public TaskEntity setProgress(ProgressEntity progress) {
        this.progress = progress;
        return this;
    }

    public List<UserEntity> getAssignedUsers() {
        return assignedUsers;
    }

    public TaskEntity setAssignedUsers(List<UserEntity> assignedUsers) {
        this.assignedUsers = assignedUsers;
        return this;
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public TaskEntity setPosts(List<PostEntity> posts) {
        this.posts = posts;
        return this;
    }

    public void removeUserFromAssignUsersList(UserEntity userEntity) {
        this.assignedUsers.remove(userEntity);
    }

    public void clearTaskFromAllPosts() {
        this.posts
                .forEach(postEntity -> postEntity.setTask(null));
    }

    public void addUserToTask(UserEntity user) {
        if (this.assignedUsers
                .stream()
                .anyMatch(userEntity -> Objects.equals(userEntity.getId(), user.getId()))) {
            return;
        }
        this.assignedUsers.add(user);
    }
}
