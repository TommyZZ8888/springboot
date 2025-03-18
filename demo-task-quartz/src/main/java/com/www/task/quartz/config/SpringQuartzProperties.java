package com.www.task.quartz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Describtion: QuartzProperties
 * @Author: 张卫刚
 * @Date: 2025/3/18 16:34
 */
@Component
@ConfigurationProperties(prefix = "spring.quartz")
public class SpringQuartzProperties {

	private String jobStoreType;
	private boolean waitForJobsToCompleteOnShutdown;
	private String schedulerName;
	private Properties properties = new Properties();

	// Getters and Setters

	public String getJobStoreType() {
		return jobStoreType;
	}

	public void setJobStoreType(String jobStoreType) {
		this.jobStoreType = jobStoreType;
	}

	public boolean isWaitForJobsToCompleteOnShutdown() {
		return waitForJobsToCompleteOnShutdown;
	}

	public void setWaitForJobsToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
		this.waitForJobsToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
//		properties.forEach((key, value) -> this.properties.setProperty(key.toString(), value.toString()));
	}
}
