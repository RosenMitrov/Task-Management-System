package app.taskmanagementsystem.repositories;

import app.taskmanagementsystem.domain.entity.ClassificationEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificationRepository extends JpaRepository<ClassificationEntity, Long> {

    Optional<ClassificationEntity> findFirstByClassification(ClassificationTypeEnum classification);
}
