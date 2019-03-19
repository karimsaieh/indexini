package tn.insat.pfe.searchservice.mq.consumers;

import java.io.IOException;

public interface IRabbitConsumer {

    void consume(byte[] in) throws IOException;

}