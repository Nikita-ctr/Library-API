package org.nikitactr.gatewayservice.config;
import org.nikitactr.gatewayservice.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes().route("AUTH-SERVICE", r -> r.path("/authenticate/**").filters(f -> f.filter(filter)).uri("lb://AUTH-SERVICE"))
                .route("BOOK-SERVICE", r -> r.path("/books/**").filters(f -> f.filter(filter)).uri("lb://BOOK-SERVICE"))
                .route("LIBRARY-SERVICE", r -> r.path("/library/loans/**").filters(f -> f.filter(filter)).uri("lb://LIBRARY-SERVICE"))
                .route("ORDER-SERVICE", r -> r.path("/order/**").filters(f -> f.filter(filter)).uri("lb://ORDER-SERVICE")).build();
    }
}
