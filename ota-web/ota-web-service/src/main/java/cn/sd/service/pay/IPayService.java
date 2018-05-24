package cn.sd.service.pay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sd.entity.produce.CompanyDetail;


public interface IPayService {
	
	public Map<String, Object> alipay(String orderNo, String bankCode, Double money, boolean isBankPay, String uid, String description, CompanyDetail company);
	
	public Map<String, Object> cftpay(String orderNo, String bankCode, Double money, boolean isBankPay, String uid, HttpServletRequest request, HttpServletResponse response);
	
	public Map<String, Object> wapalipay(String orderNo, String bankCode, Double money, boolean isBankPay, String uid, String type, CompanyDetail company, String minishop);
}
