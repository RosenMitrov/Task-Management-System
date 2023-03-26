package app.taskmanagementsystem.repositories;

import app.taskmanagementsystem.domain.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {




    @Query("SELECT t FROM TaskEntity as t " +
            " where year(t.dueDate) = :y and month(t.dueDate) = :m and day(t.dueDate) = :d ")
    Optional<List<TaskEntity>> findAllByDueDate_YearAndDueDate_MonthAndDueDate_DayOfMonth(@Param("y") int year,
                                                                                          @Param("m") int month,
                                                                                          @Param("d") int day
    );



}
