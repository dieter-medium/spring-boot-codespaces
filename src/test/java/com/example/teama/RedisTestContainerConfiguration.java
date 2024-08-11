package com.example.teama;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface RedisTestContainerConfiguration {
    @Container
    @ServiceConnection("redis")
    RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:latest"))
                    .withCommand("redis-server", "--loglevel", "verbose")
                    .withLabel("project", "teama")
                    .withReuse(true);
}
