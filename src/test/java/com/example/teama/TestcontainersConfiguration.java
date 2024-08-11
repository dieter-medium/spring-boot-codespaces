package com.example.teama;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers({MariaDBTestContainerConfiguration.class, RedisTestContainerConfiguration.class})
public class TestcontainersConfiguration {

    @Bean(destroyMethod = "stop")
    MariaDBContainer<?> mariaDbContainer() {
        return MariaDBTestContainerConfiguration.mariaDbContainer;
    }

}
