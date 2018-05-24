package cn.sd.controller.report.bean;

import java.util.LinkedHashMap;

public class OrderBean {

	static {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("NO","订单编号");
		map.put("PRODUCE_TYPE","产品类型");
		map.put("COMPANY_NAME","产品公司名称");
		map.put("PORDUCE_CONCAT","产品公司联系人");
		map.put("PRODUCE_MOBILE","产品联系手机");
		map.put("START_DATE","出发班期");
		map.put("MUSTER_TIME","集合时间");
		map.put("MUSTER_PLACE","集合地点");
		map.put("VISITOR_CONCAT","游客联系人");
		map.put("VISITOR_MOBILE","游客联系电话");
		map.put("MAN_COUNT","成人");
		map.put("CHILD_COUNT","儿童/婴儿");
		map.put("STATUS","支付状态");
		map.put("SALE_AMOUNT","外卖金额");
		map.put("INTER_AMOUNT","同行金额");
		map.put("PAY_TYPE","支付类型");
		map.put("ACCOUNT_STATUS","队长状态");
		map.put("CREATE_USER","下单人");
		map.put("CREATE_TIME","下单时间");
		map.put("SITE_RELA_ID","站关系ID");
		map.put("IS_LOST","是否调单");
		map.put("LOST_TIME","掉单时间");
		map.put("CON_NO","游客合同订单号");
		map.put("PRODUCE_NAME","产品名称");
		map.put("PAY_TIME","支付时间");
		map.put("BUY_COMPANY","下单公司");
		map.put("BUY_PHONE","下单人电话");
	}
}
