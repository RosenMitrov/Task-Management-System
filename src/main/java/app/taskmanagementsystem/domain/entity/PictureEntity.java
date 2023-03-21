package app.taskmanagementsystem.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {
    @Column(name = "label", nullable = false)
    private String label;
    @Column(name = "img_url", nullable = false)
    private String imgUrl;
    @Column(name = "added_on", nullable = false)
    private LocalDateTime addedOn;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public PictureEntity() {
    }

    public String getLabel() {
        return label;
    }

    public PictureEntity setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public PictureEntity setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public PictureEntity setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public PostEntity getPost() {
        return post;
    }

    public PictureEntity setPost(PostEntity post) {
        this.post = post;
        return this;
    }
}
