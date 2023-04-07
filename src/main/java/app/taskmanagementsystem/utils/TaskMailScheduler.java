package app.taskmanagementsystem.utils;


import app.taskmanagementsystem.services.TaskService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskMailScheduler {

    private final TaskService taskService;

    @Autowired
    public TaskMailScheduler(TaskService taskService) {
        this.taskService = taskService;
    }


    @Scheduled(cron = "0 0 8 * * *")
    public void sendEmailToAllAssignedUsersIfDueDateOfTaskIsToday() throws MessagingException {
        this.taskService.sendEmail();
    }
}
