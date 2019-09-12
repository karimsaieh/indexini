package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.insat.pfe.filemanagementservice.mq.Constants;

@Configuration
public class FileDbUpdateQueue {
    //used to create the queue only
    @Bean
    public Queue getFileDbUpdateQueue() {
        return new Queue(Constants.FILE_DB_UPDATE_QUEUE, false);
    }

}
