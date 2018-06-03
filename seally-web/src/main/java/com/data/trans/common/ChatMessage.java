package com.data.trans.common;
/**
 * @Date Jun 3, 2018
 * @author dnc
 * @Description
 */

import java.sql.Date;
import java.util.List;

import com.data.trans.model.SystemUser;
import com.seally.constant.WebConstant;

/**
 * @Date Jun 3, 2018
 * @author dnc
 * @Description 消息实体
 */
public class ChatMessage<T> extends ApiResponse<T>{
	
	private SystemUser fromUser;//来自于
	private Integer handType = WebConstant.HANDTYPE_INCLUDE; //处理类型
	private List<String> toUserIds;//发送对象(在handType 为0时 需要排除掉组中的这些用户 为1时附加这些用户)
	private List<String> toGroupIds;//发送组
	private String content;
	private Date sendTime;
	
	public List<String> getToUserIds() {
		return toUserIds;
	}
	public void setToUserIds(List<String> toUserIds) {
		this.toUserIds = toUserIds;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public List<String> getToGroupIds() {
		return toGroupIds;
	}
	public void setToGroupIds(List<String> toGroupIds) {
		this.toGroupIds = toGroupIds;
	}
	public SystemUser getFromUser() {
		return fromUser;
	}
	public void setFromUser(SystemUser fromUser) {
		this.fromUser = fromUser;
	}
	public Integer getHandType() {
		return handType;
	}
	public void setHandType(Integer handType) {
		this.handType = handType;
	}
	
	
}
