package com.data.trans.websocket.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSON;
import com.data.trans.common.ApiResponse;
import com.data.trans.common.ChatMessage;
import com.data.trans.model.SystemUser;
import com.data.trans.websocket.service.MyMessageHandler;
import com.seally.constant.WebConstant;
import com.sun.tools.javac.util.List;

/**
 * @Date Jun 2, 2018
 * @author dnc
 * @Description
 */
@Controller
@RequestMapping("user/")
public class WebsocketController {
	@Autowired
	MyMessageHandler messageHandler;

    @RequestMapping("/joinChat")
    public String joinChat(HttpSession session,SystemUser user) {
        
    	System.out.println("登录接口,userId="+user.getUserId());
        
    	SystemUser currentUser = (SystemUser)session.getAttribute(WebConstant.LOGIN_USER_KEY);
        
        session.setAttribute(WebConstant.WEBSOCKET_GROUP_ID, StringUtils.isEmpty(currentUser.getGroupId())?WebConstant.WEBSOCKET_GROUP_DEFAULT:currentUser.getGroupId().trim());
        session.setAttribute(WebConstant.WEBSOCKET_USER_ID, currentUser.getUserId());
        System.out.println(session.getAttribute(WebConstant.WEBSOCKET_USER_ID));

        return "user/chat";
    }
    
//    @ResponseBody
//    @RequestMapping("/sendMessage")
//    public ApiResponse<String> sendMessage(ChatMessage msg) {
//        ApiResponse<String> result = messageHandler.sendMessage(msg);
//        System.out.println(result.getMessage());
//        return result;
//    }
}
