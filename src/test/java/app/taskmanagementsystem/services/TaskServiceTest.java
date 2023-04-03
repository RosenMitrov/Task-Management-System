package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.TaskAddDto;
import app.taskmanagementsystem.domain.dto.view.TaskDetailsViewDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.entity.ClassificationEntity;
import app.taskmanagementsystem.domain.entity.ProgressEntity;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.TaskRepository;
import app.taskmanagementsystem.services.impl.TaskServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private TaskService taskServiceToTest;

    @Mock
    private TaskRepository mockTaskRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private ProgressService mockProgressService;
    @Mock
    private ClassificationService mockClassificationService;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private EmailService mockEmailService;

    @Captor
    private ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor;


    @BeforeEach
    void setUp() {
        this.taskServiceToTest = new TaskServiceImpl(
                this.mockTaskRepository,
                this.mockUserService,
                this.mockProgressService,
                this.mockClassificationService,
                this.mockModelMapper,
                this.mockEmailService
        );
    }

    @Test
    void test_getTaskEntityByTaskId_ShouldReturnItSuccessfully() {
        long taskId = 69L;
        ClassificationEntity classificationBUG = new ClassificationEntity().setClassification(ClassificationTypeEnum.BUG);
        ProgressEntity progressOPEN = new ProgressEntity().setProgress(ProgressTypeEnum.OPEN);
        TaskEntity expectedEntity = new TaskEntity()
                .setCreatorName("testCreatorName")
                .setTitle("Test task name")
                .setDescription("Test description")
                .setClassification(classificationBUG)
                .setProgress(progressOPEN)
                .setStartDate(LocalDateTime.now())
                .setDueDate(LocalDateTime.now().plusWeeks(2));

        Mockito.when(this.mockTaskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedEntity));

        TaskEntity actualEntity = this.taskServiceToTest.getTaskEntityByTaskId(taskId);

        Assertions.assertEquals(expectedEntity, actualEntity);
        Assertions.assertEquals(expectedEntity.getCreatorName(), actualEntity.getCreatorName());
        Assertions.assertEquals(expectedEntity.getTitle(), actualEntity.getTitle());
        Assertions.assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
        Assertions.assertEquals(expectedEntity.getClassification(), actualEntity.getClassification());
        Assertions.assertEquals(expectedEntity.getClassification().getClassification(), actualEntity.getClassification().getClassification());
        Assertions.assertEquals(expectedEntity.getProgress(), actualEntity.getProgress());
        Assertions.assertEquals(expectedEntity.getProgress().getProgress(), actualEntity.getProgress().getProgress());
        Assertions.assertEquals(expectedEntity.getStartDate(), actualEntity.getStartDate());
        Assertions.assertEquals(expectedEntity.getDueDate(), actualEntity.getDueDate());
    }

    @Test
    void test_getTaskEntityByTaskId_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            long idNotFound = 42L;
            taskServiceToTest.getTaskEntityByTaskId(idNotFound);
        });
    }

    @Test
    void test_getAllTasksDetailsViews_ShouldReturnThemSuccessfully() {
        String sessionUserEmail = "isUserInSession@true-example.bg";

        List<TaskEntity> expectedTasksDetailViews = List.of(new TaskEntity()
                        .setTitle("First Test TASK")
                        .setDescription("Description of first TASK")
                        .setCreatorName("isUserInSession@true-example.bg")
                        .setAssignedUsers(List.of(
                                new UserEntity().setEmail("noUserInSession@example.bg"
                                )
                        )),
                new TaskEntity()
                        .setTitle("Second Test TASK")
                        .setDescription("Description of second TASK")
                        .setCreatorName("test@testov.bg")
                        .setAssignedUsers(List.of(
                                new UserEntity().setEmail("isUserInSession@true-example.bg"
                                )
                        ))
        );

        Mockito
                .when(mockTaskRepository.findAll())
                .thenReturn(expectedTasksDetailViews);

        TaskEntity firstTask = expectedTasksDetailViews.get(0);
        String assignedUserToFirstTaskEmail = firstTask.getAssignedUsers().get(0).getEmail();

        Mockito
                .when(mockModelMapper.map(firstTask, TaskDetailsViewDto.class))
                .thenReturn(new TaskDetailsViewDto()
                        .setTitle(firstTask.getTitle())
                        .setDescription(firstTask.getDescription())
                        .setCreatorName(firstTask.getCreatorName())
                        .setAssignedUsers(List.of(
                                new UserBasicViewDto().setEmail(assignedUserToFirstTaskEmail)
                        ))
                        .setAssignedUserInSession(firstTask
                                .getAssignedUsers()
                                .stream()
                                .anyMatch(u -> u.getEmail().equals(sessionUserEmail)
                                )));

        TaskEntity secondTask = expectedTasksDetailViews.get(1);
        String assignedUserToSecondTaskEmail = secondTask.getAssignedUsers().get(0).getEmail();

        Mockito
                .when(mockModelMapper.map(secondTask, TaskDetailsViewDto.class))
                .thenReturn(new TaskDetailsViewDto()
                        .setTitle(secondTask.getTitle())
                        .setDescription(secondTask.getDescription())
                        .setCreatorName(secondTask.getCreatorName())
                        .setAssignedUsers(List.of(
                                new UserBasicViewDto().setEmail(assignedUserToSecondTaskEmail)
                        ))
                        .setAssignedUserInSession(secondTask
                                .getAssignedUsers()
                                .stream()
                                .anyMatch(u -> u.getEmail().equals(sessionUserEmail)
                                )));


        List<TaskDetailsViewDto> actualTasksDetailViews = this.taskServiceToTest
                .getAllTasksDetailsViews(sessionUserEmail);

        Assertions.assertEquals(expectedTasksDetailViews.get(0).getTitle(), actualTasksDetailViews.get(0).getTitle());
        Assertions.assertEquals(expectedTasksDetailViews.get(0).getDescription(), actualTasksDetailViews.get(0).getDescription());
        Assertions.assertEquals(expectedTasksDetailViews.get(0).getCreatorName(), actualTasksDetailViews.get(0).getCreatorName());
        Assertions.assertEquals(expectedTasksDetailViews.get(0).getAssignedUsers().get(0).getEmail(), actualTasksDetailViews.get(0).getAssignedUsers().get(0).getEmail());
        Assertions.assertEquals(expectedTasksDetailViews.get(1).getAssignedUsers().get(0).getEmail(), actualTasksDetailViews.get(1).getAssignedUsers().get(0).getEmail());
    }

    @Test
    void test_getTaskDetailsViewByTaskId_ShouldReturnItSuccessfully() {
        long taskId = 69L;
        TaskEntity expectedTask = new TaskEntity()
                .setTitle("Test task title")
                .setCreatorName("creator@create.bg")
                .setDescription("Test task description");

        Mockito
                .when(mockTaskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedTask));

        Mockito
                .when(mockModelMapper.map(expectedTask, TaskDetailsViewDto.class))
                .thenReturn(new TaskDetailsViewDto()
                        .setTitle(expectedTask.getTitle())
                        .setCreatorName(expectedTask.getCreatorName())
                        .setDescription(expectedTask.getDescription()));


        TaskDetailsViewDto actualTask = this.taskServiceToTest.getTaskDetailsViewByTaskId(taskId);

        Assertions.assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        Assertions.assertEquals(expectedTask.getCreatorName(), actualTask.getCreatorName());
        Assertions.assertEquals(expectedTask.getDescription(), actualTask.getDescription());
    }

    @Test
    void test_getTaskDetailsViewByTaskId_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            long idNotFound = 69L;
            taskServiceToTest.getTaskDetailsViewByTaskId(idNotFound);
        });
    }

    @Test
    void test_CreateTask_ShouldCreateItSuccessfully() {
        String creatorName = "creatorName";
        TaskAddDto expectedTaskAddDto = new TaskAddDto()
                .setTitle("Task to be created.")
                .setClassification(ClassificationTypeEnum.BUG)
                .setDescription("Task description!")
                .setDueDate(LocalDateTime.now().plusWeeks(1));

        Mockito
                .when(mockModelMapper.map(expectedTaskAddDto, TaskEntity.class))
                .thenReturn(new TaskEntity()
                        .setTitle(expectedTaskAddDto.getTitle())
                        .setDescription(expectedTaskAddDto.getDescription())
                        .setDueDate(expectedTaskAddDto.getDueDate()));

        Mockito
                .when(this.mockProgressService
                        .getProgressEntityByType(OPEN))
                .thenReturn(new ProgressEntity().setProgress(OPEN));

        Mockito
                .when(this.mockClassificationService.getClassificationByEnumType(expectedTaskAddDto.getClassification()))
                .thenReturn(new ClassificationEntity().setClassification(expectedTaskAddDto.getClassification()));

        this.taskServiceToTest.createTask(expectedTaskAddDto, creatorName);

        Mockito
                .verify(mockTaskRepository, Mockito.times(1))
                .saveAndFlush(this.taskEntityArgumentCaptor.capture());

        TaskEntity actualEntity = this.taskEntityArgumentCaptor.getValue();

        Assertions.assertEquals(expectedTaskAddDto.getTitle(), actualEntity.getTitle());
        Assertions.assertEquals(expectedTaskAddDto.getClassification(), actualEntity.getClassification().getClassification());
        Assertions.assertEquals(expectedTaskAddDto.getDescription(), actualEntity.getDescription());
        Assertions.assertEquals(expectedTaskAddDto.getDueDate(), actualEntity.getDueDate());
        Assertions.assertEquals(OPEN, actualEntity.getProgress().getProgress());

    }

    @Test
    void test_assignUserToTask_ShouldHTrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            long idNotFound = 13L;
            String email = "email@email.bg";
            this.taskServiceToTest.assignUserToTask(idNotFound, email);
        });
    }

    @Test
    void test_assignUserToTask_FirstAssigned_StatusChangedFrom_OPEN_to_IN_PROGRESS() {
        long taskId = 42L;
        String email = "email@email.bg";

        ProgressEntity currentProgress = new ProgressEntity().setProgress(OPEN);
        TaskEntity expectedTask = new TaskEntity()
                .setTitle("Task title")
                .setProgress(currentProgress)
                .setAssignedUsers(new ArrayList<>());

        Mockito
                .when(this.mockTaskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedTask));

        ProgressEntity newProgress = new ProgressEntity().setProgress(IN_PROGRESS);
        Mockito
                .when(this.mockProgressService.getProgressEntityByType(IN_PROGRESS))
                .thenReturn(newProgress);

        UserEntity userToAssign = new UserEntity().setEmail(email);
        Mockito
                .when(this.mockUserService.getUserEntityByEmail(email))
                .thenReturn(userToAssign);


        this.taskServiceToTest.assignUserToTask(taskId, email);

        Mockito
                .verify(mockTaskRepository, Mockito.times(1))
                .saveAndFlush(taskEntityArgumentCaptor.capture());

        TaskEntity actualTask = taskEntityArgumentCaptor.getValue();

        Assertions.assertEquals(expectedTask.hashCode(), actualTask.hashCode());
        Assertions.assertEquals(expectedTask.getId(), actualTask.getId());
        Assertions.assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        Assertions.assertEquals(newProgress.getProgress(), actualTask.getProgress().getProgress());
        Assertions.assertEquals(userToAssign, actualTask.getAssignedUsers().get(0));
        Assertions.assertEquals(userToAssign.getEmail(), actualTask.getAssignedUsers().get(0).getEmail());
    }


    @Test
    void test_assignUserToTask_FirstAssigned_StatusChangedFrom_COMPLETED_to_RE_OPEN() {
        long taskId = 13L;
        String email = "email@email.bg";

        ProgressEntity currentProgress = new ProgressEntity().setProgress(COMPLETED);
        TaskEntity expectedTask = new TaskEntity()
                .setTitle("Task title")
                .setProgress(currentProgress)
                .setAssignedUsers(new ArrayList<>());

        Mockito
                .when(this.mockTaskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedTask));

        ProgressEntity newProgress = new ProgressEntity().setProgress(RE_OPEN);
        Mockito
                .when(this.mockProgressService.getProgressEntityByType(RE_OPEN))
                .thenReturn(newProgress);

        UserEntity userToAssign = new UserEntity().setEmail(email);
        Mockito
                .when(this.mockUserService.getUserEntityByEmail(email))
                .thenReturn(userToAssign);

        this.taskServiceToTest.assignUserToTask(taskId, email);

        Mockito
                .verify(mockTaskRepository, Mockito.times(1))
                .saveAndFlush(taskEntityArgumentCaptor.capture());

        TaskEntity actualTask = taskEntityArgumentCaptor.getValue();

        Assertions.assertEquals(expectedTask.hashCode(), actualTask.hashCode());
        Assertions.assertEquals(expectedTask.getId(), actualTask.getId());
        Assertions.assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        Assertions.assertEquals(newProgress.getProgress(), actualTask.getProgress().getProgress());
        Assertions.assertEquals(userToAssign, actualTask.getAssignedUsers().get(0));
        Assertions.assertEquals(userToAssign.getEmail(), actualTask.getAssignedUsers().get(0).getEmail());
    }

    @Test
    void test_removeUserFromTaskById_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            long idNotFound = 13L;
            String email = "test@test.bg";
            this.taskServiceToTest.removeUserFromTaskById(idNotFound, email);
        });
    }

    @Test
    void test_removeUserFromTaskById_RemoveItSuccessfully_AndChangeProgressStatusFrom_IN_PROGRESS_to_COMPLETED() {
        long taskId = 42L;
        String email = "user@email.bg";

        UserEntity currentAssignedUser = new UserEntity().setEmail(email);
        ProgressEntity currentProgress = new ProgressEntity().setProgress(IN_PROGRESS);

        List<UserEntity> assignedUsers = new ArrayList<>(Collections.singletonList(currentAssignedUser));

        TaskEntity expectedTask = new TaskEntity()
                .setTitle("Test TITLE")
                .setProgress(currentProgress)
                .setAssignedUsers(assignedUsers);

        Mockito.when(this.mockTaskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedTask));

        Mockito.when(this.mockUserService.getUserEntityByEmail(email))
                .thenReturn(currentAssignedUser);

        ProgressEntity newProgressCompleted = new ProgressEntity().setProgress(COMPLETED);
        Mockito.when(this.mockProgressService.getProgressEntityByType(COMPLETED))
                .thenReturn(newProgressCompleted);

        this.taskServiceToTest.removeUserFromTaskById(taskId, email);

        Mockito.verify(mockTaskRepository, Mockito.times(1))
                .saveAndFlush(taskEntityArgumentCaptor.capture());

        TaskEntity actualTask = taskEntityArgumentCaptor.getValue();

        Assertions.assertEquals(expectedTask, actualTask);
        Assertions.assertEquals(expectedTask.hashCode(), actualTask.hashCode());
        Assertions.assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        Assertions.assertEquals(expectedTask.getAssignedUsers(), actualTask.getAssignedUsers());
        Assertions.assertEquals(0, actualTask.getAssignedUsers().size());
        Assertions.assertEquals(newProgressCompleted.getProgress(), actualTask.getProgress().getProgress());
    }


    @Test
    void test_removeUserFromTaskById_NotChangingStatusАsTheListIsNotEmpty_RemoveUserSuccessfully() {
        long taskId = 42L;
        String userEmailWhoWillBeRemoved = "user@email.bg";
        String userEmailWhoWillRemain = "remain@email.bg";

        UserEntity userWillBeRemoved = new UserEntity().setEmail(userEmailWhoWillBeRemoved);
        UserEntity userWillRemain = new UserEntity().setEmail(userEmailWhoWillRemain);
        ProgressEntity currentProgress = new ProgressEntity().setProgress(IN_PROGRESS);

        List<UserEntity> assignedUsers = new ArrayList<>(Arrays.asList(userWillBeRemoved, userWillRemain));

        TaskEntity expectedTask = new TaskEntity()
                .setTitle("Test TITLE")
                .setProgress(currentProgress)
                .setAssignedUsers(assignedUsers);

        Mockito.when(this.mockTaskRepository.findById(taskId))
                .thenReturn(Optional.of(expectedTask));

        Mockito.when(this.mockUserService.getUserEntityByEmail(userEmailWhoWillBeRemoved))
                .thenReturn(userWillBeRemoved);


        this.taskServiceToTest.removeUserFromTaskById(taskId, userEmailWhoWillBeRemoved);

        Mockito.verify(mockTaskRepository, Mockito.times(1))
                .saveAndFlush(taskEntityArgumentCaptor.capture());

        TaskEntity actualTask = taskEntityArgumentCaptor.getValue();

        Assertions.assertEquals(expectedTask, actualTask);
        Assertions.assertEquals(expectedTask.hashCode(), actualTask.hashCode());
        Assertions.assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        Assertions.assertEquals(1, actualTask.getAssignedUsers().size());
        Assertions.assertEquals(userWillRemain, actualTask.getAssignedUsers().get(0));
        Assertions.assertEquals(userWillRemain.getEmail(), actualTask.getAssignedUsers().get(0).getEmail());
    }

    @Test
    void test_sendEmail_ShouldNotSendEmail_AsThereIsNoTaskDueDateWithCurrentDate() throws MessagingException {
        String email = "test@testov.bg";
        String receiverFullName = "Test Testov";
        long taskId = 13;
        String taskCreatorName = "creator@create.bg";
        String taskTitle = "Test task title";

        LocalDateTime currentTime = LocalDateTime.now();
        int year = currentTime.getYear();
        int month = currentTime.getMonth().getValue();
        int day = currentTime.getDayOfMonth();

        Mockito
                .when(this.mockTaskRepository
                        .findAllByDueDate_YearAndDueDate_MonthAndDueDate_DayOfMonth(year, month, day))
                .thenReturn(new ArrayList<>());

        this.taskServiceToTest.sendEmail();

        Mockito
                .verify(mockEmailService, Mockito.times(0))
                .sendEmailToUserWithTaskWhichDueDateIsToday(email, receiverFullName, taskId, taskCreatorName, taskTitle);
    }

    @Test
    void test_sendEmail_ShouldSendEmailToTwoUsersRelatedToOneTaskSuccessfully() throws MessagingException {

        LocalDateTime currentTime = LocalDateTime.now();
        int year = currentTime.getYear();
        int month = currentTime.getMonth().getValue();
        int day = currentTime.getDayOfMonth();

        UserEntity userOne = new UserEntity()
                .setFirstName("Test")
                .setLastName("Testov")
                .setEmail("test@testov.bg");
        UserEntity userTwo = new UserEntity()
                .setFirstName("Test_1")
                .setLastName("Testov_1")
                .setEmail("test_1@testov_1.bg");

        long taskId = 13L;
        String creatorName = "CreatorName";
        String taskTitle = "Test Task Title";
        Mockito
                .when(this.mockTaskRepository
                        .findAllByDueDate_YearAndDueDate_MonthAndDueDate_DayOfMonth(year, month, day))
                .thenReturn(List.of(
                        (TaskEntity) new TaskEntity()
                                .setTitle(taskTitle)
                                .setAssignedUsers(List.of(
                                        userOne,
                                        userTwo

                                ))
                                .setCreatorName(creatorName)
                                .setId(taskId)
                ));

        this.taskServiceToTest.sendEmail();

        Mockito
                .verify(mockEmailService, Mockito.times(2))
                .sendEmailToUserWithTaskWhichDueDateIsToday(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        Mockito
                .verify(mockEmailService, Mockito.times(1))
                .sendEmailToUserWithTaskWhichDueDateIsToday(
                        userOne.getEmail(),
                        userOne.getFirstName() + " " + userOne.getLastName(),
                        taskId, creatorName, taskTitle);

        Mockito
                .verify(mockEmailService, Mockito.times(1))
                .sendEmailToUserWithTaskWhichDueDateIsToday(
                        userTwo.getEmail(),
                        userTwo.getFirstName() + " " + userTwo.getLastName(),
                        taskId, creatorName, taskTitle);
    }


    @Test
    void test_taskInitialization_ShouldPopulateDB_Successfully() {

        long countClassificationsInDb = 69L;
        Mockito
                .when(this.mockClassificationService.getCount())
                .thenReturn(countClassificationsInDb);

        Random randomClassification = new Random(countClassificationsInDb);

        ProgressEntity expectedStatusInProgress = new ProgressEntity().setProgress(IN_PROGRESS);
        Mockito
                .when(this.mockProgressService.getProgressEntityByType(IN_PROGRESS))
                .thenReturn(expectedStatusInProgress);
        ClassificationEntity expectedClassification = new ClassificationEntity().setClassification(ClassificationTypeEnum.BUG);
        Mockito
                .when(this.mockClassificationService.getClassificationEntityById(randomClassification.nextLong(countClassificationsInDb) + 1))
                .thenReturn(expectedClassification);
        UserEntity adminUserEntity = new UserEntity().setEmail("admin@adminov.bg");
        Mockito
                .when(this.mockUserService.getUserEntityByEmail("admin@adminov.bg"))
                .thenReturn(adminUserEntity);
        UserEntity userUserEntity = new UserEntity().setEmail("user@userov.bg");
        Mockito
                .when(this.mockUserService.getUserEntityByEmail("user@userov.bg"))
                .thenReturn(userUserEntity);
        UserEntity moderatorUserEntity = new UserEntity().setEmail("moderator@moderatorov.bg");
        Mockito
                .when(this.mockUserService.getUserEntityByEmail("moderator@moderatorov.bg"))
                .thenReturn(moderatorUserEntity);

        List<TaskEntity> expectedTasks = List.of(
                new TaskEntity()
                        .setTitle("Title One")
                        .setProgress(expectedStatusInProgress)
                        .setClassification(expectedClassification)
                        .setAssignedUsers(List.of(adminUserEntity, moderatorUserEntity)),
                new TaskEntity()
                        .setTitle("Title Two")
                        .setProgress(expectedStatusInProgress)
                        .setClassification(expectedClassification)
                        .setAssignedUsers(List.of(adminUserEntity, moderatorUserEntity, userUserEntity))

        );

        this.taskServiceToTest.taskInitialization();

        Mockito
                .when(mockTaskRepository.saveAllAndFlush(Mockito.any()))
                .thenReturn(expectedTasks);

        Mockito
                .verify(mockTaskRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());

        List<TaskEntity> actualTasks = mockTaskRepository.saveAllAndFlush(Mockito.any());

        Assertions.assertEquals(expectedTasks, actualTasks);
        Assertions.assertEquals(expectedTasks.get(0).getTitle(), actualTasks.get(0).getTitle());
        Assertions.assertEquals(expectedTasks.get(1).getTitle(), actualTasks.get(1).getTitle());
        Assertions.assertEquals(2, actualTasks.get(0).getAssignedUsers().size());
        Assertions.assertEquals(3, actualTasks.get(1).getAssignedUsers().size());
        Assertions.assertEquals("admin@adminov.bg", actualTasks.get(0).getAssignedUsers().get(0).getEmail());
        Assertions.assertEquals("moderator@moderatorov.bg", actualTasks.get(0).getAssignedUsers().get(1).getEmail());
        Assertions.assertEquals("admin@adminov.bg", actualTasks.get(1).getAssignedUsers().get(0).getEmail());
        Assertions.assertEquals("moderator@moderatorov.bg", actualTasks.get(1).getAssignedUsers().get(1).getEmail());
        Assertions.assertEquals("user@userov.bg", actualTasks.get(1).getAssignedUsers().get(2).getEmail());
    }


    @Test
    void test_isDbInit_ShouldReturnTrue() {
        Mockito.when(mockTaskRepository.count())
                .thenReturn(50L);

        taskServiceToTest.taskInitialization();

        Mockito
                .verify(mockTaskRepository, Mockito.times(0))
                .saveAllAndFlush(Mockito.any());
    }

}
