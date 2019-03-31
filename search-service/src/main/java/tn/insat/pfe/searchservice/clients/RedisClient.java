package tn.insat.pfe.searchservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Component
public class RedisClient implements IRedisClient {
    @Value("${redis.hostname}")
    protected String redisHostname;

    @Override
    public void deleteAll() {
        try {
            // got problems when using a single connection (broken pipe) & got no time to find a solution
            //this is temporary
            Jedis jedis = new Jedis(this.redisHostname);
            jedis.flushDB();
            jedis.close();
        }catch(Exception ex){
//            ex.printStackTrace();
            System.out.println("redis thrown an exception: RedisClient.RedisClient");
        }
    }
}
