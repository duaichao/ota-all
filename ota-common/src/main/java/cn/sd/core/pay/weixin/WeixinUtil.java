package cn.sd.core.pay.weixin;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import cn.sd.core.util.CommonUtils;

public class WeixinUtil {
    
	private static Map<String, Object> weixinInfo = new HashMap<String, Object>();
	
	static {
		weixinInfo.put("xsx518.com", "wxdb7844363cf98f4e#8d5c58e947c8176583c856ba7ea83f80");
	}
	
    /**
    * 方法名：httpRequest</br>
    * 详述：发送http请求</br>
    * 开发人员：souvc </br>
    * 创建时间：2016-1-5  </br>
    * @param requestUrl
    * @param requestMethod
    * @param outputStr
    * @return 说明返回值含义
    * @throws 说明发生此异常的条件
     */
    public static JSONObject httpRequest(String requestUrl,String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    
    public static JSONObject getAccessToken(String domain){
    	String info = (String)weixinInfo.get(domain);
    	String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ info.split("#")[0] + "&secret=" + info.split("#")[1];
    	JSONObject tokenJson = WeixinUtil.httpRequest(url, "GET", null);
    	url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ tokenJson.getString("access_token")+ "&type=jsapi";
        return WeixinUtil.httpRequest(url, "GET", null);
    }
    
    /**
    * 方法名：getWxConfig</br>
    * 详述：获取微信的配置信息 </br>
    * 开发人员：souvc  </br>
    * 创建时间：2016-1-5  </br>
    * @param request
    * @return 说明返回值含义
    * @throws 说明发生此异常的条件
     */
    public static Map<String, Object> getWxConfig(HttpServletRequest request, String signUrl) {
    	WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();  
	    ServletContext application = webApplicationContext.getServletContext();  
	    System.out.println("signUrl---"+signUrl);
	    String requestUrl = request.getRequestURL().toString();
        if(CommonUtils.checkString(signUrl)){
        	requestUrl = signUrl; 
        }
	    String domain = "";
		try {
			domain = getTopDomainWithoutSubdomain(requestUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	    Map<String, Object> ret = new HashMap<String, Object>();
	    Map<String, Object> tickMap = new HashMap<String, Object>();
	    JSONObject json = null;
	    boolean b = false;
	    if(application.getAttribute(domain) != null){  
	    	Map<String, Object> tickTemp = (Map<String, Object>) application.getAttribute(domain);  
	        if(System.currentTimeMillis() < Long.parseLong(String.valueOf(tickTemp.get("expires_in")))){
	        	json = JSONObject.fromObject(tickTemp);
	        }else{  
	        	b = true;
	        	json = getAccessToken(domain);  
	        }  
	    }else{  
	    	b = true;
	    	json = getAccessToken(domain);
	    }
	    
	    if(b){
	    	tickMap.put("expires_in",  System.currentTimeMillis()+(3600*1000));
	    	tickMap.put("ticket", json.getString("ticket"));
		    application.setAttribute(domain, tickMap);
	    }
      
        String signature = "";
        String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
        String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串
        
        String jsapi_ticket = json.getString("ticket");
        
        System.out.println("requestUrl--11--"+requestUrl);
        requestUrl = getRequestURL(requestUrl);
        System.out.println("requestUrl--22--"+requestUrl);
     
        // 注意这里参数名必须全部小写，且必须有序
        String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr+ "&timestamp=" + timestamp + "&url=" + requestUrl;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String info = (String)weixinInfo.get(domain);
        ret.put("appId", info.split("#")[0]);
        ret.put("timestamp", timestamp);
        ret.put("nonceStr", nonceStr);
        ret.put("signature", signature);
        ret.put("expires_in", tickMap.get("expires_in"));
        return ret;
    }

    
    public static String getRequestURL(String requestUrl) {
    	if(requestUrl.indexOf("#") != -1){
    		requestUrl = requestUrl.substring(0, requestUrl.indexOf("#"));
    	}
		return requestUrl;
	}
    
    /**
    * 方法名：byteToHex</br>
    * 详述：字符串加密辅助方法 </br>
    * 开发人员：souvc  </br>
    * 创建时间：2016-1-5  </br>
    * @param hash
    * @return 说明返回值含义
    * @throws 说明发生此异常的条件
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    public static void main(String[] args) {
//    	getWxConfig(null, "");
    	System.out.println(System.currentTimeMillis());
    	System.out.println("1490172270665".length());
	}
    
    
    public static String getTopDomainWithoutSubdomain(String url)throws MalformedURLException {
		String host = new URL(url).getHost().toLowerCase();// 此处获取值转换为小写
		Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)");
		Matcher matcher = pattern.matcher(host);
		while (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
}