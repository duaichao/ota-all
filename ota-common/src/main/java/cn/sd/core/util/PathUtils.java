package cn.sd.core.util;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class PathUtils {
	private static long localTime = 0;	//当前时间，只用于getTempFileMainName方法
	private static long localIndex = 0;	//当前时间生成的文件名次数，以防速度太快生成的文件名有重复。
	
	/**
	 * 按一个日期得到一个 yyyy/MM/dd 格式的路径，以URL分隔符表示
	 * @param data
	 * @return
	 */
	public static String getPathByDate(Date date){
		String returnValue = null;
		returnValue = DateUtil.parseDate(date,"yyyy/MM/dd");
		return returnValue;
	}
	
	/**
	 * 按时间得到一个不重复的文件名。但在集群环境下还是可能有重复。
	 * @return
	 */
	public static String getTempFileMainName(){
		long time = System.currentTimeMillis();
		if(localTime==time){
			localIndex = localIndex + 1;
		} else {
			localIndex = 0;
			localTime = time;
		}
		String name = time + "";
		if(localIndex!=0){
			name = name + "_" + localIndex;
		}
		return name;
	}
	
	/** 
     * 格式化文件路径，将其中不规范的分隔转换为当前操作系统的分隔符,并且去掉末尾的"/"符号。 
     * 
     * @param path 文件路径 
     * @return 格式化后的文件路径 
     */ 
    public static String formatPathWithLocalSystem(String path) { 
            String temp = formatPath(path);
            temp = StringUtils.replace(temp, "/", File.separator);
            return temp; 
    }
    
	/** 
     * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号。 
     * 
     * @param path 文件路径 
     * @return 格式化后的文件路径 
     */ 
    public static String formatPath(String path) { 
            String reg = "\\\\+|/+"; 
            String temp = path.trim().replaceAll(reg, "/"); 
            if (temp.endsWith("/")) { 
                    return temp.substring(0, temp.length() - 1); 
            } 
            return temp; 
    } 

    /** 
     * 获取文件父路径 
     * 
     * @param path 文件路径 
     * @return 文件父路径 
     */ 
    public static String getParentPath(String path) { 
            return new File(path).getParent(); 
    } 

    /** 
     * 获取相对路径 
     * 
     * @param fullPath 全路径 
     * @param rootPath 根路径 
     * @return 相对根路径的相对路径 
     */ 
    public static String getRelativeRootPath(String fullPath, String rootPath) { 
            String relativeRootPath = null; 
            String _fullPath = formatPath(fullPath); 
            String _rootPath = formatPath(rootPath); 

            if (_fullPath.startsWith(_rootPath)) { 
                    relativeRootPath = fullPath.substring(_rootPath.length()); 
            } else { 
                    throw new RuntimeException("要处理的两个字符串没有包含关系，处理失败！"); 
            } 
            if (relativeRootPath == null) return null; 
            else 
                    return formatPath(relativeRootPath); 
    }
    
	/**
	 * 分离出完整文件名中的路径
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFilePath(String _sFilePathName){
        int nPos = _sFilePathName.lastIndexOf('/');
        if(nPos < 0)
        {
            nPos = _sFilePathName.lastIndexOf('\\');
        }
        return nPos < 0 ? "" : _sFilePathName.substring(0, nPos + 1);
    }
	
	/**
	 * 分离文件扩展名
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFileExtensionName(String _sFilePathName){
		String returnValue = null;
		int index = _sFilePathName.lastIndexOf(".");
		if(index!=-1){
			returnValue = _sFilePathName.substring(index + 1);
		} else {
			returnValue = "";
		}
		return returnValue;
	}
	
	/**
	 * 分离文件主文件名，不带扩展名
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractMainFileName(String _sFilePathName){
		String returnValue = null;
		int index = _sFilePathName.lastIndexOf(".");
		if(index!=-1){
			returnValue = _sFilePathName.substring(0,index);
		} else {
			returnValue = _sFilePathName;
		}
		return returnValue;
	}
	
	/**
	 * 分离出完整文件名中的文件名部分
	 * @param _sFilePathName
	 * @return
	 */
	public static String extractFileName(String _sFilePathName){
        return extractFileName(_sFilePathName, File.separator);
    }
	
    public static String extractFileName(String _sFilePathName, String _sFileSeparator){
        int nPos = -1;
        if(_sFileSeparator == null)
        {
            nPos = _sFilePathName.lastIndexOf(File.separator);
            if(nPos < 0)
            {
                nPos = _sFilePathName.lastIndexOf(File.separator.equals("\\") ? "\\" : "/");
            }
        } else
        {
            nPos = _sFilePathName.lastIndexOf(_sFileSeparator);
        }
        if(nPos < 0)
        {
            return _sFilePathName;
        } else
        {
            return _sFilePathName.substring(nPos + 1);
        }
    }
    
    /**
     * 多级目录创建
     * @param folderPath 准备要在本级目录下创建新目录的目录路径 例如 c:myf
     * @param paths 无限级目录参数，各级目录以单数线区分 例如 a|b|c
     * @return 返回创建文件后的路径 例如 c:myfac
     */
    public static String createFolders(String path){
		String pathName = PathUtils.formatPath(path);
		String[] pathNames = StringUtils.split(pathName,"/");
		String tempPath = null;
		for(int i=0;i<pathNames.length;i++){
			if(tempPath==null) {
				tempPath = pathNames[i];
			} else {
				tempPath = tempPath + java.io.File.separator + pathNames[i];
			}
			java.io.File file=new java.io.File(tempPath);
			if(!file.exists()){
				file.mkdirs();
			}
		}
		return tempPath;
	}

    public static void main(String[] args){
    	String oldURL = "D:\\1\\2\\4\\4";
    	createFolders(oldURL);
    }

	public static long getLocalTime() {
		return localTime;
	}

	public static void setLocalTime(long localTime) {
		PathUtils.localTime = localTime;
	}

	public static long getLocalIndex() {
		return localIndex;
	}

	public static void setLocalIndex(long localIndex) {
		PathUtils.localIndex = localIndex;
	}
}
