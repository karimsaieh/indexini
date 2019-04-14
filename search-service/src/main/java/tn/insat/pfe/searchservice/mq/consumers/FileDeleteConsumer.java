package tn.insat.pfe.searchservice.mq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.FileDeletePayload;
import tn.insat.pfe.searchservice.services.ISearchService;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RabbitListener(queues = Constants.FILE_DELETE_QUEUE)
public class FileDeleteConsumer implements IRabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(FileDeleteConsumer.class);
    private String logMsg;
    private final ISearchService searchService;
    @Autowired
    public FileDeleteConsumer(ISearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    @RabbitHandler
    public void consume(byte[] in) throws IOException {
        this.logMsg = String.format("I'im in FileDeleteConsumer consumer (byte[] in ) %n %s", new String(in, StandardCharsets.UTF_8));
        logger.info(this.logMsg);
    }

    @Override
    @RabbitHandler
    public void consume(String in) throws IOException {
        FileDeletePayload fileDeletePayload= (FileDeletePayload)JsonUtils.jsonStringToObject(in, FileDeletePayload.class);
        this.logMsg = fileDeletePayload.toString();
        logger.info(this.logMsg);
        this.searchService.delete(fileDeletePayload);
    }
}
