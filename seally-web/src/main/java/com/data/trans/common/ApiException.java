package com.data.trans.common;

/**
 * @Date 2018年3月25日
 * @author dnc
 * @Description 统一异常
 */
public class ApiException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String desc;
	
	public ApiException(){
		this.code=ResponseEnum.ERROR.getCode();
		this.desc = ResponseEnum.ERROR.getMessage();
	}
	
	public ApiException(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public ApiException(String desc) {
		this(ResponseEnum.ERROR.getCode(),desc);
	}
	
	public ApiException(ResponseEnum resp) {
		this(resp.getCode(),resp.getMessage());
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
