package cn.sd.core.util;
 
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.imageio.ImageIO;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.filter.CityFilter;

public class UploadUtil {
	public static String uploadByte(byte[] fileByte, String savePath, String visitPath, String fileName) {
		int bytelength = fileByte.length;
		String newFileName = (fileName==null||fileName.equals(""))?getExtention(fileName):fileName;
		File newFile = new File(savePath);
		//判断路径是否存在
		if(!newFile.exists()) {  
			//创建此路径与文件
			if(!newFile.mkdirs()) {
				newFile.mkdirs();
			}
			newFile = new File(newFile+ File.separator + newFileName);
		}else{
			newFile = new File(newFile+ File.separator + newFileName);
		}	
	    try  {
	       OutputStream out = null ;
	        try  {                
	           out = new BufferedOutputStream(new FileOutputStream(newFile), bytelength);
	           out.write(fileByte);
	        } finally  {
	             if ( null != out)  {
	               out.close();
	           } 
	       } 
	    } catch (Exception e)  {
	       e.printStackTrace();
	   }
	   return  visitPath + newFileName;
	}
	
		
	/**
	 * 文件重新命名.
	 * @param fileName 原文件名.
	 * @return 返回一个新的文件名称.
	 */
	public static String getExtention(String fileName)  {
		int pos = fileName.lastIndexOf(".");
		String newFileName = CommonUtils.uuid() + fileName.substring(pos);
		return newFileName;
	}
	
	/**
	 * 文件上传路径和数据库保存的相对路径
	 * @param modelName 上传的模块名称
	 * @return
	 */
	public static String[] path(String modelName, boolean isBackUp){
		/*
		 * 获得配置文件上传路径,当前用户的城市拼音,当前系统时间.生成文件的保存路径.
		 */
		String uploadPath = ConfigLoader.config.getStringConfigDetail("upload.path");
		String cityName = CityFilter.getLocalCityPinYin();
		if("".equals(cityName) || cityName ==null){
			cityName = "xian";
		}
		Calendar cal = Calendar.getInstance(); 
		int year = cal.get(Calendar.YEAR); 
		int month = cal.get(Calendar.MONTH )+1; 
		int days = cal.get(Calendar.DATE);
		String relativePath = cityName + File.separator + modelName + File.separator + year + File.separator + month + File.separator + days + File.separator;
		String savePath = "";
		if(isBackUp) {
			savePath = uploadPath +File.separator+ "backup" + File.separator + relativePath;
		}else{
			savePath = uploadPath + File.separator + relativePath;
		}
		String[] path = new String[2];
		path[0] = savePath;
		path[1] = relativePath;
		return path;
	}
	
	/**
	 * 后台管理用
	 * 文件上传路径和数据库保存的相对路径
	 * @param modelName 上传的模块名称 
	 * @return
	 */
	public static String[] pathAdmin(String modelName,String cityName){
		/*
		 * 获得配置文件上传路径,当前用户的城市拼音,当前系统时间.生成文件的保存路径.
		 */
		String uploadPath = ConfigLoader.config.getStringConfigDetail("upload.path");

		if("".equals(cityName) || cityName ==null){
			cityName = "xian";
		}
		
		Calendar cal = Calendar.getInstance(); 
		int year = cal.get(Calendar.YEAR); 
		int month = cal.get(Calendar.MONTH )+1; 
		int days = cal.get(Calendar.DATE);
		String relativePath = cityName + File.separator + modelName + File.separator + year + File.separator + month + File.separator + days + File.separator;
		String savePath = uploadPath + File.separator + relativePath;
		String[] path = new String[2];
		path[0] = savePath;
		path[1] = relativePath;
		return path;
	}
	
	/**
	 * 文件上传路径和数据库保存的相对路径
	 * @param modelName 上传的模块名称 
	 * @return
	 */
	public static String[] pathAdmin(String modelName){
		/*
		 * 获得配置文件上传路径,当前系统时间.生成文件的保存路径.
		 */
		String uploadPath = ConfigLoader.config.getStringConfigDetail("upload.path");

		Calendar cal = Calendar.getInstance(); 
		int year = cal.get(Calendar.YEAR); 
		int month = cal.get(Calendar.MONTH )+1; 
		int days = cal.get(Calendar.DATE);
		String relativePath = modelName + File.separator + year + File.separator + month + File.separator + days + File.separator;
		String savePath = uploadPath + File.separator + relativePath + File.separator;
		String[] path = new String[2];
		path[0] = savePath;
		path[1] = relativePath;
		return path;
	}
	/**
	 * 缩略图
	 * @param modelName
	 * @return
	 */
	public static String[] thumbImagePath(String modelName){
		/*
		 * 获得配置文件上传路径,当前用户的城市拼音,当前系统时间.生成文件的保存路径.
		 */
		String uploadPath = ConfigLoader.config.getStringConfigDetail("upload.path");
		String cityName = CityFilter.getLocalCityPinYin();
		if("".equals(cityName) || cityName ==null){
			cityName = "xian";
		}
		Calendar cal = Calendar.getInstance(); 
		int year = cal.get(Calendar.YEAR); 
		int month = cal.get(Calendar.MONTH )+1; 
		int days = cal.get(Calendar.DATE);
		String relativePath = cityName + File.separator + modelName + File.separator + "thumbImage" + File.separator + year + File.separator + month + File.separator + days + File.separator;
		String savePath = uploadPath + File.separator + relativePath;
		String[] path = new String[2];
		path[0] = savePath;
		path[1] = relativePath;
		return path;
	}

	/**
	 * 生成缩略图
	 * @param tempFile上传的生成的临时文件
	 */
	public static String scale(File tempFile,int newWidth,int newHeight,String sysPath,String filename,String type){
		String fn = filename+tempFile.getName().substring(tempFile.getName().indexOf("."), tempFile.getName().length());
		if(!"".equals(type)){
			fn = filename+"-"+type+tempFile.getName().substring(tempFile.getName().indexOf("."), tempFile.getName().length());
		}
    	 
    	 try{
    		 BufferedImage src = ImageIO.read(tempFile); 
    		 int width = src.getWidth(null);
    	     int height = src.getHeight(null);
    		 if("source".equals(type)){
    			 newWidth = width;
    			 newHeight = height;
    		 }   	   
			if(newWidth >= width){
				if(newHeight < height){
					width = (int) (width * newHeight / height);
					height = newHeight;
				}
			}else{
				if(newHeight >= height){
					height = (int) (height * newWidth / width);
					width = newWidth;
				}else{
					if(height > width){
						width = (int) (width * newHeight / height);
						height = newHeight;
					}else{
						height = newHeight;
						width = newWidth;
					}
				}
			}
    		 Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);
    		 BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
    		// Graphics g = tag.getGraphics();
    		 Graphics2D g = tag.createGraphics();
    		 g.setRenderingHint(
    			        RenderingHints.KEY_ANTIALIASING,
    			        RenderingHints.VALUE_ANTIALIAS_ON); 
    		 g.drawImage(image, 0, 0, null);
    		 g.dispose();
    		 File newFile = new File(sysPath);
    			//判断路径是否存在
    			if(!newFile.exists()) {  
    				//创建此路径与文件
    				if(!newFile.mkdirs()) {
    					newFile.mkdirs();
    				}
    				newFile = new File(newFile+ File.separator + fn);
    			}else{
    				newFile = new File(newFile+ File.separator + fn);
    			}	
    		
    		 ImageIO.write(tag, "JPG", newFile);
    		 
    		 /*
    		    * 判断图片是否符合加水印的大小
    		    * 如果是会员头像.不添加水印
    		    */
    		  /* if(sysPath.indexOf("/member/icon/") == -1){
    			   ImageIcon imageIcon = new ImageIcon(newFile.getPath());
        	   	   if(imageIcon.getIconWidth() > 300 || imageIcon.getIconHeight() > 220) {
        	   		   String presspath = ConfigLoader.config.getStringConfigDetail("press.image.path");
        	   		   ImageUtils.pressImage(presspath, newFile.getPath(), ImageUtils.PressButtomRight, 8);
        	   	   }
    		   }*/
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }
    	 return fn;
     }
}
