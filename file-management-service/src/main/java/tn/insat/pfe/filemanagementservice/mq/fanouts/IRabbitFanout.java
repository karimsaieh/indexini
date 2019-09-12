package tn.insat.pfe.filemanagementservice.mq.fanouts;

import org.springframework.amqp.core.FanoutExchange;

public interface IRabbitFanout {
    FanoutExchange getFanout();
}
