package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Qualifier("WebScrapingQueue")
public class WebScrapingQueue implements IRabbitQueue{

    private Queue queue;

    @PostConstruct
    public void init() {
        this.queue = new Queue("web_scraping_queue");
    }

    public Queue getQueue() {
        return queue;
    }
}
