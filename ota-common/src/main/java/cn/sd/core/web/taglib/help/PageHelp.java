/*
 * 创建日期 2004-11-26
 * 作者：qyh
 * 说明：分页标签

 * 修改记录：

 *     1、qyh于2004-11-26创建。

 *     2、

 */
package cn.sd.core.web.taglib.help;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.WebUtils;


/**
 * @author qyh
 * 用途:
 * 说明:
 */
public class PageHelp {
	private int pageSize=20;					//分页大小
	private long recordCount=0;					//记录总数，当这里为0时，则取recordCountKey
	private int recordOffset=0;					//取上一页多少行
	private int indexNum=10;					//索引个数
	private int indexSkip=2;					//索引感知点

	private int pageOffset=10;					//上N页，下N页

	private boolean autoScroll=false;			//索引自动滚动
	private boolean createIndex=false;			//是否生成索引
	private String recordCountKey="recordcount";//记录总数所放的Session的Key
	private String pageURLKey="pageurl";		//页面URL所放的Session的Key
	private String style="";					//分页条的样式
	private String pageBar="top";				//none,top,buttom,all
	private String pageURL="";					//页面URL
	
	//以下内容从模板取
	private String locaPageStart="<B>";			//当前页前导字
	private String locaPageEnd="</B>";			//当前页结束符
	private String hrefStart="href=\"";			//URL前导字
	private String hrefEnd="\"";				//URL结束符
	
	//以下为返回值
	private long startRecord=0;				//开始记录数
	private long endRecord=0;				//结束记录数

	private String pageBarHTML="";			//分页条

	
	HTMLBuilder hb = new HTMLBuilder(); 		//分页条样式操作
	HTMLBuilder indexhb = new HTMLBuilder(); 	//索引条样式操作
	
	public PageHelp(){
	}
	
	/**
	 * Defer our checking until the end of this tag is encountered.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public String CreatePageBar(HttpServletRequest request) {
		//取完整路径

		style = ConfigLoader.appPath  + java.io.File.separator + "WEB-INF" + java.io.File.separator + "style" + java.io.File.separator + style;
		PageCalc pc = new PageCalc();
		
		String tempurl=WebUtils.getParameter("pagesize",request);
		try{
			pageSize = Integer.parseInt(tempurl);
		} catch (Exception e){}
		
		long cpage=0;
		String spage = request.getParameter("page");
		try{
			cpage = Long.parseLong(spage);
		} catch (Exception e){
			cpage=0;
		}
		if (cpage==0) cpage = 1;
		
		long lastIndex = 0;
		spage = request.getParameter("lastindex");
		try{
			lastIndex = Long.parseLong(spage);
		} catch (Exception e){
			lastIndex=0;
		}
		
		if (pageURL==null || pageURL.equals("")){
			spage = (String)request.getAttribute(pageURLKey);
			if (spage==null){
				pageURL="";
			} else {
				pageURL=spage;
			}
		}
		
		if (recordCount==0) {
			spage = (String)request.getAttribute(recordCountKey);
			try{
				recordCount = Long.parseLong(spage);
			} catch (Exception e){
				recordCount=0;
			}
		}
		
		if (pageSize==0) {
			spage = request.getParameter("pagesize");
			pageSize = Integer.parseInt(spage);
		}
		
		pc.Init(cpage,recordCount,pageSize,recordOffset);
		cpage=pc.getCurpage();
		
		pc.InitPageIndex(cpage,indexNum,indexSkip,lastIndex,autoScroll);
		
		startRecord = pc.getStart();
		endRecord = pc.getEnd();
		
		if (pageBar!=null && (!"".equals(pageBar)) && (!"none".equals(pageBar))) {
			if (hb.LoadStyle(style)) {
				
				//从模板中取得配置信息
				String sb = hb.getContent();
				int strat = sb.indexOf("<!--Config");
				int end = sb.indexOf("Config-->");
				if (strat==-1 || end==-1){
					sb ="";
				} else {
					sb=sb.substring(strat+12,end);
				}
				
				if(!"".equals(sb)) {
					if(locaPageStart==null || "".equals(locaPageStart)) {
						strat = sb.indexOf("locaPageStart=");
						end  = sb.indexOf("\n",strat);
						if (strat==-1 || end==-1){
							locaPageStart="";
						} else {
							locaPageStart=sb.substring(strat+14,end-1);
						}
					}
					if(locaPageEnd==null || "".equals(locaPageEnd)) {
						strat = sb.indexOf("locaPageEnd=");
						end  = sb.indexOf("\n",strat);
						if (strat==-1 || end==-1){
							locaPageEnd="";
						} else {
							locaPageEnd=sb.substring(strat+12,end-1);
						}
					}
					if(hrefStart==null || "".equals(hrefStart)) {
						strat = sb.indexOf("hrefStart=");
						end  = sb.indexOf("\n",strat);
						if (strat==-1 || end==-1){
							hrefStart="";
						} else {
							hrefStart=sb.substring(strat+10,end-1);
						}
					}
					if(hrefEnd==null || "".equals(hrefEnd)) {
						strat = sb.indexOf("hrefEnd=");
						end  = sb.indexOf("\n",strat);
						if (strat==-1 || end==-1){
							hrefEnd="";
						} else {
							hrefEnd=sb.substring(strat+8,end-1);
						}
					}
				}
				//从模板中取得配置信息完成
				
				//将分页信息应用到模板中
				
				hb.PutValue("<%=页面URL%>",pageURL + "");
				hb.PutValue("<%=记录总数%>",recordCount + "");
				hb.PutValue("<%=当前页码%>","" + cpage + "");
				hb.PutValue("<%=总页数%>","" + pc.getTotalpage() + "");
				hb.PutValue("<%=记录开始位置%>","" + pc.getStart() + "");
				hb.PutValue("<%=记录结束位置%>","" + pc.getEnd() + "");
				
				if (cpage>1 && pc.getTotalpage()>1){
					hb.PutValue("<%=第一页URL%>",hrefStart + pageURL + "&page=1&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
				} else {
					hb.PutValue("<%=第一页URL%>","");
				}
				if (cpage>1 && pc.getTotalpage()>1){
					hb.PutValue("<%=上一页URL%>",hrefStart + pageURL + "&page=" + (cpage - 1) + "&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
				} else {
					hb.PutValue("<%=上一页URL%>","");
				}
				
				if (cpage<pc.getTotalpage() && pc.getTotalpage()>1){
					hb.PutValue("<%=下一页URL%>",hrefStart + pageURL + "&page=" + (cpage + 1) + "&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
				} else {
					hb.PutValue("<%=下一页URL%>","");
				}
				
				if (cpage<pc.getTotalpage() && pc.getTotalpage()>1){
					hb.PutValue("<%=最后一页URL%>",hrefStart + pageURL + "&page=" + pc.getTotalpage() + "&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
				} else {
					hb.PutValue("<%=最后一页URL%>","");
				}
				
				hb.PutValue("<%=当前开始索引号%>","" + pc.getPageIndexStart());
				hb.PutValue("<%=当前结束索引号%>","" + pc.getPageIndexEnd());
				
				if (cpage-pageOffset>1 && pc.getTotalpage()>1){
					hb.PutValue("<%=上N页URL%>",hrefStart + pageURL + "&page=" + (cpage - pageOffset) + "&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
				} else {
					hb.PutValue("<%=上N页URL%>","");
				}
				
				if (cpage + pageOffset<pc.getTotalpage() && pc.getTotalpage()>1){
					hb.PutValue("<%=下N页URL%>",hrefStart + pageURL + "&page=" + (cpage + pageOffset) + "&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
				} else {
					hb.PutValue("<%=下N页URL%>","");
				}
				
				//生成索引
				if (createIndex && pc.getTotalpage()>=1) {
					if (indexhb.LoadStyle(style+ ".ind")){
						String indexs = "";
						for (long i = pc.getPageIndexStart();i<=pc.getPageIndexEnd();i++){
							indexhb.reset();
							if (cpage==i){
								indexhb.PutValue("<%=页码%>","" + locaPageStart + i + locaPageEnd);
								indexhb.PutValue("<%=样式%>", " class=\"curr\"");
							} else {
								indexhb.PutValue("<%=页码%>","" + i);
								indexhb.PutValue("<%=样式%>", " ");
							}
							if (cpage==i){
								indexhb.PutValue("<%=页码URL%>","");
							} else {
								indexhb.PutValue("<%=页码URL%>",hrefStart + pageURL + "&page=" + i + "&pagesize=" + pageSize + "&lastindex=" + pc.getPageIndexEnd() + hrefEnd);
							}
							indexs=indexs + indexhb.getContent();
						}
						hb.PutValue("<%=索引%>",indexs);
					} else {
						hb.PutValue("<%=索引%>","");
					}
				} else {
					hb.PutValue("<%=索引%>","");
				}
			}
		}
		pageBarHTML=hb.getContent();
		return pageBarHTML;
	}

	/**
	 * @return
	 */
	public long getRecordCount() {
		return recordCount;
	}

	/**
	 * @return
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param l
	 */
	public void setRecordCount(long l) {
		recordCount = l;
	}

	/**
	 * @param string
	 */
	public void setStyle(String string) {
		style = string;
	}

	/**
	 * @return
	 */
	public String getRecordCountKey() {
		return recordCountKey;
	}

	/**
	 * @param string
	 */
	public void setRecordCountKey(String string) {
		recordCountKey = string;
	}

	/**
	 * @return
	 */
	public String getPageBar() {
		return pageBar;
	}

	/**
	 * @param string
	 */
	public void setPageBar(String string) {
		pageBar = string;
	}

	/**
	 * @return
	 */
	public boolean isAutoScroll() {
		return autoScroll;
	}

	/**
	 * @return
	 */
	public int getIndexNum() {
		return indexNum;
	}

	/**
	 * @return
	 */
	public int getIndexSkip() {
		return indexSkip;
	}

	/**
	 * @return
	 */
	public int getPageOffset() {
		return pageOffset;
	}

	/**
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return
	 */
	public int getRecordOffset() {
		return recordOffset;
	}

	/**
	 * @param b
	 */
	public void setAutoScroll(boolean b) {
		autoScroll = b;
	}

	/**
	 * @param i
	 */
	public void setIndexNum(int i) {
		indexNum = i;
	}

	/**
	 * @param i
	 */
	public void setIndexSkip(int i) {
		indexSkip = i;
	}

	/**
	 * @param i
	 */
	public void setPageOffset(int i) {
		pageOffset = i;
	}

	/**
	 * @param i
	 */
	public void setPageSize(int i) {
		pageSize = i;
	}

	/**
	 * @param i
	 */
	public void setRecordOffset(int i) {
		recordOffset = i;
	}

	/**
	 * @return
	 */
	public String getPageURL() {
		return pageURL;
	}

	/**
	 * @param string
	 */
	public void setPageURL(String string) {
		pageURL = string;
	}

	/**
	 * @return
	 */
	public String getPageURLKey() {
		return pageURLKey;
	}

	/**
	 * @param string
	 */
	public void setPageURLKey(String string) {
		pageURLKey = string;
	}

	/**
	 * @return
	 */
	public boolean isCreateIndex() {
		return createIndex;
	}

	/**
	 * @param b
	 */
	public void setCreateIndex(boolean b) {
		createIndex = b;
	}

	/**
	 * @return
	 */
	public String getHrefEnd() {
		return hrefEnd;
	}

	/**
	 * @return
	 */
	public String getHrefStart() {
		return hrefStart;
	}

	/**
	 * @param string
	 */
	public void setHrefEnd(String string) {
		hrefEnd = string;
	}

	/**
	 * @param string
	 */
	public void setHrefStart(String string) {
		hrefStart = string;
	}

	/**
	 * @return
	 */
	public String getLocaPageEnd() {
		return locaPageEnd;
	}

	/**
	 * @return
	 */
	public String getLocaPageStart() {
		return locaPageStart;
	}

	/**
	 * @param string
	 */
	public void setLocaPageEnd(String string) {
		locaPageEnd = string;
	}

	/**
	 * @param string
	 */
	public void setLocaPageStart(String string) {
		locaPageStart = string;
	}

	/**
	 * @return
	 */
	public String getPageBarHTML() {
		return pageBarHTML;
	}

	/**
	 * @return
	 */
	public long getEndRecord() {
		return endRecord;
	}

	/**
	 * @return
	 */
	public long getStartRecord() {
		return startRecord;
	}

}


/**
 * 说明：HTML生成类

 * @author qyh
 * @version 1.0
 * 创建日期 2006-1-17 17:29:06
 */
class HTMLBuilder {
	String templateContent = "";
	String tempContent = "";
	public String getContent(){
		return templateContent;
	}
	
	public boolean LoadStyle(String filename){
		FileInputStream fileinputstream=null;
		try {
			fileinputstream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			fileinputstream=null;
		}
		
		if (fileinputstream!=null){
			//读取模块文件
			int lenght = 0;
			try {
				lenght = fileinputstream.available();
				byte bytes[] = new byte[lenght];
				fileinputstream.read(bytes);
				fileinputstream.close();
				templateContent = new String(bytes,"UTF-8");
				tempContent = templateContent;
				return true;
			} catch (IOException e1) {
				templateContent = "";
				tempContent = templateContent;
			}
		} else {
			templateContent = "";
			tempContent = templateContent;
		}
		return false;
	}
	
	public void reset(){
		templateContent= tempContent;
	}
	
	public void PutValue(String key,String value){
		templateContent = StringUtils.replace(templateContent,key,value);
	}
}
