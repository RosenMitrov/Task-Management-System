package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.TaskAddDto;
import app.taskmanagementsystem.domain.dto.view.TaskDetailsViewDto;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import jakarta.mail.MessagingException;

import java.util.List;

public interface TaskService {
    void taskInitialization();

    TaskEntity getTaskEntityByTaskId(long taskId);

    List<TaskDetailsViewDto> getAllTasksDetailsViews(String sessionUserEmail);

    TaskDetailsViewDto getTaskDetailsViewByTaskId(Long taskId);

    void createTask(TaskAddDto taskAddDto, String creatorUsername);


    void assignUserToTask(Long taskId,
                          String email);

    void removeUserFromTaskById(Long taskId,
                                String email);

    void sendEmail() throws MessagingException;

}
