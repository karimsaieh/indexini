package tn.insat.pfe.searchservice.mq.producers;


import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.fanouts.IRabbitFanout;

import javax.annotation.PostConstruct;

@Component
@Qualifier("NotificationProducer")
public class NotificationProducer implements  IRabbitProducer{
    private final IRabbitFanout notificationFanout;
    private final RabbitTemplate rabbitTemplate;
    private FanoutExchange fanout;
    @Autowired
    public NotificationProducer(@Qualifier("NotificationFanout") IRabbitFanout notificationFanout,
                                RabbitTemplate rabbitTemplate){
        this.notificationFanout = notificationFanout;
        this.rabbitTemplate = rabbitTemplate;
    }
    @PostConstruct
    public void init(){
        this.fanout = this.notificationFanout.getFanout();
    }

    public void produce(String payload) {
        this.rabbitTemplate.convertAndSend(this.fanout.getName(),"", payload);
    }
}
