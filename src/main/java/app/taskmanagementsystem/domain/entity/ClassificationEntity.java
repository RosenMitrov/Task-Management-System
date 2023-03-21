package app.taskmanagementsystem.domain.entity;


import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "classifications")
public class ClassificationEntity extends BaseEntity {

    @Column(name = "classification", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ClassificationTypeEnum classification;

    public ClassificationEntity() {
    }

    public ClassificationTypeEnum getClassification() {
        return classification;
    }

    public ClassificationEntity setClassification(ClassificationTypeEnum classification) {
        this.classification = classification;
        return this;
    }
}
