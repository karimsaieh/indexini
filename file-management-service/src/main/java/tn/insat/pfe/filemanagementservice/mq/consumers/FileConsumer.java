package tn.insat.pfe.filemanagementservice.mq.consumers;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.insat.pfe.filemanagementservice.mq.payloads.FilePayload;
import tn.insat.pfe.filemanagementservice.services.FileService;

import java.io.IOException;

@Component
@RabbitListener(queues = "files_queue")
public class FileConsumer implements IRabbitConsumer{

    private final FileService fileService;

    @Autowired
    public FileConsumer(FileService fileService) {
        this.fileService = fileService;
    }

    @RabbitHandler
    public void consume(byte[] in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FilePayload filePayload = mapper.readValue(new String(in), FilePayload.class);
        System.out.println(filePayload);
        this.fileService.downloadAndSaveFile(filePayload);
    }

}
