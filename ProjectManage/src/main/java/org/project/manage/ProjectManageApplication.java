package org.project.manage;

import java.io.IOException;
import java.util.Properties;

import org.project.manage.util.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "org.project.manage" })
@EnableJpaRepositories({ "org.project.manage" })
@EntityScan({ "org.project.manage.entities" })
@EnableConfigurationProperties({ FileStorageProperties.class })
public class ProjectManageApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManageApplication.class, args);
	}

	@Bean()
	@Profile("!dev")
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
