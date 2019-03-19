package tn.insat.pfe.searchservice.mq.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.services.ISearchService;

import java.io.IOException;

//TODO: override existing topics
@Component
@RabbitListener(queues = Constants.LDA_TOPICS_DESCRIPTION_QUEUE)
public class LdaTopicsDescriptionConsumer implements IRabbitConsumer {
    private final ISearchService searchService;
    @Autowired
    public LdaTopicsDescriptionConsumer(ISearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitHandler
    @Override
    public void consume(byte[] in) throws IOException {

    }
}
