package com.seally.constant;

/**
 * @Date Jun 3, 2018
 * @author dnc
 * @Description 统一常量类
 */
public interface WebConstant {

	/**websocket会话用户默认组标识**/
	String LOGIN_USER_KEY = "loginedUser";
	
	/**websocket会话用户默认组标识**/
	String WEBSOCKET_GROUP_DEFAULT = "seally-group";

	/**websocket会话用户默认组标识**/
	String WEBSOCKET_USER_DEFAULT = "seally-user";
	
	/**websocket会话用户组标识**/
	String WEBSOCKET_GROUP_ID = "groupId";
	
	/**websocket会话用户标识**/
	String WEBSOCKET_USER_ID = "userId";
	
	/**排除对象**/
	Integer HANDTYPE_EXCLUDE = 0;
	
	/**包含对象**/
	Integer HANDTYPE_INCLUDE = 1;
	
	/**客户端请求发送消息**/
	Integer CODE_CLIENT_REQUEST_SENDMSG = 10000;
	/**客户端请求获取在线好友列表**/
	Integer  CODE_CLIENT_REQUEST_GETUSER_ONLINE =10001;
	
	
}
