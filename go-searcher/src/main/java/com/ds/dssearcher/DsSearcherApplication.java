package com.ds.dssearcher;

import com.ds.dssearcher.runtime_config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
@RefreshScope
@EnableEurekaClient
@EnableConfigurationProperties({ AppConfig.class })
public class DsSearcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsSearcherApplication.class, args);
	}

}

@Configuration
class RestTemplateConfig {

	// Create a bean for restTemplate to call services
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}