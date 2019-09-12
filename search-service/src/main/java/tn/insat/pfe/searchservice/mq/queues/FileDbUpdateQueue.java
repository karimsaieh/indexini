package tn.insat.pfe.searchservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;
import tn.insat.pfe.searchservice.mq.Constants;

@Configuration
public class FileDbUpdateQueue implements IRabbitQueue{

    @Override
    public Queue getQueue() {
        return new Queue(Constants.FILE_DB_UPDATE_QUEUE,false);
    }
}
