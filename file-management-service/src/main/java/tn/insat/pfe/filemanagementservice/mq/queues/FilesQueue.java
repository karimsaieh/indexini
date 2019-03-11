package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilesQueue{

    @Bean
    public Queue replyQueue() {
        return new Queue("files_queue", false);
    }

}

