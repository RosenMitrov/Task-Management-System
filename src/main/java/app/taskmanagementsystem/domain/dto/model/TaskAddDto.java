package app.taskmanagementsystem.domain.dto.model;

import app.taskmanagementsystem.domain.entity.ClassificationEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaskAddDto {

    @NotBlank
    @Size(min = 3, max = 20)
    private String title;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    private LocalDateTime startDate;
    @NotNull
    private ClassificationTypeEnum classification;
    @NotBlank
    @Size(min = 5)
    private String description;

    public TaskAddDto() {
    }

    public String getTitle() {
        return title;
    }

    public TaskAddDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public TaskAddDto setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public ClassificationTypeEnum getClassification() {
        return classification;
    }

    public TaskAddDto setClassification(ClassificationTypeEnum classification) {
        this.classification = classification;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TaskAddDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
