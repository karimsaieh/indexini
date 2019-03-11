package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;

public interface IRabbitQueue {
    Queue getQueue();
}
