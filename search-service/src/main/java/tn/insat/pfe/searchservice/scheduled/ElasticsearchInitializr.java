package tn.insat.pfe.searchservice.scheduled;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.insat.pfe.searchservice.services.ISearchService;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Component
public class ElasticsearchInitializr {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchInitializr.class);
    @Autowired
    private ISearchService searchService;

    // it's okay if each instance of the search-ms reinitialise ES
    // it won't change anything
    // N.B => everything is okay as long as the first ever initialisation happens by this method and
    // and not by an indexing process => Cause there will be type conflict
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws IOException, InterruptedException {
        Boolean initialised = false;
        while(!initialised) {
            logger.info("trying to set ES Mapping");
            try{
                initialised = this.searchService.initEsMapping();
            }catch (Exception ex){
                logger.warn("it seems that ES isn't ready yet offline", ex);
            }
            if(!initialised){
                TimeUnit.SECONDS.sleep(1);
            }
        }

    }

}
