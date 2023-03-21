package app.taskmanagementsystem.repositories;

import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findFirstByEmail(String email);
    Optional<UserEntity> findFirstByUsername(String username);
    @Query("select u from UserEntity as u " +
            " join u.roles as ur " +
            " where ur.id = :roleId")
    Optional<List<UserEntity>> findAllByRole_id(@Param("roleId") Long roleId);



}
