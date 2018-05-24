package cn.sd.core.util.pinyin;

import cn.sd.core.util.jdbc;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.sql.ResultSet;


public class PinYin extends jdbc{
	public static void main(String[] args) {


		ResultSet rs;  
	    jdbc conn = new jdbc();  
	    rs = conn.executeQuery("select * from sd_city");  
	    try {  
	      while (rs.next()) {  
	    	  jdbc conn1 = new jdbc();  
				String id = rs.getString("ID");
				String text = rs.getString("TEXT");
				String pinyin = PinyinHelper.convertToPinyinString(text,"", PinyinFormat.WITHOUT_TONE);
				String jianpin = PinyinHelper.getShortPinyin(text);
				System.out.println(text+"--"+pinyin+"--"+jianpin);
				conn1.executeUpdate("update sd_city set pinyin= '"+pinyin+"', jianpin = '"+jianpin+"' where id = '"+id+"'");
				conn1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		ResultSet rs;  
//	    jdbc conn = new jdbc();  
//	    rs = conn.executeQuery("select * from SD_SCENIC where PINYIN IS NULL");  
//	    try {  
//	      while (rs.next()) {  
//	    	  jdbc conn1 = new jdbc();  
//				String id = rs.getString("ID");
//				String text = rs.getString("TEXT");
//				String pinyin = PinyinHelper.convertToPinyinString(text,"",PinyinFormat.WITHOUT_TONE);
//				String jianpin = PinyinHelper.getShortPinyin(text);
//				System.out.println(text+"--"+pinyin+"--"+jianpin);
//				conn1.executeUpdate("update SD_SCENIC set pinyin= '"+pinyin+"', jianpin = '"+jianpin+"' where id = '"+id+"'");
//				conn1.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
	}
	
	
}
