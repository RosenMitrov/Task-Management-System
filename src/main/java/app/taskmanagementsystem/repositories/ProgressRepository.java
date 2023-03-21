package app.taskmanagementsystem.repositories;

import app.taskmanagementsystem.domain.entity.ProgressEntity;
import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {

    Optional<ProgressEntity> findFirstByProgress(ProgressTypeEnum progress);
}
