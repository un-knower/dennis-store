package com.data.trans.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.data.trans.model.SystemUser;

/**
 * @Date 2018年3月25日
 * @author dnc
 * @Description 系统用户
 */
public interface SystemUserMapper extends BaseMapper<SystemUser>{

	List<SystemUser> findListByUserIds(@Param("userIds")Set<String> userIds);
	
	
}
