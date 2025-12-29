package com.scu.config;

import com.scu.util.TableOperator;
import com.scu.util.GridFsUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {
    @Bean
    public TableOperator dynamicTableBuilder(JdbcTemplate jdbcTemplate) {
        return new TableOperator(jdbcTemplate);
    }

    @Bean
    public GridFsUtils gridFsUtils(MongoTemplate mongoTemplate) {
        return new GridFsUtils(mongoTemplate);
    }

}
