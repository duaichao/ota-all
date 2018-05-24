package cn.sd.core.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
public class FileUtil {

	/**
	 * 检查文件后缀名
	 * @param fileName 文件名称
	 * @param suffixs  扩展名,(如:jpg. 也可以使用多个后缀名以逗号隔开.如:jpg,png,gif)
	 * @return false 存在 true 不存在
	 */
	public static boolean checkFilesuffix(String fileName, String suffixs)  {
		boolean b = false;
		if(fileName == null || fileName.equals("") || suffixs == null || suffixs.equals("")){
			b = true;
		}else{
			suffixs = suffixs.toLowerCase();
			String suffix = getFileSuffix(fileName);
			if(suffixs.indexOf(suffix) == -1) {
				return true;
			}
		}
		return b;
	}
	
	/**
	 * 计算文件大小,返回传入计量单位的文件大小
	 * @param file 文件
	 * @param unit 要转换的单位
	 * @return 返回浮点类型,取值范围太小.返回整型会损失精度.所以返回的字符类型.
	 * 1.TB 2.GB 3.MB 4.KB 5.BYTE 
	 * 1T = 1024GB 1GB = 1024MB 1MB = 1024KB 1KB = 1024BYTE
	 */
	public static String computeFile(File file, int unit) {
		String size = "0";
		System.out.println(file.isFile());
		try {
			if (file != null && file.isFile()) {
				byte[] b = FileUtils.readFileToByteArray(file);
				int i = b.length;
				switch (unit) {
					case 1:
						size = String.valueOf(i/(1024*1024*1024*1024));
						break;
					case 2:
						size = String.valueOf(i/(1024*1024*1024));
						break;
					case 3:
						size = String.valueOf(i/(1024*1024));
						break;
					case 4:
						size = String.valueOf(i/(1024));
						break;
					case 5:
						size = String.valueOf(i);
						break;
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}
	
	/**
	 * 比较文件大小
	 * @return
	 */
	public static boolean compareFile(File srcFile, File destFile){
		boolean b = false;
		double srcFileSize = Double.valueOf(computeFile(srcFile, 5));
		double destFileSize = Double.valueOf(computeFile(destFile, 5));
		if(srcFileSize > destFileSize){
			b = true;
		}
		return b;
	}
	/**
	 * 得到文件的后缀名
	 * @param fileName
	 * @return
	 */
	public static String getFileSuffix(String fileName){
		int pos = fileName.lastIndexOf(".") + 1;
		String suffix = fileName.substring(pos).toLowerCase();
		return suffix;
	}
	/**
	 * 创建目录
	 * 
	 * @param destDirName 目标目录名
	 */
	public static void createDir(String destDirName) {
		File newFile = new File(destDirName);
		//判断路径是否存在
		if(!newFile.exists()) {  
			//创建此路径与文件
			if(!newFile.mkdirs()) {
				newFile.mkdirs();
			}
		}
	}

}
