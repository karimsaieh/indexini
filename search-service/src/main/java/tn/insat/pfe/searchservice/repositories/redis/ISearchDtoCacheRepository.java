package tn.insat.pfe.searchservice.repositories.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.insat.pfe.searchservice.entities.redis.SearchDtoCache;

@Repository
public interface ISearchDtoCacheRepository extends CrudRepository<SearchDtoCache, String> {
}
