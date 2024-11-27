package com.nullterminators.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for creating RestTemplate bean. This configuration allows for RestTemplate to
 * be injected and used in other Spring-managed beans within the application.
 */
@Configuration
public class RestTemplateConfig {

  /**
   * Creates and returns a new instance of RestTemplate. This bean can be used to perform HTTP
   * requests in a Spring application.
   *
   * @return a RestTemplate instance
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
