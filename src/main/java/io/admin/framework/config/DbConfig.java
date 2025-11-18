package io.admin.framework.config;


import io.admin.common.utils.DbUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;

@Configuration
@EnableJpaAuditing
public class DbConfig {

    @Bean
    @ConditionalOnMissingBean(value = DbUtils.class)
    public DbUtils dbUtils(DataSource dataSource){
        return new DbUtils(dataSource);
    }
}
