package tn.insat.pfe.filemanagementservice.mq.consumers;

import java.io.IOException;

public interface IRabbitConsumer {

    void consume(byte[] in) throws IOException;
    void consume(String in) throws IOException;
}
