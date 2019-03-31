package tn.insat.pfe.searchservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.insat.pfe.searchservice.mq.Constants;

@Configuration
public class FileDeleteQueue {
    @Bean
    public Queue getFileDeleteQueue(){
        return new Queue(Constants.FILE_DELETE_QUEUE, false);
    }
}
