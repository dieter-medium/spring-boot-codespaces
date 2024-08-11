package com.example.teama.config;

import com.example.teama.config.docker.compose.core.RunningServiceWithFixedPorts;
import com.example.teama.config.docker.compose.service.connection.mariadb.MariaDbJdbcDockerComposeConnectionDetailsFactory;
import com.example.teama.config.docker.compose.service.connection.redis.RedisDockerComposeConnectionDetailsFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.boot.docker.compose.core.DockerCompose;
import org.springframework.boot.docker.compose.core.DockerComposeFile;
import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "spring.docker.compose.devcontainer.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class DevcontainerComposeConfig {
    private List<DockerComposeConnectionSource> connectionSourceList;

    private static DockerComposeConnectionSource getDockerComposeConnectionSource(RunningService runningService) {
        try {
            Constructor<DockerComposeConnectionSource> constructor =
                    DockerComposeConnectionSource.class.getDeclaredConstructor(RunningService.class);
            constructor.setAccessible(true);
            return constructor.newInstance(new RunningServiceWithFixedPorts(runningService));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            String errorMessage = "Failed to create DockerComposeConnectionSource instance";
            log.error(errorMessage, e);

            throw new RuntimeException(errorMessage, e);
        }
    }

    private List<DockerComposeConnectionSource> getConnectionSourceList(DockerCompose dockerCompose) {
        if (connectionSourceList == null) {
            connectionSourceList = dockerCompose.getRunningServices().stream()
                    .map(DevcontainerComposeConfig::getDockerComposeConnectionSource)
                    .toList();
        }

        return connectionSourceList;
    }

    private <T extends ConnectionDetails> List<T> getConnectionDetails(DockerCompose dockerCompose, DockerComposeConnectionDetailsFactory<T> factory) {
        return getConnectionSourceList(dockerCompose).stream()
                .map(factory::getConnectionDetails)
                .filter(Objects::nonNull)
                .toList();
    }

    @Bean
    public DockerCompose dockerCompose() {
        return DockerCompose.get(
                DockerComposeFile.find(new File(".")),
                null,
                new HashSet<String>()
        );
    }

    @Bean
    @Primary
    public JdbcConnectionDetails jdbcConnectionDetails(DockerCompose dockerCompose) {
        var factory = new MariaDbJdbcDockerComposeConnectionDetailsFactory();
        var connectionDetails = getConnectionDetails(dockerCompose, factory);

        return getFirstConnectionDetail(connectionDetails, "MariaDB");
    }

    @Bean
    @Primary
    public RedisConnectionDetails redisConnectionDetails(DockerCompose dockerCompose) {
        var factory = new RedisDockerComposeConnectionDetailsFactory();
        var connectionDetails = getConnectionDetails(dockerCompose, factory);

        return getFirstConnectionDetail(connectionDetails, "Redis");
    }

    private <T extends ConnectionDetails> T getFirstConnectionDetail(List<T> connectionDetails, String serviceName) {
        return Optional.ofNullable(connectionDetails)
                .filter(details -> !details.isEmpty())
                .map(details -> details.get(0))
                .orElseThrow(() -> {
                    String errorMessage = String.format("No connection details found for %s service", serviceName);
                    log.error(errorMessage);
                    return new IllegalStateException(errorMessage);
                });
    }
}
