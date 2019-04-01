package tn.insat.pfe.filemanagementservice.mq.consumers;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileDbUpdatePayload;
import tn.insat.pfe.filemanagementservice.services.IFileService;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILE_DB_UPDATE_QUEUE)
public class FileDbUpdateConsumer implements  IRabbitConsumer {

    private final IFileService fileService;

    @Autowired
    public FileDbUpdateConsumer(IFileService fileService) {
        this.fileService = fileService;
    }

    @Override
    @RabbitHandler
    public void consume(byte[] in) throws IOException {
        System.out.println("I'im in FileDbUpdateConsumer consumer (byte[] in ) \n" + in);
    }

    @Override
    @RabbitHandler
    public void consume(String in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileDbUpdatePayload fileDbUpdatePayload = mapper.readValue(in, FileDbUpdatePayload.class);
        this.fileService.updateFile(fileDbUpdatePayload);
    }
}
