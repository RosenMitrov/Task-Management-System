package app.taskmanagementsystem.helper;

import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;
import app.taskmanagementsystem.repositories.ClassificationRepository;
import app.taskmanagementsystem.repositories.ProgressRepository;
import app.taskmanagementsystem.repositories.TaskRepository;
import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HelperCreator {

    private final TaskRepository taskRepository;
    private final ProgressRepository progressRepository;
    private final ClassificationRepository classificationRepository;

    private final AdminService adminService;

    @Autowired
    public HelperCreator(TaskRepository taskRepository,
                         ProgressRepository progressRepository,
                         ClassificationRepository classificationRepository,
                         AdminService adminService) {
        this.taskRepository = taskRepository;
        this.progressRepository = progressRepository;
        this.classificationRepository = classificationRepository;
        this.adminService = adminService;
    }

    public TaskEntity createdTaskONE(String taskTitle) {
        TaskEntity taskToBeSaved = new TaskEntity()
                .setTitle(taskTitle)
                .setStartDate(LocalDateTime.now())
                .setDueDate(LocalDateTime.now().plusWeeks(1))
                .setCreatorName("creatorName")
                .setProgress(this.progressRepository.findFirstByProgress(ProgressTypeEnum.IN_PROGRESS).get())
                .setClassification(this.classificationRepository.findFirstByClassification(ClassificationTypeEnum.BUG).get())
                .setDescription("Some description");
        this.taskRepository.saveAndFlush(taskToBeSaved);
        return taskToBeSaved;
    }

    public  UserDetailsViewDto getUserDetailsById(long userId) {
        return this.adminService.getUserDetailsViewDtoByUserId(userId);
    }
}
