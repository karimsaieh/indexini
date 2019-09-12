package tn.insat.pfe.searchservice.mq.queues;

import org.springframework.amqp.core.Queue;

public interface IRabbitQueue {
    Queue getQueue();
}
