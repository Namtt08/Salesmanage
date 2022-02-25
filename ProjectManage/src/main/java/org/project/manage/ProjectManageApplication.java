package org.project.manage;

import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@SpringBootApplication
public class ProjectManageApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManageApplication.class, args);
	}
	
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
		Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("/global.config"));
		final PropertySourcesPlaceholderConfigurer propertyConfig = new PropertySourcesPlaceholderConfigurer();
		propertyConfig.setLocation(new FileSystemResource(properties.getProperty("configPath")));
		propertyConfig.setIgnoreResourceNotFound(false);
		propertyConfig.setFileEncoding("UTF-8");
		return propertyConfig;
	}

}
