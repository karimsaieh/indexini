package tn.insat.pfe.searchservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Value("${pfe_redis_hostname}")
    private String hostname;

    @Bean
    public Jedis getJedis() {
        return new Jedis(this.hostname);
    }

}
