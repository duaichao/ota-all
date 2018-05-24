package cn.sd.core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.sd.core.util.LoadProperties;
	  
	/** 
	* 用JDBC连接Oracle并读取数据的例子。 
	* 
	*  
	*/  
	public class jdbc {  
		private  LoadProperties prop=new LoadProperties("/jdbc.properties");
		String dbUrl = prop.getPropName("jdbc.url");  
		String driver = prop.getPropName("jdbc.driverClassName");
		String theUser = prop.getPropName("jdbc.username");  
		String thePw = prop.getPropName("jdbc.password");  
		
//		String dbUrl = "jdbc:oracle:thin:@113.140.83.72:1521:orcl";
//		String driver = "oracle.jdbc.driver.OracleDriver";
//		String theUser = "xsxlv";  
//		String thePw = "xsxlvora136";  
		
		Connection c = null;  
		Statement conn;  
		ResultSet rs = null;  
	  
		public jdbc() {  
		    try {  
		      Class.forName(driver).newInstance();  
		      c = DriverManager.getConnection(dbUrl, theUser, thePw);  
		      conn = c.createStatement();  
		    } catch (Exception e) {  
		      e.printStackTrace();  
		    }  
		}  
		  
		public boolean executeUpdate(String sql) {  
		    try {  
		      conn.executeUpdate(sql);  
		      return true;  
		    } catch (SQLException e) {  
		      e.printStackTrace();  
		      return false;  
		    }  
		}  
		  
		public ResultSet executeQuery(String sql) {  
		    rs = null;  
		    try {  
		      rs = conn.executeQuery(sql);  
		    } catch (SQLException e) {  
		      e.printStackTrace();  
		    }  
		    return rs;  
		}  
		  
		public void close() {  
		    try {  
		      conn.close();  
		      c.close();  
		    } catch (Exception e) {  
		      e.printStackTrace();  
		    }  
		}  
		  
		public static void main(String[] args) {  
		    ResultSet rs;  
		    jdbc conn = new jdbc();  
		    rs = conn.executeQuery("select * from test");  
		    try {  
		      while (rs.next()) {  
		        System.out.println(rs.getString("id"));  
		        System.out.println(rs.getString("name"));  
		      }  
		    } catch (Exception e) {  
		      e.printStackTrace();  
		    }  
		}  
	} 