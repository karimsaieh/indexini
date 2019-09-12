package tn.insat.pfe.searchservice.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ElasticSearchProvider implements IElasticSearchProvider {

    @Value("${pfe_elasticsearch_hostname}")
    private String hostname;
    @Value("${pfe_elasticsearch_port}")
    private int port;

    private RestHighLevelClient restHighLevelClient;

    @PostConstruct
    public void init() {
        this.restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(this.hostname, this.port, "http")
                )
        );
    }
    public RestHighLevelClient getRestHighLevelClient() {
        return this.restHighLevelClient;
    }
}
