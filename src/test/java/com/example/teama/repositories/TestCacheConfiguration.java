package com.example.teama.repositories;

import com.example.teama.config.CachingConfig;
import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@TestConfiguration
@EnableCaching
@Import(CachingConfig.class)
public class TestCacheConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            RedisContainer redisContainer) {
        return new LettuceConnectionFactory(redisContainer.getHost(),
                redisContainer.getFirstMappedPort());

    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).build();
    }
}
