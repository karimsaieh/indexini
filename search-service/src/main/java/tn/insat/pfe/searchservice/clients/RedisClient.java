package tn.insat.pfe.searchservice.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


@Component
public class RedisClient implements IRedisClient {
    private static final Logger logger= LoggerFactory.getLogger(RedisClient.class);
    @Value("${pfe_redis_hostname}")
    protected String redisHostname;

    @Override
    public void deleteAll() {
        try {
            // got problems when using a single connection (broken pipe) & got no time to find a solution
            //this is temporary
            try(Jedis jedis = new Jedis(this.redisHostname)) {
                jedis.flushDB();
            }
        }catch(Exception ex){
            logger.error("redisClient thrown an exception in delete all", ex);
        }
    }
}
