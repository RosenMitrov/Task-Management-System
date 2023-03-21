package app.taskmanagementsystem.domain.dto.view;

import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;

public class ClassificationBasicView {

    private ClassificationTypeEnum classification;

    public ClassificationBasicView() {
    }

    public ClassificationTypeEnum getClassification() {
        return classification;
    }

    public ClassificationBasicView setClassification(ClassificationTypeEnum classification) {
        this.classification = classification;
        return this;
    }
}
