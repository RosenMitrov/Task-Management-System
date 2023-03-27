package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.model.TaskAddDto;
import app.taskmanagementsystem.domain.dto.view.TaskDetailsViewDto;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.TaskRepository;
import app.taskmanagementsystem.services.*;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum.*;

@Service
public class TaskServiceImpl implements TaskService, DbInit {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProgressService progressService;
    private final ClassificationService classificationService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           ProgressService progressService,
                           ClassificationService classificationService,
                           ModelMapper modelMapper,
                           EmailService emailService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.progressService = progressService;
        this.classificationService = classificationService;

        this.modelMapper = modelMapper;

        this.emailService = emailService;
    }

    @Override
    public boolean isDbInit() {
        return this.taskRepository.count() != 0;
    }

    @Override
    public void taskInitialization() {
        if (isDbInit()) {
            return;
        }


        long countClassificationsInDb = this.classificationService.getCount();
        Random randomClassification = new Random(countClassificationsInDb);
        List<TaskEntity> allTasks = new ArrayList<>();


        for (int i = 1; i <= 2; i++) {

            UserEntity adminUserEntity = this.userService.getUserEntityByEmail("admin@adminov.bg");
            UserEntity userUserEntity = this.userService.getUserEntityByEmail("user@userov.bg");
            UserEntity moderatorUserEntity = this.userService.getUserEntityByEmail("moderator@moderatorov.bg");
            TaskEntity serviceLayerTask = new TaskEntity()
                    .setTitle("Fixing service layer" + i)
                    .setProgress(this.progressService.getProgressEntityByType(IN_PROGRESS))
                    .setClassification(this.classificationService.getClassificationEntityById(randomClassification.nextLong(countClassificationsInDb) + 1))
                    .setStartDate(LocalDateTime.now())
                    .setDueDate(LocalDateTime.now().plusWeeks(2))
                    .setAssignedUsers(List.of(userUserEntity))
                    .setCreatorName(userUserEntity.getUsername())
                    .setDescription("Some description for Fixing service layer");

            TaskEntity repositoryLayer = new TaskEntity()
                    .setTitle("Fixing repository layer" + i)
                    .setProgress(this.progressService.getProgressEntityByType(IN_PROGRESS))
                    .setClassification(this.classificationService.getClassificationEntityById(randomClassification.nextLong(countClassificationsInDb) + 1))
                    .setStartDate(LocalDateTime.now())
                    .setDueDate(LocalDateTime.now().plusWeeks(2))
                    .setAssignedUsers(List.of(moderatorUserEntity, userUserEntity, adminUserEntity))
                    .setCreatorName(moderatorUserEntity.getUsername())
                    .setDescription("Some description for Fixing repository layer");

            allTasks.add(serviceLayerTask);
            allTasks.add(repositoryLayer);

        }

        this.taskRepository.saveAllAndFlush(allTasks);

    }

    @Override
    public TaskEntity getTaskEntityById(long taskId) {
        Optional<TaskEntity> taskRepositoryById = this.taskRepository.findById(taskId);

        if (taskRepositoryById.isEmpty()) {
           throw new ObjNotFoundException();
        }
        return taskRepositoryById.get();
    }

    @Override
    @Transactional
    public List<TaskDetailsViewDto> getAllTasksDetailsViews(String sessionUserEmail) {
        List<TaskEntity> allTaskEntities = this.taskRepository.findAll();
        return allTaskEntities
                .stream()
                .map(taskEntity -> fromTaskEntityToDetailsView(taskEntity, sessionUserEmail))
                .collect(Collectors.toList());
    }

    private TaskDetailsViewDto fromTaskEntityToDetailsView(TaskEntity taskEntity,
                                                           String sessionUserEmail) {
        TaskDetailsViewDto mapped = fromTaskEntityToDetailsView(taskEntity);
        return mapped.setAssignedUserInSession(mapped
                .getAssignedUsers()
                .stream()
                .anyMatch(userBasicViewDto -> userBasicViewDto.getEmail().equals(sessionUserEmail)));
    }

    @Override
    @Transactional
    public TaskDetailsViewDto getTaskDetailsViewById(Long taskId) {
        Optional<TaskEntity> taskEntityById = this.taskRepository.findById(taskId);
        if (taskEntityById.isEmpty()) {
            throw new ObjNotFoundException();
        }
        return fromTaskEntityToDetailsView(taskEntityById.get());
    }

    @Override
    public void createTask(TaskAddDto taskAddDto,
                           String creatorUsername) {
        TaskEntity taskTobeSaved = this.modelMapper.map(taskAddDto, TaskEntity.class);
        taskTobeSaved
                .setProgress(this.progressService.getProgressEntityByType(OPEN))
                .setClassification(this.classificationService.getClassificationByEnumType(taskAddDto.getClassification()))
                .setCreatorName(creatorUsername)
                .setStartDate(LocalDateTime.now());
        this.taskRepository.saveAndFlush(taskTobeSaved);
    }

    @Override
    @Transactional
    public void assignUserToTask(Long taskId,
                                 String email) {
        Optional<TaskEntity> optionalTask = this.taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new ObjNotFoundException();
        }
        TaskEntity taskEntity = optionalTask.get();
        if (taskEntity.getProgress().getProgress() == OPEN) {
            taskEntity.setProgress(this.progressService.getProgressEntityByType(IN_PROGRESS));
        }
        if (taskEntity.getProgress().getProgress() == COMPLETED) {
            taskEntity.setProgress(this.progressService.getProgressEntityByType(RE_OPEN));
        }
        UserEntity userToBeAddedToTask = this.userService.getUserEntityByEmail(email);
        taskEntity.addUserToTask(userToBeAddedToTask);

        this.taskRepository.saveAndFlush(taskEntity);
    }

    @Override
    @Transactional
    public void removeUserFromTaskById(Long taskId,
                                       String email) {
        Optional<TaskEntity> optionalTask = this.taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return;
        }
        TaskEntity taskEntity = optionalTask.get();
        UserEntity userToBeDetached = this.userService.getUserEntityByEmail(email);
        taskEntity.removeUserFromTask(userToBeDetached);
        if (taskEntity.getCountOfAssignedUsers() == 0) {
            taskEntity.setProgress(this.progressService.getProgressEntityByType(COMPLETED));
        }
        this.taskRepository.saveAndFlush(taskEntity);
    }

    @Override
    @Transactional
    public void sendEmail() throws MessagingException {
        LocalDateTime currentTime = LocalDateTime.now();

        int year = currentTime.getYear();
        int month = currentTime.getMonth().getValue();
        int day = currentTime.getDayOfMonth();

        List<TaskEntity> allByDueDate = this.taskRepository.findAllByDueDate_YearAndDueDate_MonthAndDueDate_DayOfMonth(year, month, day);
        if (allByDueDate.size() == 0) {
            return;
        }

        for (TaskEntity task : allByDueDate) {
            List<UserEntity> assignedUsers = task.getAssignedUsers();
            for (UserEntity assignedUser : assignedUsers) {
                this.emailService.sendEmailToUserWithTaskWhichDueDateIsToday(assignedUser.getEmail(),
                        assignedUser.getFirstName() + " " + assignedUser.getLastName(),
                        task.getId(),
                        task.getCreatorName(),
                        task.getTitle());
            }
        }
    }


    private TaskDetailsViewDto fromTaskEntityToDetailsView(TaskEntity taskEntity) {
        return this.modelMapper.map(taskEntity, TaskDetailsViewDto.class);
    }
}
