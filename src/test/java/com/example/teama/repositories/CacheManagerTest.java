package com.example.teama.repositories;

import com.example.teama.RedisTestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestCacheConfiguration.class})
@ImportTestcontainers({RedisTestContainerConfiguration.class})
@Testcontainers
@ActiveProfiles("test")
public class CacheManagerTest {

    @Autowired
    private CacheManager cacheManager;


    @Test
    @DisplayName("it uses Redis as cache manager")
    public void itUsesRedis() {
        assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);

        Cache cache = cacheManager.getCache("blogPosts");
        assertThat(cache).isInstanceOf(RedisCache.class);
    }
}