package tn.insat.pfe.searchservice.services.fallbacks;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.LdaTopicsDescriptionGetDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;
import tn.insat.pfe.searchservice.entities.redis.SearchDtoCache;
import tn.insat.pfe.searchservice.services.fallbacks.utils.RedisUtils;
import tn.insat.pfe.searchservice.utils.JsonUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceCacheFallback extends SearchServiceSilentFallback {


    @Value("${redis.hostname}")
    protected String redisHostname;
    @Autowired
    protected Jedis jedis;
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
            return this.silentFind(query,pageable);
        } else {
            SearchDtoCache searchDtoCache = (SearchDtoCache) JsonUtils.jsonStringToObject(cachedValueString, SearchDtoCache.class);
            return searchDtoCache.getSearchDto();
        }
    }


//    @HystrixCommand(fallbackMethod = "silentFindBy")
//    public Page<FileGetDto> cachedFindBy(String by, String value, Pageable pageable) throws IOException {
//        String key = RedisUtils.generateKey(new String[]{this.keyPrefix,"findBy", by, value}, pageable);
//        String cachedValueString =  jedis.get(key);
//        if(cachedValueString ==  null) {
//            return this.silentFindBy(null,null,null);
//        } else {
//            return (Page<FileGetDto>) JsonUtils.jsonStringToObject(cachedValueString, Page.class);
//        }
//    }
//
//    @HystrixCommand(fallbackMethod = "silentFindById")
//    public FileGetDto cachedFindById(String id) throws IOException {
//        String key = RedisUtils.generateKey(new String[]{this.keyPrefix,"findById", id});
//        String cachedValueString =  jedis.get(key);
//        if(cachedValueString ==  null) {
//            return this.silentFindById(null);
//        } else {
//            return (FileGetDto) JsonUtils.jsonStringToObject(cachedValueString, FileGetDto.class);
//        }
//    }
//
//    @HystrixCommand(fallbackMethod = "silentFindAllSortBy")
//    public Page<FileGetDto> cachedFindAllSortBy(String sortBy, Pageable pageable) throws IOException {
//        String key = RedisUtils.generateKey(new String[]{this.keyPrefix,"findAllSortBy",  sortBy}, pageable);
//        String cachedValueString =  jedis.get(key);
//        if(cachedValueString ==  null) {
//            return this.silentFindAllSortBy(null,null);
//        } else {
//            return (Page<FileGetDto>) JsonUtils.jsonStringToObject(cachedValueString, Page.class);
//        }
//    }
//
//    @HystrixCommand(fallbackMethod = "silentGetLdaTopics")
//    public List<LdaTopicsDescriptionGetDto> cachedGetLdaTopics() throws IOException {
//        String key = RedisUtils.generateKey(new String[]{this.keyPrefix, "getLdaTopics"});
//        String cachedValueString =  jedis.get(key);
//        if(cachedValueString ==  null) {
//            return this.silentGetLdaTopics();
//        } else {
//            return (List<LdaTopicsDescriptionGetDto>) JsonUtils.jsonStringToObject(cachedValueString, List.class);
//        }
//    }
}
