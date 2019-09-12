package tn.insat.pfe.filemanagementservice.mq.producers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.queues.IRabbitQueue;

import javax.annotation.PostConstruct;

@Component
@Qualifier("FtpExplorerProducer")
public class FtpExplorerProducer implements IRabbitProducer{

    private final IRabbitQueue ftpExplorerQueue;
    private final RabbitTemplate rabbitTemplate;
    private Queue queue;
    @Autowired
    public FtpExplorerProducer(@Qualifier("FtpExplorerQueue") IRabbitQueue ftpExplorerQueue,
                               RabbitTemplate rabbitTemplate) {
        this.ftpExplorerQueue = ftpExplorerQueue;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        this.queue = this.ftpExplorerQueue.getQueue();
    }

    @Override
    public void produce(String payload) {
        this.rabbitTemplate.convertAndSend(this.queue.getName(), payload);
    }

}
