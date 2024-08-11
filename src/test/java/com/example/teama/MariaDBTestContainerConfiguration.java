package com.example.teama;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.Map;

public interface MariaDBTestContainerConfiguration {
    @Container
    @ServiceConnection
    MariaDBContainer<?> mariaDbContainer =
            new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"))
                    // .withInitScript("sql/tweak-mariadb-settings.sql") does not work is run after the container is started with an unprivileged user
                    .withCopyFileToContainer(
                            MountableFile.forClasspathResource("sql/tweak-mariadb-settings.sql"),
                            "/docker-entrypoint-initdb.d/tweak-mariadb-settings.sql")
                    .withLabel("project", "teama")
                    .withReuse(true)
                    .withDatabaseName("test-teama")
                    .withUsername("test")
                    .withPassword("test")
                    .withTmpFs(Map.of("/var/lib/mysql", "rw"));
}
