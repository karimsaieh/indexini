package tn.insat.pfe.searchservice.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ElasticSearchProvider implements IElasticSearchProvider {

    @Value("${elasticsearch.hostname}")
    private String hostname;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.index}")
    private String index;
    @Value("${elasticsearch.type}")
    private String type;

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

    public String getIndex() {
        return this.index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
