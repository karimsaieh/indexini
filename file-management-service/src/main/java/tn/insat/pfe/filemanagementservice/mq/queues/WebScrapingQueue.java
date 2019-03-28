package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;

import javax.annotation.PostConstruct;

@Component
@Qualifier("WebScrapingQueue")
public class WebScrapingQueue implements IRabbitQueue{

    private Queue queue;

    @PostConstruct
    public void init() {
        this.queue = new Queue(Constants.WEB_SCRAPING_QUEUE);
    }

    public Queue getQueue() {
        return queue;
    }
}
