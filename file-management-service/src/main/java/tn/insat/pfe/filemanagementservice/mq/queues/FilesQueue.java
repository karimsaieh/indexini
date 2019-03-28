package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.insat.pfe.filemanagementservice.mq.Constants;

@Configuration
public class FilesQueue{

    @Bean
    public Queue replyQueue() {
        return new Queue(Constants.FILES_QUEUE, false);
    }

}

