package tn.insat.pfe.searchservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.insat.pfe.searchservice.mq.Constants;

@Configuration
public class LdaTopicsDescriptionQueue{
    @Bean
    public Queue getQueue() {
        return new Queue(Constants.LDA_TOPICS_DESCRIPTION_QUEUE, false);
    }
}
