package app.taskmanagementsystem.domain.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    @Column(name = "creator_name", nullable = false)
    private String creatorName;
    @OneToMany(mappedBy = "post", targetEntity = PictureEntity.class)
    private List<PictureEntity> pictures;
    @OneToMany(mappedBy = "post", targetEntity = CommentEntity.class)
    private List<CommentEntity> comments;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    public PostEntity() {
    }

    public String getTitle() {
        return title;
    }

    public PostEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public PostEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public PostEntity setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public PostEntity setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public PostEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public TaskEntity getTask() {
        return task;
    }

    public PostEntity setTask(TaskEntity task) {
        this.task = task;
        return this;
    }
}
