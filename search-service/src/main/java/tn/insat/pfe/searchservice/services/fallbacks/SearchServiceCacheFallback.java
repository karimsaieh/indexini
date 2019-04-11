package tn.insat.pfe.searchservice.services.fallbacks;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tn.insat.pfe.searchservice.dtos.SearchDto;
import tn.insat.pfe.searchservice.entities.redis.SearchDtoCache;
import tn.insat.pfe.searchservice.services.fallbacks.utils.RedisUtils;
import tn.insat.pfe.searchservice.utils.JsonUtils;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class SearchServiceCacheFallback extends SearchServiceSilentFallback {


    @Value("${pfe_redis_hostname}")
    protected String redisHostname;
    protected String keyPrefix;

    @PostConstruct
    public void init() {
        this.keyPrefix = "SearchService";
    }

    @HystrixCommand(fallbackMethod = "silentFind") //it should be called when redis fails
    public SearchDto cachedFind(String query, Pageable pageable) throws IOException {
        String key = RedisUtils.generateKey(new String[]{this.keyPrefix,"find",query}, pageable);
        Jedis jedis = new Jedis(this.redisHostname);
        String cachedValueString =  jedis.get(key);
        jedis.close();
        if(cachedValueString ==  null) {
            return this.silentFind(query, pageable);
        } else {
            SearchDtoCache searchDtoCache = (SearchDtoCache) JsonUtils.jsonStringToObject(cachedValueString, SearchDtoCache.class);
            return searchDtoCache.getSearchDto();
        }
    }
}
