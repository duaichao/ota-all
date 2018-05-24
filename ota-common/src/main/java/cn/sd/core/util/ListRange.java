package cn.sd.core.util;

import java.util.List;

/**
 * 用于方便返回JSON数据，列表用
 * @author rex
 */
@SuppressWarnings("unchecked")
public class ListRange {
	private List data;
	private List children;
	private int totalSize;
	private boolean success = true;
	private String message;
	private String statusCode;//状态码，不一定所有返回的数据都有这个值
	
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean getSuccess() {
		return this.success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public List getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	
}
