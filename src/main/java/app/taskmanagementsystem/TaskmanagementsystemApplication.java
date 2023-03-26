package app.taskmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskmanagementsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagementsystemApplication.class, args);
    }

}
