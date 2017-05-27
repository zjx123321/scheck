package com.simple.scheck.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PreDestroy;

/**
 * Created by dell on 2017/5/27.
 */
@Configuration
@EnableConfigurationProperties({ DataBaseProperties.class })
@MapperScan(basePackages = { "com.kashuo.backend.dao" })
public class MybatisConfiguration {
    @Autowired
    private DataBaseProperties dataSourceProperties;

    private DataSource dataSource;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DataBaseProperties config = this.dataSourceProperties;

        this.dataSource = new DataSource();

        this.dataSource.setDriverClassName(config.getDriverClassName());
        this.dataSource.setUrl(config.getUrl());
        if (config.getUsername() != null) {
            this.dataSource.setUsername(config.getUsername());
        }
        if (config.getPassword() != null) {
            this.dataSource.setPassword(config.getPassword());
        }
        this.dataSource.setInitialSize(config.getInitialSize());
        this.dataSource.setMaxActive(config.getMaxActive());
        this.dataSource.setMaxIdle(config.getMaxIdle());
        this.dataSource.setMinIdle(config.getMinIdle());
        this.dataSource.setTestOnBorrow(config.isTestOnBorrow());
        this.dataSource.setTestOnReturn(config.isTestOnReturn());
        this.dataSource.setValidationQuery(config.getValidationQuery());
        this.dataSource.setTestWhileIdle(config.isTestWhileIdle());
        return this.dataSource;
    }

    @PreDestroy
    public void close() {
        if (this.dataSource != null)
            this.dataSource.close();
    }

    @Bean
    @Lazy
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/kashuo/backend/dao/mapper/*Mapper.xml"));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    @Lazy
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


}
