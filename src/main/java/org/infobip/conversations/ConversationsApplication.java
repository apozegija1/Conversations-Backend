package org.infobip.conversations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.infobip.conversations")
@EnableJpaRepositories(basePackages = "org.infobip.conversations")
public class ConversationsApplication {
    public static void main(String[] args) {
       SpringApplication.run(ConversationsApplication.class, args);
    }
}
