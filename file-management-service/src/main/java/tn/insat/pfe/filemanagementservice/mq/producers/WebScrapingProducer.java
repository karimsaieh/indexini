package tn.insat.pfe.filemanagementservice.mq.producers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.queues.IRabbitQueue;

import javax.annotation.PostConstruct;

@Component
@Qualifier("WebScrapingProducer")
public class WebScrapingProducer implements IRabbitProducer{

    private final IRabbitQueue webScrapingQueue;
    private final RabbitTemplate rabbitTemplate;
    private Queue queue;
    @Autowired
    public WebScrapingProducer(@Qualifier("WebScrapingQueue") IRabbitQueue webScrapingQueue,
                                RabbitTemplate rabbitTemplate) {
        this.webScrapingQueue = webScrapingQueue;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        this.queue = this.webScrapingQueue.getQueue();
    }

    public void produce(String payload) {
            this.rabbitTemplate.convertAndSend(this.queue.getName(), payload);
    }

}
