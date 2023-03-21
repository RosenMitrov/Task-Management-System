package app.taskmanagementsystem.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(TemplateEngine templateEngine,
                        JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

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

    private String generateMessageContent(String username) {
        Context context = new Context();
        context.setVariable("userName", username);
      return   this.templateEngine.process("/email/registration", context);

    }
}
