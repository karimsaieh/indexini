package tn.insat.pfe.searchservice.services.fallbacks;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.insat.pfe.searchservice.dtos.SearchDto;
import tn.insat.pfe.searchservice.entities.redis.SearchDtoCache;
import tn.insat.pfe.searchservice.repositories.redis.ISearchDtoCacheRepository;
import tn.insat.pfe.searchservice.services.fallbacks.utils.RedisUtils;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Service
public class SearchServiceCacheFallback extends SearchServiceSilentFallback {

    protected String keyPrefix;

    @Autowired
    private ISearchDtoCacheRepository searchDtoCacheRepository;

    @PostConstruct
    public void init() {
        this.keyPrefix = "SearchService";
    }

    @HystrixCommand(fallbackMethod = "silentFind") //it should be called when redis fails
    public SearchDto cachedFind(String query, Pageable pageable) throws IOException {
        String key = RedisUtils.generateKey(new String[]{this.keyPrefix,"find",query}, pageable);
        Optional<SearchDtoCache> searchDtoCache = this.searchDtoCacheRepository.findById(key);
        if(searchDtoCache.orElse(null) ==  null) {
            return this.silentFind(query, pageable);
        } else {
            return searchDtoCache.get().getSearchDto();
        }
    }
}
