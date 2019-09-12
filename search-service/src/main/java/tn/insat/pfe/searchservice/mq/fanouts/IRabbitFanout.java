package tn.insat.pfe.searchservice.mq.fanouts;

import org.springframework.amqp.core.FanoutExchange;

public interface IRabbitFanout {
    FanoutExchange getFanout();
}
