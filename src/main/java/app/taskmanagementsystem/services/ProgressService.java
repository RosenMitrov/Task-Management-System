package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.entity.ProgressEntity;
import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;

public interface ProgressService {
    void progressInitialization();

    ProgressEntity getProgressEntityByType(ProgressTypeEnum progressTypeEnum);
}
