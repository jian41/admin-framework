package io.admin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdminFrameworkApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(AdminFrameworkApplication.class, args);
    }

}
