package tn.insat.pfe.filemanagementservice.mq.producers;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.topics.IRabbitTopic;

import javax.annotation.PostConstruct;

@Component
@Qualifier("NotificationProducer")
public class NotificationProducer implements  IRabbitProducer{
    private final IRabbitTopic notificationTopic;
    private final RabbitTemplate rabbitTemplate;
    private TopicExchange topic;
    @Autowired
    public NotificationProducer(@Qualifier("NotificationTopic") IRabbitTopic notificationTopic,
                                RabbitTemplate rabbitTemplate){
        this.notificationTopic = notificationTopic;
        this.rabbitTemplate = rabbitTemplate;
    }
    @PostConstruct
    public void init(){
        this.topic = this.notificationTopic.getTopic();
    }

    @Override
    public void produce(String payload) {
        throw new UnsupportedOperationException("NotificationProducer.produce(String payload)");
    }

    @Override
    public void produce(String routingKey, String payload) {
        this.rabbitTemplate.convertAndSend(this.topic.getName(), routingKey, payload);
    }
}
