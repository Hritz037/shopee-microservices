package com.shopee.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {
    //we define all the routing rules for API Gateway
    //if we want to access product service, then we have to add the rules in api gateway to forward our request to product service
    //here we are using Functional Endpoint Programming Model
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {

        return route("product_service")
                .route(RequestPredicates.path("/api/product"), HandlerFunctions.http("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse>  productServiceSwaggerRoute(){
        return route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8082"))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {

        return route("order_service")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http("http://localhost:8083"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){
        return route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8083"))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {

        return route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){
        return route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8084"))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute(){
        return route("fallbackRoute").GET("/fallbackRoute",request->ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service is down, please try again later"))
                .POST("/fallbackRoute",request->ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service is down, please try again later"))
                .build();
    }

}
