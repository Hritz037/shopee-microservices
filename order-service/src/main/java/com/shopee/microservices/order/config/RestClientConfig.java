package com.shopee.microservices.order.config;

import org.springframework.beans.factory.annotation.Value;
import com.shopee.microservices.order.client.InventoryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
public class RestClientConfig {
    @Value("${inventory.url}")
    private String inventoryServiceURL;

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return factory;
    }
    @Bean
    public InventoryClient inventoryClient(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceURL)
                .requestFactory(clientHttpRequestFactory)
                .build();


        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(InventoryClient.class);
    }
}
