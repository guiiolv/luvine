package com.luvine.modules.notification.infrastructure.config;

import com.luvine.modules.notification.infrastructure.client.BrevoEmailApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(BrevoProperties.class)
public class BrevoClientConfig {

    private static final String BREVO_BASE_URL = "https://api.brevo.com/v3";

    @Bean
    public BrevoEmailApi brevoEmailApi(BrevoProperties properties) {
        RestClient restClient = RestClient.builder()
                .baseUrl(BREVO_BASE_URL)
                .defaultHeader("api-key", properties.apiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(BrevoEmailApi.class);
    }
}