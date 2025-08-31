package com.JinRui.fengkong4.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * MySQL数据源配置类
 * 
 * @author JinRui
 */
@Configuration
@MapperScan(basePackages = "com.JinRui.fengkong4.mapper", sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlDataSourceConfig {

    @Value("${spring.datasource.mysql.jdbc-url}")
    private String jdbcUrl;
    
    @Value("${spring.datasource.mysql.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.mysql.username}")
    private String username;
    
    @Value("${spring.datasource.mysql.password}")
    private String password;
    
    @Value("${spring.datasource.mysql.hikari.maximum-pool-size:20}")
    private int maximumPoolSize;
    
    @Value("${spring.datasource.mysql.hikari.minimum-idle:5}")
    private int minimumIdle;
    
    @Value("${spring.datasource.mysql.hikari.connection-timeout:30000}")
    private long connectionTimeout;

    /**
     * MySQL数据源配置
     * 
     * @return DataSource
     */
    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setConnectionTimeout(connectionTimeout);
        return dataSource;
    }

    /**
     * MySQL SqlSessionFactory配置
     * 
     * @param dataSource MySQL数据源
     * @return SqlSessionFactory
     * @throws Exception 异常
     */
    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(DataSource mysqlDataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(mysqlDataSource);
        
        // 设置实体类别名扫描路径
        factoryBean.setTypeAliasesPackage("com.JinRui.fengkong4.entity.mysql");
        
        // 设置MyBatis-Plus配置
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        factoryBean.setConfiguration(configuration);
        
        // 设置插件
        factoryBean.setPlugins(mybatisPlusInterceptor());
        
        return factoryBean.getObject();
    }

    /**
     * MySQL事务管理器
     * 
     * @param dataSource MySQL数据源
     * @return PlatformTransactionManager
     */
    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(DataSource mysqlDataSource) {
        return new DataSourceTransactionManager(mysqlDataSource);
    }

    /**
     * MyBatis-Plus插件配置
     * 
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        return interceptor;
    }
}
