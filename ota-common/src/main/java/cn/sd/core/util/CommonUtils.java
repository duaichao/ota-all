package cn.sd.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class CommonUtils {
	
	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
    // 字符串在编译时会被转码一次,所以是 "\\b"    
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"    
            +"|windows (phone|ce)|blackberry"    
            +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"    
            +"|laystation portable)|nokia|fennec|htc[-_]"    
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"    
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
      
    //移动设备正则匹配：手机端、平板  
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);    
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);    
        
    /** 
     * 检测是否是移动设备访问 
     *  
     * @Title: check 
     * @Date : 2014-7-7 下午01:29:07 
     * @param userAgent 浏览器标识 
     * @return true:移动设备接入，false:pc端接入 
     */  
    public static boolean checkMobile(String userAgent){    
        if(null == userAgent){    
            userAgent = "";    
        }    
        // 匹配    
        Matcher matcherPhone = phonePat.matcher(userAgent);    
        Matcher matcherTable = tablePat.matcher(userAgent);    
        if(matcherPhone.find() || matcherTable.find()){    
            return true;    
        } else {    
            return false;    
        }    
    }  
    
	 /**
     * 检查字符是否是空值
     * @param s 需要检查的字符
     * @return false:空值,true:不是空值
     */
    public static boolean checkString(String s){
    	if(s == null || s.equals("") || s.trim().length() == 0 || "null".equals(s)){
    		return false;
    	}
    	return true;
    }
    public static boolean checkString(Object o){
    	if(o == null || o.toString().equals("") || o.toString().trim().length() == 0){
    		return false;
    	} 
    	return true;
    }
	public static boolean checkList(List s){
    	if(s == null || s.size() == 0){
    		return false;
    	}
    	return true;
    }
	
	public static boolean checkMap(Map m){
    	if(m == null || m.isEmpty()){
    		return false;
    	}
    	return true;
    }
    
	/**
	 * 抽取结果集中所需要的部分数据
	 * @param sql 查询语句
	 * @param start 开始值
	 * @param end 结束值
	 * @return
	 */
	public static String extractResult(String sql,int start, int end){
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("select * from (select my_table.*, rownum as my_rownum from(") 
        .append(sql) 
        .append(") my_table where rownum<=").append(end) 
        .append(") where my_rownum>=").append(start);
		return querySQL.toString();
	}
	
	/**
	 * 
	 * @return 
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 
	 * @param o 截取对象长度
	 * @param s 开始位置
	 * @param e 结束为止
	 * @param ObjectType 对象类型  1.int 2.double 3.float 4.long
	 * @return 截取成功的对象
	 */
	public static Object subObject(Object o, int s, int e, int ObjectType){
		String str = o.toString();
		Object obj = null;
		switch (ObjectType) {
		case 1:
			obj = Integer.parseInt(str.substring(s, e));
			break;
		case 2:
			obj = Double.parseDouble(str.substring(s, e));
			break;
		case 3:
			obj = Float.parseFloat(str.substring(s, e));
			break;
		case 4:
			obj = Long.parseLong(str.substring(s, e));
			break;
		}
		return obj;
	}
	
	/**
	 * 返回随机四个字母
	 * @return
	 */
	public static String getCharAuthCode(){
		int length=4;
		StringBuffer sb = new StringBuffer(length);
		Random r = new Random();
		int i = 0;
		while (i < length) {
			int asc=r.nextInt(122);
			if ((asc > 64&asc<91)||(asc > 96&asc<123)) {
				sb.append((char) asc);
				i++;
			}
		}
		return sb.toString();
	}
	
	/**
	 * 使用MD5加密字符
	 * @param str 要加密的字符
	 * @return
	 */
	public static String getMD5Code(String str){
		MessageDigest messageDigest;
		String result=null;
		try {
		messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(str.getBytes("UTF8"));
		result=byteHEX(messageDigest.digest());	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	/**
	 * byteHEX()，用来把一个byte类型的数转换成十六进制的ASCII表示，
	 * 因为java中的byte的toString无法实现这一点，我们又没有C语言中的
	 * sprintf(outbuf,"%02X",ib)
	 */
	public static String byteHEX(byte[] ib) {
	char[] digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	StringBuffer s = new StringBuffer(64);
	for(int i=0;i<ib.length;i++){
		char [] ob = new char[2];
		ob[0] = digit[(ib[i] >>> 4) & 0X0F];
		ob[1] = digit[ib[i] & 0X0F];
		s.append(new String(ob));
	}
	return s.toString();
	}
	
	public static String randomint(int lens) {
		char[] CharArray = {'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9' };
		int clens = CharArray.length;
		StringBuffer sCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < lens; i++) {
			sCode.append(CharArray[random.nextInt(clens)]);
		}
		return sCode.toString();
	}
	
	/**
     * 获取随机码
     * @param lens 要取多少位随机码
     * @return
     */
    public static String randomString(int lens) {
		char[] CharArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9' };
		int clens = CharArray.length;
		StringBuffer sCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < lens; i++) {
			sCode.append(CharArray[random.nextInt(clens)]);
		}
		return sCode.toString();
	}
   
    public static String _randomString(int lens) {
		char[] CharArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
				'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7',
				'8', '9' };
		int clens = CharArray.length;
		StringBuffer sCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < lens; i++) {
			sCode.append(CharArray[random.nextInt(clens)]);
		}
		return sCode.toString();
	}
    
    public static String rs(int lens) {
		char[] CharArray = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
				'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')','-' ,'+'};
		int clens = CharArray.length;
		StringBuffer sCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < lens; i++) {
			sCode.append(CharArray[random.nextInt(clens)]);
		}
		return sCode.toString();
	}
    
    /**
     * MD5加密
     * @param str
     * @return
     */
	public static String MD5(String str) {
		return CommonUtils.getMD5Code(str).toLowerCase();
	}
	/**
	 * 生成指定位数的随机码
	 * @param j 
	 * @return
	 */
	public static String randomInt(int i){
		StringBuffer s = new StringBuffer(); 
		Random random = new Random();
		for (int j = 0; j < i; j++) {
			s.append(random.nextInt(9));
		}
		return s.toString();
	}
	
	public static List<Map<String, Object>> jsonArrayToList(Object[] objArray, String[] keyNames){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
	    for(int i = 0; i < objArray.length; i++){
	    	JSONObject jobject = (JSONObject) JSON.toJSON(objArray[i]);
	    	Map<String, Object> data = new HashMap<String, Object>();
	    	for (int j = 0; j < keyNames.length; j++) {
	    		boolean b = jobject.containsKey(keyNames[j]);
	    		if(b){
	    			data.put(keyNames[j], jobject.getString(keyNames[j]));
	    		}
			}
	    	datas.add(data);
		}
		return datas;
	}
	
	public static void main(String[] args) {
	}
}
