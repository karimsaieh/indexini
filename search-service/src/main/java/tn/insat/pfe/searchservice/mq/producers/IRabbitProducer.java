package tn.insat.pfe.searchservice.mq.producers;

public interface IRabbitProducer {
    void produce(String payload);
}
