package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.model.TaskAddDto;
import app.taskmanagementsystem.domain.dto.view.TaskDetailsViewDto;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.TaskRepository;
import app.taskmanagementsystem.services.ClassificationService;
import app.taskmanagementsystem.services.ProgressService;
import app.taskmanagementsystem.services.TaskService;
import app.taskmanagementsystem.services.UserService;
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

import static app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum.IN_PROGRESS;
import static app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum.OPEN;

@Service
public class TaskServiceImpl implements TaskService, DbInit {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProgressService progressService;
    private final ClassificationService classificationService;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           ProgressService progressService,
                           ClassificationService classificationService,
                           ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.progressService = progressService;
        this.classificationService = classificationService;

        this.modelMapper = modelMapper;
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
                    .setAssignedUsers(List.of(userUserEntity))
                    .setCreatorName(userUserEntity.getUsername())
                    .setDescription("Some description for Fixing service layer");

            TaskEntity repositoryLayer = new TaskEntity()
                    .setTitle("Fixing repository layer" + i)
                    .setProgress(this.progressService.getProgressEntityByType(IN_PROGRESS))
                    .setClassification(this.classificationService.getClassificationEntityById(randomClassification.nextLong(countClassificationsInDb) + 1))
                    .setStartDate(LocalDateTime.now())
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
            // TODO: 3/16/2023 think about exception
            return null;
        }
        return taskRepositoryById.get();
    }

    @Override
    @Transactional
    public List<TaskDetailsViewDto> getAllTasksDetailsViews() {
        List<TaskEntity> allTaskEntities = this.taskRepository.findAll();

        return allTaskEntities
                .stream()
                .map(this::fromTaskEntityToDetailsView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDetailsViewDto getTaskDetailsViewById(Long taskId) {
        Optional<TaskEntity> taskEntityById = this.taskRepository.findById(taskId);
        if (taskEntityById.isEmpty()) {
            // TODO: 3/18/2023 think about exception
            return null;
        }
        return fromTaskEntityToDetailsView(taskEntityById.get());
    }

    @Override
    public void createTask(TaskAddDto taskAddDto,
                           String creatorUsername) {
        TaskEntity taskTobeSaved = this.modelMapper.map(taskAddDto, TaskEntity.class);
        taskTobeSaved
                .setProgress(this.progressService.getProgressEntityByType(OPEN))
                .setClassification(this.classificationService.getClassificationByEnuType(taskAddDto.getClassification()))
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
            return;
        }
        TaskEntity taskEntity = optionalTask.get();
        if (taskEntity.getProgress().getProgress() == OPEN) {
            taskEntity.setProgress(this.progressService.getProgressEntityByType(IN_PROGRESS));
        }
        UserEntity userToBeAddedToTask = this.userService.getUserEntityByEmail(email);
        taskEntity.addUserToTask(userToBeAddedToTask);

        this.taskRepository.saveAndFlush(taskEntity);
    }


    private TaskDetailsViewDto fromTaskEntityToDetailsView(TaskEntity taskEntity) {
        return this.modelMapper.map(taskEntity, TaskDetailsViewDto.class);
    }
}
