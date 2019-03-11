package tn.insat.pfe.filemanagementservice.mq.producers;

public interface IRabbitProducer {

    void produce(String payload);
    void produce(String routingKey, String payload);

}
