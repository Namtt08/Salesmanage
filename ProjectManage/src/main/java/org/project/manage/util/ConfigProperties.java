package org.project.manage.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties()
public class ConfigProperties {

	@Bean(name="configureBean")
	public Properties configureBean() throws IOException {
		Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        properties.load(inputStream);
		return properties;
    }
}
