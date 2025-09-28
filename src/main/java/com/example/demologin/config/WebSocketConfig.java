package com.example.demologin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LessonPlanWebSocketHandler lessonPlanHandler;

    public WebSocketConfig(LessonPlanWebSocketHandler lessonPlanHandler) {
        this.lessonPlanHandler = lessonPlanHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(lessonPlanHandler, "/ws/lesson-plans")
                .setAllowedOrigins("*"); // dev cho phép all origin
    }
}
