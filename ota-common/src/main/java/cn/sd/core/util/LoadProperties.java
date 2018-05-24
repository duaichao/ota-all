package cn.sd.core.util;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class LoadProperties {
	private String filePath;
	public LoadProperties(String file){
		filePath = file;
	}
	
	public Properties getProps(){
		InputStream is = getClass().getResourceAsStream(filePath);
		Properties dbProps = new Properties();
		try {
	    dbProps.load(new InputStreamReader(is, "UTF-8"));
	  }
	  catch (Exception e) {
		  e.printStackTrace();
	    System.out.println("error");
	    return null;
	  }
	  return dbProps;
	}
	public String getPropName(String mame){
		return getProps().getProperty(mame);
	}
}
