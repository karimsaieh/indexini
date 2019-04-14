package tn.insat.pfe.searchservice.mq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.mq.Constants;
import tn.insat.pfe.searchservice.mq.payloads.FileIndexPayload;
import tn.insat.pfe.searchservice.services.ISearchService;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILES_INDEX_QUEUE)
public class FileIndexConsumer implements IRabbitConsumer{
    private static final Logger logger = LoggerFactory.getLogger(FileIndexConsumer.class);
    private String logMsg;
    private final ISearchService searchService;

    @Autowired
    public FileIndexConsumer(ISearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitHandler
    @Override
    public void consume(byte[] in) throws IOException {
        FileIndexPayload fileIndexPayload = (FileIndexPayload) JsonUtils.jsonStringToObject(new String(in), FileIndexPayload.class);
        this.logMsg = fileIndexPayload.getId();
        logger.info(this.logMsg);
        this.searchService.upsertFileIndex(fileIndexPayload);
    }

    @Override
    public void consume(String in) throws IOException {
        this.logMsg = String.format("I'im in file index consumer (String in ) %n %s", in);
        logger.info(this.logMsg);
    }
}
