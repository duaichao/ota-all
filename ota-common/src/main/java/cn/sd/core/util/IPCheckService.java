package cn.sd.core.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * IP 区域查询工具类
 * @author liwz at 2010-10-24
 *
 */
public class IPCheckService {
	private static boolean IS_ALL_FLAG= false;
	
	/**
	 * 
	 * @param需要查询的ip地址 或者域名信息
	 * @return String 区域信息 如： 陕西省西安市
	 */
	public static String getIPAreaString(String IPorDomain){
		String return_value="";
		return_value = getIPAreaStringByTaobao(IPorDomain);
		return return_value;
	}
	
	private static String getIPAreaStringByIP138(String IPorDomain){
		String areaString = "";
		try {
			NodeFilter filter = new TagNameFilter("li");
			Parser parser = new Parser();
			parser.setURL("http://www.ip138.com/ips8.asp?ip="+IPorDomain+"&action=2");
			parser.setEncoding(parser.getEncoding());
			NodeList list = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < list.size(); i++) {
				String temp = list.elementAt(i).toPlainTextString();
				if(temp.startsWith("本站主数据：")){
					areaString = temp.substring(6,temp.lastIndexOf(" "));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return areaString;		
	}
	
	private static String getIPAreaStringByPConline(String IPorDomain){
		String areaString = "";
		
	      try {
				String url = "http://whois.pconline.com.cn/jsLabel.jsp?ip="+IPorDomain;
				  HttpURLConnection urlConn = (HttpURLConnection) (new URL(url).openConnection());
				  HttpURLConnection.setFollowRedirects(true);
				  urlConn.setRequestMethod("GET");
				  urlConn.setConnectTimeout(6000); // 6秒钟链接主机
				  urlConn.setReadTimeout(5000);// 5秒钟读取内容超时
				  InputStream in = urlConn.getInputStream();
				  byte[] chByte = new byte[in.available()];
				  in.read(chByte);
				  areaString = new String(chByte).trim().split("=")[1].split(" ")[0];
				  areaString = areaString.substring(1,areaString.length());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		

		return areaString;		
	}	
	
	public static String getIPAreaStringByTaobao(String IPorDomain){
		String areaString = "";
		
	      try {
				String url = "http://ip.taobao.com/service/getIpInfo.php?ip="+IPorDomain;
				HttpURLConnection urlConn = (HttpURLConnection) (new URL(url).openConnection());
				HttpURLConnection.setFollowRedirects(true);
				urlConn.setRequestMethod("GET");
				urlConn.setConnectTimeout(6000); // 6秒钟链接主机
				urlConn.setReadTimeout(5000);// 5秒钟读取内容超时
				InputStream in = urlConn.getInputStream();
				byte[] chByte = new byte[in.available()];
				in.read(chByte);
				areaString = new String(chByte).trim();
				JSONObject ojb  = (JSONObject) JSON.toJSON(areaString);
			  	JSONObject jb = (JSONObject)ojb.get("data");
//				System.out.println(jb.toString());				
				//国家 区域 省 市 运营商
				String country = jb.getString("country");
				String area = jb.getString("area");
				String region = jb.getString("region");
				String city = jb.getString("city");
				String isp = jb.getString("isp");
//				areaString = country +" " +area+ " " +region+ " "+city+ " "+isp ;
				areaString = region+ " "+city;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		

		return areaString;		
	}		


	public static String getIPAreaStringBySina(String IPorDomain){
		String areaString = "";
		
	      try {
				String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="+IPorDomain;
				HttpURLConnection urlConn = (HttpURLConnection) (new URL(url).openConnection());
				HttpURLConnection.setFollowRedirects(true);
				urlConn.setRequestMethod("GET");
				urlConn.setConnectTimeout(6000); // 6秒钟链接主机
				urlConn.setReadTimeout(5000);// 5秒钟读取内容超时
				InputStream in = urlConn.getInputStream();
				byte[] chByte = new byte[in.available()];
				in.read(chByte);
				areaString = new String(chByte).trim();
//				areaString = areaString.substring(21, areaString.length() - 1);
				String as  = areaString.toString().replaceAll("var remote_ip_info = ", "");
				as = as.replaceAll(";", "");
				if(as.indexOf("{") != -1 && as.indexOf("}") != -1){
					JSONObject jb  = (JSONObject)JSON.toJSON(as);
					String province = jb.getString("province");
					String city = jb.getString("city");
					areaString = province+ " "+city;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}		
		return areaString;		
	}	
	
	
	/**
	 * 比较输入的两个ip或域名是不是在同一区域
	 * @param IPorDomain1 比较信息1
	 * @param IPorDomain2 比较信息2
	 * @return 区域相同返回 true ，否则返回 false
	 */
	public static boolean compareIPAreaInfo(String IPorDomain1, String IPorDomain2){
		boolean return_value = false;
		if(getIPAreaString(IPorDomain1).equals(getIPAreaString(IPorDomain2))){
			return_value = true;
		}
		return return_value;

	}	
	
	/**
	 * 比较输入的区域是不是在同一区域
	 * @param areaName1 比较信息1
	 * @param areaName2 比较信息2
	 * @return 区域相同返回 true ，否则返回 false
	 */
	public static boolean compareAreaInfo( String areaName1, String areaName2){
		boolean return_value = false;
		//假如是精确比较
		if(IS_ALL_FLAG){
			if(areaName1.equals(areaName2)){
				return_value = true;
			}
		}else{
			String areaName1_temp = areaName1;
			String areaName2_temp = areaName2;
			if(areaName1.indexOf("省")>-1){
				areaName1_temp = new String(areaName1.substring(0,areaName1.indexOf("省")));
			}
			if(areaName2.indexOf("省")>-1){
				areaName2_temp = new String(areaName2.substring(0,areaName2.indexOf("省")));
			}
			if(areaName1.indexOf(areaName2_temp)>-1 || areaName2.indexOf(areaName1_temp)>-1){
				return_value = true;
			}
			System.out.print(areaName1_temp+"=="+areaName2_temp);
		}
		return return_value;

	}	
	
	/**
	 *
	 * @param IP 需要查询的ip地址 或者域名信息
	 * @return String  电信/网通/铁通 等
	 */
	public static String getIPType(String IPorDomain){
		String typeString = "";
		try {
			NodeFilter filter = new TagNameFilter("li");
			Parser parser = new Parser();
			parser.setURL("http://www.ip138.com/ips8.asp?ip="+IPorDomain+"&action=2");
			parser.setEncoding(parser.getEncoding());
			NodeList list = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < list.size(); i++) {
				String temp = list.elementAt(i).toPlainTextString();
				if(temp.startsWith("本站主数据：")){
					typeString = temp.substring(temp.lastIndexOf(" ")+1,temp.length());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return typeString;
	}	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String areaInfo = IPCheckService.getIPAreaStringBySina("116.116.198.248");
		System.out.println(areaInfo);	
//		System.out.println(IPCheckService.getIPAreaString("www.3yx.com"));
//		System.out.println(IPCheckService.compareIPAreaInfo("www.3yx.com", "113.140.75.177"));		
//		System.out.println(IPCheckService.getIPType("113.140.75.178"));
//		System.out.println(IPCheckService.compareAreaInfo("山西省临汾市", "四川省成都市"));	
		
//		String cityName = "西安";
//		String areaInfo = IPCheckService.getIPAreaStringByTaobao("121.204.100.247");
//		if(areaInfo.indexOf("内蒙古")!=-1){
//			cityName="包头";
//			if(areaInfo.indexOf("呼和浩特")!=-1){
//				cityName="呼和浩特";
//			} else if(areaInfo.indexOf("呼伦贝尔")!=-1){
//				cityName="呼伦贝尔";
//			}
//		} else if(areaInfo.indexOf("浙江")!=-1){
//			cityName="杭州";
//		} else if(areaInfo.indexOf("福建")!=-1){
//			cityName="厦门";
//			if(areaInfo.indexOf("福州")!=-1){
//				cityName="福州";
//			} else if(areaInfo.indexOf("三明")!=-1){
//				cityName="三明";
//			}
//		} else if(areaInfo.indexOf("北京")!=-1){
//			cityName="北京";
//		} else if(areaInfo.indexOf("广西")!=-1){
//			cityName="桂林";
//		} else if(areaInfo.indexOf("贵州")!=-1){
//			cityName="贵阳";
//		} else if(areaInfo.indexOf("海南")!=-1){
//			cityName="海口";
//		} else if(areaInfo.indexOf("山东")!=-1){
//			cityName="济南";
//		} else if(areaInfo.indexOf("云南")!=-1){
//			cityName="昆明";
//			if(areaInfo.indexOf("丽江")!=-1){
//				cityName="丽江";
//			}
//		} else if(areaInfo.indexOf("甘肃")!=-1){
//			cityName="兰州";
//		} else if(areaInfo.indexOf("江苏")!=-1){
//			cityName="南京";
//		} else if(areaInfo.indexOf("青海")!=-1){
//			cityName="西宁";
//		} else if(areaInfo.indexOf("宁夏")!=-1){
//			cityName="银川";
//		} else if(areaInfo.indexOf("湖南")!=-1){
//			cityName="张家界";
//		}
//		
//		System.out.println(areaInfo);
//		System.out.println(cityName);
		
		
		

	}

}
