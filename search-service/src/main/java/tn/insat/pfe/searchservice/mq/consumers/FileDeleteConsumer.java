package tn.insat.pfe.searchservice.mq.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import tn.insat.pfe.searchservice.clients.IRedisClient;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.FileDeletePayload;
import tn.insat.pfe.searchservice.services.ISearchService;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILE_DELETE_QUEUE)
public class FileDeleteConsumer implements IRabbitConsumer {

    private final ISearchService searchService;
    private final IRedisClient redisClient;
    @Autowired
    public FileDeleteConsumer(ISearchService searchService, IRedisClient redisClient) {
        this.searchService = searchService;
        this.redisClient = redisClient;
    }

    @Override
    @RabbitHandler
    public void consume(byte[] in) throws IOException {
        System.out.println("I'im in FileDeleteConsumer consumer (byte[] in ) \n" + in);
    }

    @Override
    @RabbitHandler
    public void consume(String in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileDeletePayload fileDeletePayload= mapper.readValue(in, FileDeletePayload.class);
        System.out.println(fileDeletePayload);
        this.searchService.delete(fileDeletePayload);
        this.redisClient.deleteAll();
    }
}
