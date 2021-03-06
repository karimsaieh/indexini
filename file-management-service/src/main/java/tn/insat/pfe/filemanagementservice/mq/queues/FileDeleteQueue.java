package tn.insat.pfe.filemanagementservice.mq.queues;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;
import tn.insat.pfe.filemanagementservice.mq.Constants;

import javax.annotation.PostConstruct;


@Component
@Qualifier("FileDeleteQueue")
public class FileDeleteQueue implements IRabbitQueue{

    private Queue queue;

    @PostConstruct
    public void init() {
        this.queue = new Queue(Constants.FILE_DELETE_QUEUE);
    }

    public Queue getQueue() {
        return queue;
    }
}
