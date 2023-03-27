package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.entity.ClassificationEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;

public interface ClassificationService {
    void classificationInitialization();

    long getCount();

    ClassificationEntity getClassificationEntityById(long classificationId);

    ClassificationEntity getClassificationByEnumType(ClassificationTypeEnum classification);
}
