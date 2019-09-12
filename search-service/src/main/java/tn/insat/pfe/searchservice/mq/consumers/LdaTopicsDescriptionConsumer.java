package tn.insat.pfe.searchservice.mq.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.LdaTopicsDescriptionPayload;
import tn.insat.pfe.searchservice.services.ISearchService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RabbitListener(queues = Constants.LDA_TOPICS_DESCRIPTION_QUEUE)
public class LdaTopicsDescriptionConsumer implements IRabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(LdaTopicsDescriptionConsumer.class);
    private String logMsg;
    private final ISearchService searchService;
    @Autowired
    public LdaTopicsDescriptionConsumer(ISearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitHandler
    @Override
    public void consume(byte[] in) throws IOException {
        this.logMsg = new String(in, StandardCharsets.UTF_8);
        logger.info(this.logMsg);
        ObjectMapper mapper = new ObjectMapper();
        List<LdaTopicsDescriptionPayload> ldaTopicsDescriptionPayloadList= mapper.readValue(new String(in),
                mapper.getTypeFactory().constructCollectionType(List.class, LdaTopicsDescriptionPayload.class));
        this.logMsg = ldaTopicsDescriptionPayloadList.toString();
        logger.info(this.logMsg);
        this.searchService.upsertLdaTopicsDescription(ldaTopicsDescriptionPayloadList);
    }

    @Override
    public void consume(String in) throws IOException {
        this.logMsg = "I'im in LdaTopicsDescriptionConsumer (String in ) \n" + in;
        logger.info(this.logMsg);
    }
}
