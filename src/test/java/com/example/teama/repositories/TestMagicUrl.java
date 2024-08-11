package com.example.teama.repositories;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(scripts = {"classpath:/sql/seed.sql"})
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:mariadb:10.3.39:///test-db"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestMagicUrl {
    @Autowired
    private TestEntityManager testEntityManager;

    public String getDatabaseUrl(EntityManager entityManager) throws SQLException {
        Session session = entityManager.unwrap(Session.class);
        final String[] url = new String[1];
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                DatabaseMetaData metaData = connection.getMetaData();
                url[0] = metaData.getURL();
            }
        });
        return url[0];
    }

    @Test
    @DisplayName("it populates the jdbc url")
    public void itUsesTheMagicUrl() throws SQLException {
        String url = getDatabaseUrl(testEntityManager.getEntityManager());
        String expectedHostname = System.getenv().getOrDefault("TESTCONTAINERS_HOST_OVERRIDE", "localhost");

        assertThat(url).startsWith("jdbc:mariadb://" + expectedHostname + ":");
    }

}
