package com.www.multiple.datasource.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Description MaaterDataSourceConfig
 * @Author 张卫刚
 * @Date Created on 2023/12/27
 */
@Configuration
// 指定数据库扫描对应的mapper文件，生成代理对象
@MapperScan(basePackages = "com.www.multiple.datasource.mybatis.mapper",sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    // mapper.xml所在地址
    private static final String MAPPER_LOCATION = "classpath:mapper/*Mapper.xml";

    /**
     * 主数据源，Primary 注解必须添加，它表示该数据源为默认数据源
     * 项目中还可能存在其他的数据源，如获取时不指定名称，则默认获取这个数据源，如果不添加，则启动时候会报错
     * @return
     */
    @Primary
    @Bean(name = "masterDataSource")
    // 读取spring.datasource.master 前缀的配置文件映射对应的配置对象
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource dataSource(){
        DataSource build = DataSourceBuilder.create().build();
        return build;
    }

    /**
     * 事务管理器，Primary注解作用同上
     * @param dataSource
     * @return
     */
    @Bean(name = "masterTransactionManager")
    @Primary
    public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("masterDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * session工厂，Primary注解作用同上
     * @param dataSource
     * @return
     */
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
       final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MasterDataSourceConfig.MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }
}
