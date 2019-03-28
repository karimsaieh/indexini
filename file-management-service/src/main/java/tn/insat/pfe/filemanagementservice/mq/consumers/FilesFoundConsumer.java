package tn.insat.pfe.filemanagementservice.mq.consumers;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.Constants;
import tn.insat.pfe.filemanagementservice.mq.payloads.FileFoundPayload;
import tn.insat.pfe.filemanagementservice.services.FileService;

import java.io.IOException;

@Component
@RabbitListener(queues = Constants.FILES_FOUND_QUEUE)
public class FilesFoundConsumer implements IRabbitConsumer{

    private final FileService fileService;

    @Autowired
    public FilesFoundConsumer(FileService fileService) {
        this.fileService = fileService;
    }

    @RabbitHandler
    public void consume(byte[] in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileFoundPayload fileFoundPayload = mapper.readValue(new String(in), FileFoundPayload.class);
        System.out.println(fileFoundPayload);
        this.fileService.downloadAndSaveFile(fileFoundPayload);
    }

}
