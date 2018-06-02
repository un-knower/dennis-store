package com.data.trans.util;

import java.util.UUID;

/**
 * @Date Jun 2, 2018
 * @author dnc
 * @Description
 */
public class UUidUtil {
	public static String generateUUid() {
		// 生成唯一uuid
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
