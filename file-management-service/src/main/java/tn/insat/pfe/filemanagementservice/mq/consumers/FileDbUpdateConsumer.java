package tn.insat.pfe.filemanagementservice.mq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileDbUpdatePayload;
import tn.insat.pfe.filemanagementservice.services.IFileService;
import tn.insat.pfe.filemanagementservice.utils.JsonUtils;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILE_DB_UPDATE_QUEUE)
public class FileDbUpdateConsumer implements  IRabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(FileDbUpdateConsumer.class);
    private final IFileService fileService;

    @Autowired
    public FileDbUpdateConsumer(IFileService fileService) {
        this.fileService = fileService;
    }

    @Override
    @RabbitHandler
    public void consume(byte[] in) throws IOException {
        String msg = String.format("I'im in FileDbUpdateConsumer consumer (byte[] in ) %n  %s", new String(in));
        logger.info(msg);
    }

    @Override
    @RabbitHandler
    public void consume(String in) throws IOException {
        FileDbUpdatePayload fileDbUpdatePayload = (FileDbUpdatePayload) JsonUtils.jsonStringToObject(in, FileDbUpdatePayload.class);
        this.fileService.updateFile(fileDbUpdatePayload);
    }
}
