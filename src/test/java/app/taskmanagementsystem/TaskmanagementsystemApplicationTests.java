package app.taskmanagementsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskmanagementsystemApplicationTests {

    @Value("${mail.host}") String mailHost;
    @Value("${mail.port}") Integer mailPort;
    @Value("${mail.username}") String mailUsername;
    @Value("${mail.password}") String mailPassword;
    @Test
    void contextLoads() {
    }

}
