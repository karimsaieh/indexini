package tn.insat.pfe.filemanagementservice.mq.topics;

import org.springframework.amqp.core.TopicExchange;

public interface IRabbitTopic {
    TopicExchange getTopic();
}
