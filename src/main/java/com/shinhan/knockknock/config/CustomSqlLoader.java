package com.shinhan.knockknock.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CustomSqlLoader {

    private final JdbcTemplate jdbcTemplate;
    private final ResourcePatternResolver resourcePatternResolver;

    public CustomSqlLoader(JdbcTemplate jdbcTemplate, ResourcePatternResolver resourcePatternResolver) {
        this.jdbcTemplate = jdbcTemplate;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Bean
    ApplicationRunner loadSqlFiles() {
        return args -> {
            // 실행할 SQL 파일들 리스트
            String[] sqlFiles = {"data.sql", "sql/welfare.sql"};
            for (String sqlFile : sqlFiles) {
                Resource resource = resourcePatternResolver.getResource("classpath:" + sqlFile);
                String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));
                jdbcTemplate.execute(sql);
            }
        };
    }
}