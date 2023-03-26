package app.taskmanagementsystem.services;

public interface EmailService {


    void sendRegistrationEmail(
            String userEmail,
            String username
    );

    String generateMessageContent(String username);

    void sendEmailToUserWithTaskWhichDueDateIsToday(String email,
                                                    String receiverFullName,
                                                    Long task,
                                                    String taskCreatorName,
                                                    String taskTitle);

    String generateDueDateTaskMessage(String receiverFullName,
                                      Long taskId,
                                      String taskCreatorName,
                                      String taskTitle);


}

