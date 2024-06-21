package com.vvsoft.event_sourcing_demo.event.event_repo;

import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class EventRepoConfiguration {

    @Bean
    public DataSource postgresDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost/postgres");
        config.setUsername("postgres");
        config.setPassword("test");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    @Bean
    public EventRepository<ReminderEvent> addReminderEventRepository(JdbcTemplate jdbcTemplate){
        return new EventRepository<>(jdbcTemplate);
    }
}
