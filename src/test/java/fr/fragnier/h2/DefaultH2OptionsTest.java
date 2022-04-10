package fr.fragnier.h2;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(classes = JdbcTemplateAutoConfiguration.class)
public class DefaultH2OptionsTest {

    @Autowired
    private H2Container<?> h2Container;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Verify that h2 server is started with expected version")
    public void test() throws SQLException {
        assertThat(h2Container.getJdbcUrl()).isEqualTo(jdbcTemplate.getDataSource().getConnection().getMetaData().getURL());

        String h2ServerVersion = jdbcTemplate.queryForObject(
                "select setting_value from information_schema.settings where setting_name = 'info.VERSION'",
                String.class);
        assertThat(h2ServerVersion).startsWith("2.1.210");
    }

    @Configuration
    public static class Conf {
        @Bean
        H2Container<?> h2Container() {
            H2Container<?> h2Container = new H2Container<>();
            h2Container.start();
            return h2Container;
        }

        @Bean
        DataSource dataSource(H2Container<?> h2Container) {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(h2Container.getJdbcUrl());
            dataSource.setUsername(h2Container.getUsername());
            dataSource.setPassword(h2Container.getPassword());
            return dataSource;
        }
    }
}
