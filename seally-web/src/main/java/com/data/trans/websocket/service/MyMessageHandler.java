package com.data.trans.websocket.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.data.trans.common.ApiException;
import com.data.trans.common.ApiResponse;
import com.data.trans.common.ChatMessage;
import com.data.trans.common.ResponseEnum;
import com.data.trans.model.SystemUser;
import com.data.trans.service.SystemUserService;
import com.seally.constant.WebConstant;

/**
 * @Date Jun 2, 2018
 * @author dnc
 * @Description
 */
public class MyMessageHandler extends TextWebSocketHandler {
	//在线用户列表
    private static final Map<String, WebSocketSession> USER_SESSION_MAP = new HashMap<>();
    
    //在线用户组用户记录
    private static final Map<String, Set<String>> GROUP_USER_MAP = new HashMap<>();
    
    @Autowired
	private SystemUserService systemUserService;

    /**
     * @Date Jun 3, 2018
     * @author dnc
     * @Description 用户的参数信息再握手之前的拦截器中拦截放入到session中的
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立连接");
        String userId = getClientUserId(session);
        String groupId = getClientGroupId(session);
        System.out.println(userId+"  "+groupId);
        if (userId != null) {
        	putUser(session);
        	//建立连接成功后获取当前在线用户列表，并发送给该上线的人
        	sendOnlineUsersToUser(session);
            System.out.println(userId);
            System.out.println(session);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        
        @SuppressWarnings("unchecked")
		ChatMessage<List<SystemUser>> msgObj = JSON.parseObject(message.getPayload(), ChatMessage.class);
        //发送在线人列表
        if(null != msgObj.getCode() && WebConstant.CODE_CLIENT_REQUEST_GETUSER_ONLINE.intValue() == msgObj.getCode().intValue()) {
        	try {
        		sendOnlineUsersToUser(session);
			} catch (IOException e) {
				e.printStackTrace();
			}//直接给发件人发送
        }else {//发送消息
        	sendMessage(msgObj);
        }
    }
    //发送在线用户列表给指定用户
    public void sendOnlineUsersToUser(WebSocketSession toUserSession) throws IOException {
    	List<SystemUser> onlineUsers = systemUserService.findListByUserIds(USER_SESSION_MAP.keySet());//获取在线好友列表
    	ChatMessage<List<SystemUser>> msgObj = new ChatMessage<>();
    	msgObj.setResult(onlineUsers);
    	msgObj.setCode(WebConstant.CODE_CLIENT_REQUEST_GETUSER_ONLINE);
    	toUserSession.sendMessage(new TextMessage(JSON.toJSONString(msgObj)));
    }
    
    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public ApiResponse<String> sendMessage(ChatMessage<?> msg) {
    	
    	if(msg == null || (null == msg.getToUserIds() && null == msg.getToGroupIds())) {
    		throw new ApiException(ResponseEnum.PARAM_EMPT);
    	}
    	
    	List<String> toUserIds = msg.getToUserIds();
    	
    	List<String> toGroupIds = msg.getToGroupIds();
    	
    	Set<String> sendUserIds = new HashSet<>();
    	
    	Set<String> tempUserIds = new HashSet<>();
    	
    	if(null != toUserIds) {
    		tempUserIds.addAll(toUserIds);
    	}
    	
    	if(null != toGroupIds) {
    		toGroupIds.forEach(groupId -> {
    			if(!tempUserIds.contains(groupId)){
    				sendUserIds.addAll(GROUP_USER_MAP.get(groupId));
    			}
    		});
    	}
    	
    	if(msg.getHandType().intValue() == WebConstant.HANDTYPE_INCLUDE) {
    		sendUserIds.addAll(tempUserIds);
    	}
    	//sendUserIds.remove(msg.getFromUser().getUserId());//移除发件人自己
    	//清空多余信息
    	msg.setToGroupIds(null);
    	msg.setToUserIds(null);
    	sendUserIds.forEach(userId ->{
    		boolean status = sendToUser(userId, new TextMessage(JSON.toJSONString(msg)));
    	});
    	return ApiResponse.success();
    }
    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendToUser(String clientId, TextMessage message) {
        if (USER_SESSION_MAP.get(clientId) == null) return false;
        WebSocketSession session = USER_SESSION_MAP.get(clientId);
        System.out.println("sendMessage:" + session);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("连接出错");
        removeUser(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接已关闭：" + status);
        removeUser(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识
     * @param session
     * @return
     */
    private static String getClientUserId(WebSocketSession session) {
        try {
        	return  (String) session.getAttributes().get(WebConstant.WEBSOCKET_USER_ID);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取用户标识
     * @param session
     * @return
     */
    private static String getClientGroupId(WebSocketSession session) {
    	try {
    		return  (String) session.getAttributes().get(WebConstant.WEBSOCKET_GROUP_ID);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    
    /**
     * @Date Jun 3, 2018
     * @author dnc
     * @Description 记录用户分组及会话信息
     * @param groupId
     * @param userId
     */
    private static synchronized void putUser(WebSocketSession session) {
    	String userId = getClientUserId(session);
        String groupId = getClientGroupId(session);
    	USER_SESSION_MAP.put(userId, session);
    	Set<String> users = GROUP_USER_MAP.get(groupId);
    	if(null == users) {
    		users = new HashSet<>();
    	}
    	users.add(userId);
    	GROUP_USER_MAP.put(groupId, users);
    }
    /**
     * @Date Jun 3, 2018
     * @author dnc
     * @Description 记录用户分组及会话信息
     * @param groupId
     * @param userId
     */
    private static synchronized void removeUser(WebSocketSession session) {
    	Set<String> users = GROUP_USER_MAP.get(getClientGroupId(session));
    	if(null != users) {
    		users.remove(getClientUserId(session));
    	}
    	USER_SESSION_MAP.remove(getClientUserId(session));
    }
     
}
