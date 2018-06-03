package com.data.trans.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.seally.constant.WebConstant;

/**
 * @Date Jun 3, 2018
 * @author dnc
 * @Description 拦截器主要是用于用户登录标识（userId）的记录，便于后面获取指定用户的会话标识并向指定用户发送消息
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception arg3) {
		// TODO Auto-generated method stub
		
		System.out.println("握手以后");
		
	}

	@Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            HttpSession session = serverHttpRequest.getServletRequest().getSession();
            Map parameterMap = serverHttpRequest.getServletRequest().getParameterMap();
            System.out.println(parameterMap);
            if (session != null) {
            	Object userId = session.getAttribute(WebConstant.WEBSOCKET_USER_ID);
                map.put(WebConstant.WEBSOCKET_USER_ID, userId ==null?WebConstant.WEBSOCKET_USER_DEFAULT:userId);
                Object groupId = session.getAttribute(WebConstant.WEBSOCKET_GROUP_ID);
                map.put(WebConstant.WEBSOCKET_GROUP_ID, groupId == null?WebConstant.WEBSOCKET_GROUP_DEFAULT:groupId);
            }

        }
        return true;
    }

}
