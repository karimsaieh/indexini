package tn.insat.pfe.searchservice.mq.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "file_index_queue")
public class FileIndexConsumer implements IRabbitConsumer{

    @Autowired
    public FileIndexConsumer() {

    }

    @RabbitHandler
    @Override
    public void consume(byte[] in) throws IOException {
        System.out.println(new String(in));
    }
}
