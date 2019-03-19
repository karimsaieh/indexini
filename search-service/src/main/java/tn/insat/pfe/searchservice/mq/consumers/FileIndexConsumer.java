package tn.insat.pfe.searchservice.mq.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.services.ISearchService;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILES_INDEX_QUEUE)
public class FileIndexConsumer implements IRabbitConsumer{

    private final ISearchService searchService;
    @Autowired
    public FileIndexConsumer(ISearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitHandler
    @Override
    public void consume(byte[] in) throws IOException {
        System.out.println(new String(in));
        ObjectMapper mapper = new ObjectMapper();
        FileIndexPayload fileIndexPayload = mapper.readValue(new String(in), FileIndexPayload.class);
        System.out.println(fileIndexPayload.getId());
        this.searchService.upsertFileIndex(fileIndexPayload);
    }
}
