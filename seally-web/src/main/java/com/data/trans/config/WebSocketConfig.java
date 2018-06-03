package com.data.trans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.data.trans.interceptor.WebSocketInterceptor;
import com.data.trans.websocket.service.MyMessageHandler;

/**
 * @Date Jun 2, 2018
 * @author dnc
 * @Description 
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myMessageHandler(), "/myMessageHandler").addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");
    }

    @Bean
    public MyMessageHandler myMessageHandler() {
        return new MyMessageHandler();
    }

}
