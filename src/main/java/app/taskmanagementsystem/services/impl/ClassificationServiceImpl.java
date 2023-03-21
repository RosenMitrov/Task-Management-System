package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.entity.ClassificationEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.ClassificationRepository;
import app.taskmanagementsystem.services.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ClassificationServiceImpl implements ClassificationService, DbInit {
    private final ClassificationRepository classificationRepository;

    @Autowired
    public ClassificationServiceImpl(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    @Override
    public boolean isDbInit() {
        return this.classificationRepository.count() != 0;
    }

    @Override
    public void classificationInitialization() {
        if (isDbInit()) {
            return;
        }
        List<ClassificationEntity> allClassifications = Arrays.stream(ClassificationTypeEnum.values())
                .map(this::mapFromEnumToEntity)
                .toList();

        this.classificationRepository.saveAllAndFlush(allClassifications);
    }

    @Override
    public long getCount() {
        return this.classificationRepository.count();
    }

    @Override
    public ClassificationEntity getClassificationEntityById(long classificationId) {
        Optional<ClassificationEntity> classificationRepositoryById = this.classificationRepository.findById(classificationId);
        if (classificationRepositoryById.isEmpty()) {
            // TODO: 3/14/2023 think about exception
            return null;
        }
        return classificationRepositoryById.get();
    }

    @Override
    public ClassificationEntity getClassificationByEnuType(ClassificationTypeEnum classification) {
        Optional<ClassificationEntity> optionalClassificationEntity = this.classificationRepository.findFirstByClassification(classification);
        if (optionalClassificationEntity.isEmpty()) {
            // TODO: 3/19/2023 think about exception!
            return null;
        }
        return optionalClassificationEntity.get();
    }

    private ClassificationEntity mapFromEnumToEntity(ClassificationTypeEnum classificationTypeEnum) {
        return new ClassificationEntity()
                .setClassification(classificationTypeEnum);
    }


}
