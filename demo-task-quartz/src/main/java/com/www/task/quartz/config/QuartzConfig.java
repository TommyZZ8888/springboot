package com.www.task.quartz.config;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @Describtion: QuartzConfig
 * @Author: 张卫刚
 * @Date: 2025/3/18 9:35
 */
@Configuration
public class QuartzConfig {

	private static final String QUARTZ_PROPERTIES_PATH = "quartz.properties";


	@Bean
	public JobFactory getJobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, DataSource dataSource, PlatformTransactionManager transactionManager) throws IOException {
		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
		factoryBean.setAutoStartup(true);
		factoryBean.setJobFactory(jobFactory);
		factoryBean.setQuartzProperties(quartzProperties());

		factoryBean.setDataSource(dataSource);
		factoryBean.setTransactionManager(transactionManager);
		return factoryBean;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_PATH));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}


	public static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

		private transient AutowireCapableBeanFactory beanFactory;

		@Override
		public void setApplicationContext(final ApplicationContext applicationContext) {
			beanFactory = applicationContext.getAutowireCapableBeanFactory();
		}

		@Override
		protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
			Object job = super.createJobInstance(bundle);
			beanFactory.autowireBean(job);
			return job;
		}
	}


}
