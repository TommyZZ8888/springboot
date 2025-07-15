package com.www.zz.demoworkflow.common.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description MyBatisPlusConfig
 * @Author 张卫刚
 * @Date Created on 2023/12/6
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        return new PaginationInnerInterceptor();
    }


//    @Primary
//    @Bean("db1SqlSessionFactory")
//    public SqlSessionFactory db1SqlSessionFactory(DataSource dataSource) throws Exception {
//        /**
//         * 使用 mybatis plus 配置
//         */
//        MybatisSqlSessionFactoryBean b1 = new MybatisSqlSessionFactoryBean();
//        System.out.println("dataSourceLyz"+dataSource.toString());
//        b1.setDataSource(dataSource);
//        b1.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
//        return b1.getObject();
//    }

}
