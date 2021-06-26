package com.shareadda.api.ShareAdda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableMongoAuditing
@EnableMongoRepositories
@SpringBootApplication
@EnableScheduling
public class ShareAddaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareAddaApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
		final RestTemplate restTemplate = new RestTemplate();

//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//        messageConverters.add(converter);
//        restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}
}
