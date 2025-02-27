package test.googleoauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient googleWebClient() {
        return WebClient.builder()
                .baseUrl("https://oauth2.googleapis.com")
                .build();
    }
    @Bean
    public WebClient googleApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://gmail.googleapis.com")
                .build();
    }
}
