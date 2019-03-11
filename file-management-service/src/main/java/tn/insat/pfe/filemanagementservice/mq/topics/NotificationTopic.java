package tn.insat.pfe.filemanagementservice.mq.topics;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.queues.IRabbitQueue;

import javax.annotation.PostConstruct;

@Component
@Qualifier("NotificationTopic")
public class NotificationTopic implements IRabbitTopic {
    private TopicExchange topicExchange;

    @PostConstruct
    public void init() {
        this.topicExchange = new TopicExchange("notifications_exchange");
    }


    @Override
    public TopicExchange getTopic() {
        return this.topicExchange;
    }
}
