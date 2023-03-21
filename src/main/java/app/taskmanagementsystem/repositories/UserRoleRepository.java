package app.taskmanagementsystem.repositories;

import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    Optional<UserRoleEntity> findFirstByRole(RoleTypeEnum role);




}
