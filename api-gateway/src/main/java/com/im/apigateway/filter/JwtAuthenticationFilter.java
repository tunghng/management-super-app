package com.im.apigateway.filter;

import com.im.apigateway.exception.JwtTokenMalformedException;
import com.im.apigateway.exception.JwtTokenMissingException;
import com.im.apigateway.exception.UnAuthorizedException;
import com.im.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {

    WebClient.Builder webClientBuilder;
    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

        final List<String> apiEndpoints = List.of(
                "/api/auth/login", "/api/auth/token",
                "/api/info", "/api/auth/signup"
        );

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                throw new UnAuthorizedException("Invalid token.");

            }

            final String token = getJwtFromRequest(request);

            try {
                jwtUtil.validateToken(token);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                e.printStackTrace();

                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            }

            Claims claims = jwtUtil.getClaims(token);

            if (claims.get("role").equals("CUSTOMER_USER") && !request.getPath().value().contains("/api/auth")) {
                return webClientBuilder.build().get()
                        .uri("lb://sso-service/api/permission/validate",
                                uriBuilder -> uriBuilder
                                        .queryParam("userId", claims.get("userId"))
                                        .queryParam("url", request.getPath().value())
                                        .queryParam("method", request.getMethodValue()).build())
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus.is4xxClientError(),
                                response -> Mono.error(
                                        new UnAuthorizedException("You do not have permission to do this action.")))
                        .onStatus(httpStatus -> httpStatus.is5xxServerError(),
                                response -> Mono.error(new RuntimeException("Service timeout.")))
                        .bodyToMono(Void.class)
                        .then(chain.filter(exchange));
            }

            if (!claims.get("role").equals("SYS_ADMIN")) {
                return webClientBuilder.build().get()
                        .uri("lb://sso-service/api/permission/plan",
                                uriBuilder -> uriBuilder
                                        .queryParam("tenantId", claims.get("tenantId")).build())
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus.is4xxClientError(),
                                response -> Mono.error(
                                        new UnAuthorizedException("You do not have permission to do this action.")))
                        .onStatus(httpStatus -> httpStatus.is5xxServerError(),
                                response -> Mono.error(new RuntimeException("Service timeout.")))
                        .bodyToMono(Void.class)
                        .then(chain.filter(exchange));
            }
        }

        return chain.filter(exchange);
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}