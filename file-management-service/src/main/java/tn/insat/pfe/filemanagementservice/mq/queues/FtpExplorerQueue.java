package tn.insat.pfe.filemanagementservice.mq.queues;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;

import javax.annotation.PostConstruct;

@Component
@Qualifier("FtpExplorerQueue")
public class FtpExplorerQueue implements IRabbitQueue{

    private Queue queue;

    @PostConstruct
    public void init() {
        this.queue = new Queue(Constants.FTP_EXPLORER_QUEUE, false);
    }

    public Queue getQueue() {
        return queue;
    }
}
