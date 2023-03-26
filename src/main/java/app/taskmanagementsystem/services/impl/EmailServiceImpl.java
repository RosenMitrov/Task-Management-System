package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(TemplateEngine templateEngine,
                            JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationEmail(
            String userEmail,
            String username
    ) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("task-management-sytem@tms-group.bg");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Successful registration!");
            mimeMessageHelper.setText(generateMessageContent(username), true);

            this.javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateMessageContent(String username) {
        Context context = new Context();
        context.setVariable("userName", username);
        return this.templateEngine.process("/email/registration", context);

    }

    @Override
    public void sendEmailToUserWithTaskWhichDueDateIsToday(String email,
                                                           String receiverFullName,
                                                           Long task,
                                                           String taskCreatorName,
                                                           String taskTitle) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("task-management-sytem@tms-group.bg");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Uncompleted task");
            mimeMessageHelper.setText(generateDueDateTaskMessage(receiverFullName, task, taskCreatorName, taskTitle), true);

            this.javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateDueDateTaskMessage(String receiverFullName,
                                             Long taskId,
                                             String taskCreatorName,
                                             String taskTitle) {
        Context context = new Context();
        context.setVariable("receiverFullName", receiverFullName);
        context.setVariable("taskId", taskId);
        context.setVariable("taskCreatorName", taskCreatorName);
        context.setVariable("taskTitle", taskTitle);
        return this.templateEngine.process("/email/task-due-date", context);
    }


}

