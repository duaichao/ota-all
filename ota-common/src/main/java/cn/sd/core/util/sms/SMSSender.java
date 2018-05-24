package cn.sd.core.util.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import cn.sd.core.util.DateUtil;
import cn.sd.core.util.LoadProperties;
import cn.sd.core.util.jdbc;

public class SMSSender extends jdbc {
	private static LoadProperties prop=new LoadProperties("/jdbc.properties");
	/**
	 * 
	 * @param Mobile		用户手机号码(号码之间用英文逗号隔开，一次最多提交50个号码)
	 * @param Content		发送内容
	 * @param userName		系统用户名
	 * @param send_time		发送时间
	 * @param type			发送类型(默认:0, 1:找回密码, 2:后台注册, 3:后台订单, 4:群发, 5:在线充值, 6: 前台注册提示站长, 7：前台会员注册,8:后台其他费用审核,9:前台下单,10:微店会员注册)
	 * @param sms_type		短信类型(1:验证码, 2：其他)
	 * @param resources		来源 0:后台发送短信,1:前台发送短信
	 * @return inputLine    发送状态    0:发送成功, -1:帐号未注册, -2:其他错误, -3:密码错误, -4:手机号格式不对, -5:余额不足, -6:定时发送时间不是有效的时间格式
	 * 
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static int sendSMS(String Mobile,String Content,String userName,String send_time, String type, String SITE_ID, String sms_type, String resources, String orderId, String companyId, double cnt) throws MalformedURLException, UnsupportedEncodingException {
		URL url = null;
		String CorpID = prop.getPropName("SMS.username");//账户名
		String Pwd = prop.getPropName("SMS.password");//密码
		String send_content = URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");//发送内容
		url = new URL("http://mb345.com/ws/LinkWS.asmx/BatchSend?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time);
		BufferedReader in = null;
		int inputLine = 1; 
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(in);
			Element root = document.getRootElement();
			inputLine = Integer.parseInt(root.getText());
//			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			inputLine = -9;
		}
		String datetime = DateUtil.getNowDateTimeString();
		jdbc conn = new jdbc();  
		conn.executeUpdate("insert into SD_SMS_LOG(ID,MOBILE,CONTENT,CREATE_TIME,STATUS,CREATE_USER,TYPE, SITE_ID, sms_type, RESOURCES, order_id, company_id, cnt)values(sys_guid(),'"+Mobile+"','"+Content+"',to_date('"+datetime+"', 'YYYY-MM-DD HH24:MI:SS'),'"+inputLine+"','"+userName+"', '"+type+"','"+SITE_ID+"','"+sms_type+"', "+resources+", '"+orderId+"', '"+companyId+"', "+cnt+")");
		conn.close();
		return inputLine;
	}
	
	/**
	 * 
	 * @param Mobile		用户手机号码(号码之间用英文逗号隔开，一次最多提交50个号码)
	 * @param Content		发送内容
	 * @param send_time		发送时间
	 * @param id			日志表ID
	 * @return inputLine    发送状态    0:发送成功, -1:帐号未注册, -2:其他错误, -3:密码错误, -4:手机号格式不对, -5:余额不足, -6:定时发送时间不是有效的时间格式
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static int batchSendSMS(String Mobile,String Content, String id, String send_time) throws MalformedURLException, UnsupportedEncodingException {
		URL url = null;
		String CorpID = prop.getPropName("SMS.username");//账户名
		String Pwd = prop.getPropName("SMS.password");//密码
		String send_content = URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");//发送内容
		url = new URL("http://125.69.81.40:83/wsn/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time);
		BufferedReader in = null;
//		int inputLine = 1;
		int inputLine = 0;
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			inputLine = -9;
		}
		jdbc conn = new jdbc();  
		conn.executeUpdate("update sd_sms_log set CREATE_TIME=sysdate, STATUS = "+inputLine+" where id = '"+id+"'");
		conn.close();
		return inputLine;
	}
	
	public static void main(String[] args) {
		String s = "尊敬的在线旅行社，于2016-12-19 15:44:36提交的订单：测试fff 2016-12-24出发/2016-12-25返回，游客为1大0小， 联系人：草拟吗 电话：13720519218。请提前通知游客出团信息，并做好接待服务";
		double c = 50.00;
		double d = Double.parseDouble(String.valueOf(s.length()))/c;
		System.out.println(d);
		System.out.println(Math.ceil(d));
//		try {
//			new SMSSender().sendSMS("13720519218","旅游网欢迎您。","","","","","", "0", "", "");
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}
}
