package tn.insat.pfe.searchservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.insat.pfe.searchservice.mq.Constants;

@Configuration
public class FileIndexQueue{
    @Bean
    public Queue getFileIndexQueue() {
        return new Queue(Constants.FILES_INDEX_QUEUE, false);
    }
}
