package tn.insat.pfe.searchservice.mq.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;
import tn.insat.pfe.searchservice.services.ISearchService;

import java.io.IOException;
import java.util.List;

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
        System.out.println(new String(in));
        ObjectMapper mapper = new ObjectMapper();
        List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList= mapper.readValue(new String(in),
                mapper.getTypeFactory().constructCollectionType(List.class, LdaTopicsDescriptionPayload.class));
        System.out.println(ldaTopicsDescriptionPayloadList);
        this.searchService.upsertLdaTopicsDescription(ldaTopicsDescriptionPayloadList);
    }

    @Override
    public void consume(String in) throws IOException {
        System.out.println("I'im in LdaTopicsDescriptionConsumer (String in ) \n" + in);
    }
}
