package tn.insat.pfe.searchservice.mq.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.clients.IRedisClient;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.services.ISearchService;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILES_INDEX_QUEUE)
public class FileIndexConsumer implements IRabbitConsumer{

    private final ISearchService searchService;
    private final IRedisClient redisClient;

    @Autowired
    public FileIndexConsumer(ISearchService searchService, IRedisClient redisClient) {
        this.searchService = searchService;
        this.redisClient = redisClient;
    }

    @RabbitHandler
    @Override
    public void consume(byte[] in) throws IOException {
        System.out.println(new String(in));
        FileIndexPayload fileIndexPayload = (FileIndexPayload) JsonUtils.jsonStringToObject(new String(in), FileIndexPayload.class);
        System.out.println(fileIndexPayload.getId());
        this.searchService.upsertFileIndex(fileIndexPayload);
        this.redisClient.deleteAll();
    }

    @Override
    public void consume(String in) throws IOException {
        System.out.println("I'im in file index consumer (String in ) \n" + in);
    }
}
