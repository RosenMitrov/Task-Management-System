package app.taskmanagementsystem.domain.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Column(name = "message", nullable = false)
    private String message;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    @Column(name = "creator_name", nullable = false)
    private String creatorName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public CommentEntity() {
    }

    public String getMessage() {
        return message;
    }

    public CommentEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public CommentEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public CommentEntity setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public CommentEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }
}
