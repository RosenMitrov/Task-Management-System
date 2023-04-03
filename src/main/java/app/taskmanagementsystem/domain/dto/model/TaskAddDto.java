package app.taskmanagementsystem.domain.dto.model;

import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaskAddDto {

    @NotBlank
    @Size(min = 3, max = 20)
    private String title;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent
    private LocalDateTime dueDate;
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

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public TaskAddDto setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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
