package com.scu.config;

import com.scu.util.DynamicTableBuilder;
import com.scu.util.TableOpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public DynamicTableBuilder dynamicTableBuilder(JdbcTemplate jdbcTemplate) {
        return new DynamicTableBuilder(jdbcTemplate);
    }
    @Bean
    public TableOpUtil tableOpUtil() {
        return new TableOpUtil();
    }



}
