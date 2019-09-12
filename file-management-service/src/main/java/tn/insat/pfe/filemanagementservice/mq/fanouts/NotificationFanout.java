package tn.insat.pfe.filemanagementservice.mq.fanouts;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;

import javax.annotation.PostConstruct;

@Component
@Qualifier("NotificationFanout")
public class NotificationFanout implements IRabbitFanout {
    private FanoutExchange fanoutExchange;

    @PostConstruct
    public void init() {
        this.fanoutExchange = new FanoutExchange(Constants.NOTIFICATIONS_EXCHANGE);

    }
    @Override
    public FanoutExchange getFanout() {
        return this.fanoutExchange;
    }
}
