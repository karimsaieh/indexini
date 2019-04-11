package tn.insat.pfe.filemanagementservice.mq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileFoundPayload;
import tn.insat.pfe.filemanagementservice.services.IFileService;
import tn.insat.pfe.filemanagementservice.utils.JsonUtils;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILES_FOUND_QUEUE)
public class FilesFoundConsumer implements IRabbitConsumer{
    private static final Logger logger = LoggerFactory.getLogger(FilesFoundConsumer.class);
    private String logMsg;
    private final IFileService fileService;

    @Autowired
    public FilesFoundConsumer(IFileService fileService) {
        this.fileService = fileService;
    }

    @RabbitHandler
    public void consume(byte[] in) throws IOException {

        FileFoundPayload fileFoundPayload = (FileFoundPayload) JsonUtils.jsonStringToObject(new String(in), FileFoundPayload.class);
        this.logMsg = fileFoundPayload.toString();
        logger.info(logMsg);
        logger.info("ho");
        this.fileService.downloadAndSaveFile(fileFoundPayload);
        logger.info("yo");
    }

    @Override
    public void consume(String in) throws IOException {
        this.logMsg = "I'im in FilesFoundConsumer consumer (String in ) \n" + in;
        logger.info(logMsg);
    }

}
