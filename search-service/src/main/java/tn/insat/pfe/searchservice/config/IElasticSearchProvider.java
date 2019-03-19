package tn.insat.pfe.searchservice.config;

import org.elasticsearch.client.RestHighLevelClient;

public interface IElasticSearchProvider {
    RestHighLevelClient getRestHighLevelClient();
}
