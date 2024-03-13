package com.im.apigateway.config;

import com.im.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Autowired
    private JwtAuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("discovery-server",
                        r -> r.path("/eureka/web")
                                .filters(f -> f.rewritePath("/eureka/web", "/"))
                                .uri("http://localhost:8761"))
                .route("discovery-server-static",
                        r -> r.path("/eureka/**")
                                .uri("http://localhost:8761"))
                .route("sso-service",
                        r -> r.path("/api/noauth/user/**",
                                        "/api/noauth/info/**")
                                .uri("lb://sso-service"))
                .route("sso-service",
                        r -> r.path("/api/user**", "/api/user/**",
                                        "/api/component**", "/api/component/**",
                                        "/api/permission/**", "/api/auth/**",
                                        "/api/whiteLabel**", "/api/whiteLabel/**",
                                        "/api/admin/whiteLabel**", "/api/admin/whiteLabel/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://sso-service"))
                .route("news-service",
                        r -> r.path("/api/news**", "/api/news/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://news-service"))
                .route("contact-service",
                        r -> r.path("/api/noauth/contact**", "/api/noauth/contact/**")
                                .uri("lb://contact-service"))
                .route("contact-service",
                        r -> r.path("/api/contact**", "/api/contact/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://contact-service"))
                .route("document-service",
                        r -> r.path("/api/document**", "/api/document/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://document-service"))
                .route("announcement-service",
                        r -> r.path("/api/announcement**", "/api/announcement/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://announcement-service"))
                .route("support-service",
                        r -> r.path("/api/support**", "/api/support/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://support-service"))
                .route("file-storage-service",
                        r -> r.path("/api/file**", "/api/file/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://file-storage-service"))
                .route("file-storage-service",
                        r -> r.path("/api/noauth/file/**", "/api/noauth/file**")
                                .uri("lb://file-storage-service"))
                .route("form-service",
                        r -> r.path("/api/form**", "/api/form/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://form-service"))
                .route("form-service",
                        r -> r.path("/api/noauth/form**", "/api/noauth/form/**")
                                .uri("lb://form-service"))
                .route("billing-service",
                        r -> r.path("/api/billing**", "/api/billing/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://billing-service"))
                .route("iot-service",
                        r -> r.path("/api/device**", "/api/device/**")
                                .filters(f -> f.filter(filter))
                                .uri("http://iot-service:8080"))
                .route("iot-service",
                        r -> r.path("/api/test**", "/api/test/**")
                                .uri("http://iot-service:8080"))
                .build();
    }

}
