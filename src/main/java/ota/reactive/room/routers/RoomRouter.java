package ota.reactive.room.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ota.reactive.room.handlers.RoomHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class RoomRouter {

    @Bean
    public RouterFunction<ServerResponse> route(RoomHandler roomHandler) {
        return RouterFunctions
                .route(POST("/hello").and(accept(MediaType.APPLICATION_JSON)), roomHandler::hello)
                .andRoute(PUT("/change").and(accept(MediaType.APPLICATION_JSON)), roomHandler::change)
                .andRoute(DELETE("/bye").and(accept(MediaType.APPLICATION_JSON)), roomHandler::bye)
                .andRoute(GET("/query").and(accept(MediaType.APPLICATION_JSON)), roomHandler::query);
    }
}
