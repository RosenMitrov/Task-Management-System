package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.entity.ProgressEntity;
import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.ProgressRepository;
import app.taskmanagementsystem.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressServiceImpl implements ProgressService, DbInit {

    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public boolean isDbInit() {
        return this.progressRepository.count() != 0;
    }
    @Override
    public void progressInitialization() {

        if (isDbInit()) {
            return;
        }

        List<ProgressEntity> allProgressEntities = Arrays
                .stream(ProgressTypeEnum.values())
                .map(this::fromProgressTypeEnumToEntity)
                .toList();

        this.progressRepository.saveAllAndFlush(allProgressEntities);
    }

    @Override
    public ProgressEntity getProgressEntityByType(ProgressTypeEnum progressTypeEnum) {
        Optional<ProgressEntity> firstByProgress = this.progressRepository.findFirstByProgress(progressTypeEnum);
        if (firstByProgress.isEmpty()) {
            throw new ObjNotFoundException();
        }
        return firstByProgress.get();
    }

    private ProgressEntity fromProgressTypeEnumToEntity(ProgressTypeEnum progressTypeEnum) {
        return new ProgressEntity()
                .setProgress(progressTypeEnum);
    }
}
