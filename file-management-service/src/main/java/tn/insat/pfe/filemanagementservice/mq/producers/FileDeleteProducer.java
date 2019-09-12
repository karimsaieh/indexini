package tn.insat.pfe.filemanagementservice.mq.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;

import tn.insat.pfe.filemanagementservice.mq.queues.IRabbitQueue;

import javax.annotation.PostConstruct;

@Component
@Qualifier("FileDeleteProducer")
public class FileDeleteProducer implements IRabbitProducer{

    private final IRabbitQueue fileDeleteQueue;
    private final RabbitTemplate rabbitTemplate;
    private Queue queue;

    @Autowired
    public FileDeleteProducer(@Qualifier("FileDeleteQueue") IRabbitQueue fileDeleteQueue,
                               RabbitTemplate rabbitTemplate) {
        this.fileDeleteQueue = fileDeleteQueue;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        this.queue = this.fileDeleteQueue.getQueue();
    }

    @Override
    public void produce(String payload) {
        this.rabbitTemplate.convertAndSend(this.queue.getName(), payload);
    }
}