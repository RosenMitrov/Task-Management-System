package app.taskmanagementsystem.domain.entity;



import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "progresses")
public class ProgressEntity extends BaseEntity {
    @Column(name = "progress")
    @Enumerated(EnumType.STRING)
    private ProgressTypeEnum progress;

    public ProgressEntity() {
    }

    public ProgressTypeEnum getProgress() {
        return progress;
    }

    public ProgressEntity setProgress(ProgressTypeEnum progress) {
        this.progress = progress;
        return this;
    }
}
