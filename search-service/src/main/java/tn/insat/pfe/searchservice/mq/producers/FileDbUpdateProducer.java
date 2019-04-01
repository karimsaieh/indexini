package tn.insat.pfe.searchservice.mq.producers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.queues.FileDbUpdateQueue;

import javax.annotation.PostConstruct;

@Component
@Qualifier("FileDbUpdateProducer")
public class FileDbUpdateProducer implements  IRabbitProducer{

    private final FileDbUpdateQueue fileDbUpdateQueue;
    private final RabbitTemplate rabbitTemplate;
    private Queue queue;

    @Autowired
    public FileDbUpdateProducer(FileDbUpdateQueue fileDbUpdateQueue, RabbitTemplate rabbitTemplate) {
        this.fileDbUpdateQueue = fileDbUpdateQueue;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        this.queue = this.fileDbUpdateQueue.getQueue();
    }

    @Override
    public void produce(String payload) {
        this.rabbitTemplate.convertAndSend(this.queue.getName(),payload);
    }
}
