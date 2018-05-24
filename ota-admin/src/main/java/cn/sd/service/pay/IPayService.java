package cn.sd.service.pay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;


public interface IPayService {
	
	public Map<String, Object> alipay(String orderNo,String bankCode,Double money,boolean isBankPay,String uid, String description, String isEarnest);
	
	public Map<String, Object> cftpay(String orderNo,String bankCode,Double money,boolean isBankPay,String uid, HttpServletRequest request, HttpServletResponse response);
}
