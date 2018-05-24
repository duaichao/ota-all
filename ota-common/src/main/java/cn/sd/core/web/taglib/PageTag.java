/*
 * 创建日期 2004-11-26
 * 作者：qyh
 * 说明：分页标签

 * 修改记录：

 *     1、qyh于2004-11-26创建。

 *     2、

 */
package cn.sd.core.web.taglib;

import cn.sd.core.util.WebUtils;
import cn.sd.core.web.taglib.help.PageHelp;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * @author qyh
 * 用途:
 * 说明:
 */
public class PageTag extends BodyTagSupport {
	private String pageSizeKey="";				//pageSize的Session的Key
	private int pageSize=20;					//分页大小
	private long recordCount=0;					//记录总数，当这里为0时，则取recordCountKey
	private int recordOffset=0;					//取上一页多少行
	private int indexNum=10;					//索引个数
	private int indexSkip=2;					//索引感知点

	private int pageOffset=10;					//上N页，下N页

	private boolean autoScroll=false;			//索引自动滚动
	private boolean createIndex=false;			//是否生成索引
	private String recordCountKey="recordcount";//记录总数所放的Session的Key
	private String pageURLKey="pageurl";		//[页面URL]所放的Session的Key
	private String style="";					//分页条的样式
	private String pageBar="top";				//none,top,buttom,all
	private String pageURL="";					//页面URL，上页、下页......翻页时用。

	
	private String parameterGlean ="";			//需要自动收集的URL参数名列表。直接连接在pageURL后面。

	
	//以下内容从模板取
	private String locaPageStart="<B>";			//当前页前导字
	private String locaPageEnd="</B>";			//当前页结束符
	private String hrefStart="href=\"";			//URL前导字

	private String hrefEnd="\"";				//URL结束符

	
	private PageHelp ph=new PageHelp();			//分页辅助类

	private String startSessionKey="";			//开始记录号存放位置
	private String endSessionKey="";			//结束记录号存放位置

	private String tableHeadID="";	//表格的头部标题TR的ID,排序用
	private String formName="";		//表单的名字
	
	/**
	 * Defer our checking until the end of this tag is encountered.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String tempurl="";
		
		//自动收集需要转发的参数
		if (parameterGlean==null || parameterGlean.equals("")){
			parameterGlean = "Page_sort,Page_order";
		} else {
			parameterGlean = parameterGlean + ",Page_sort,Page_order";
		}
		if (parameterGlean!=null && (!"".equals(parameterGlean))){
			String parameters[] = StringUtils.split(parameterGlean,",");
			String myurl="";
			if (parameters!=null){
				for(int i=0;i<parameters.length;i++){
					if (!"".equals(parameters[i])){
						tempurl = request.getParameter(parameters[i]);
//						tempurl=WebUtils.getParameter(parameters[i],request);
						
						if (tempurl!=null){
							try {
								tempurl = new String(tempurl.getBytes("ISO-8859-1"),"UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							if (myurl.equals("")){
								myurl=parameters[i] + "=" + tempurl;
//								myurl=parameters[i] + "=" + java.net.URLEncoder.encode(tempurl);
							} else {
								myurl=myurl + "&" + parameters[i] + "=" + tempurl;
//								myurl=myurl + "&" + parameters[i] + "=" + java.net.URLEncoder.encode(tempurl);
							}
						}
					}
				}
				if ("".equals(pageURL)){
					HttpServletRequest hsr = (HttpServletRequest)request;
					pageURL = hsr.getServletPath() + "?" + myurl;
				} else {
					if(pageURL.indexOf("?")==-1){
						pageURL = pageURL + "?" + myurl;
					} else {
						pageURL = pageURL + "&" + myurl;
					}
				}
			}
		}
		
		//输出分页栏。
		javax.servlet.jsp.JspWriter writer = pageContext.getOut();
		ph.setAutoScroll(autoScroll);
		ph.setCreateIndex(createIndex);
		ph.setHrefEnd(hrefEnd);
		ph.setHrefStart(hrefStart);
		ph.setIndexNum(indexNum);
		ph.setIndexSkip(indexSkip);
		ph.setLocaPageEnd(locaPageEnd);
		ph.setLocaPageStart(locaPageStart);
		ph.setPageBar(pageBar);
		ph.setPageOffset(pageOffset);
		ph.setPageSize(pageSize);
		ph.setPageURL(pageURL);
		ph.setPageURLKey(pageURLKey);
		ph.setRecordCount(recordCount);
		ph.setRecordCountKey(recordCountKey);
		ph.setRecordOffset(recordOffset);
		ph.setStyle(style);
		
		ph.CreatePageBar(request);
		
		if (startSessionKey!=null && !startSessionKey.equals("")){
			pageContext.setAttribute(startSessionKey,ph.getStartRecord()+"");
		}
		if (endSessionKey!=null && !endSessionKey.equals("")){
			pageContext.setAttribute(endSessionKey,ph.getEndRecord()+"");
		}
		
		if (!"top".equals(pageBar) && !"all".equals(pageBar) && !"buttom".equals(pageBar)) {
			//输出到用户自定地方
			pageContext.setAttribute(pageBar,ph.getPageBarHTML());
		} else {
			if ("top".equals(pageBar) || "all".equals(pageBar)) {
				try {
					writer.println(ph.getPageBarHTML());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return (EVAL_BODY_INCLUDE);
	}

	public int doAfterBody() throws JspException {
		return SKIP_BODY;
	}
	
	/**
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		javax.servlet.jsp.JspWriter writer = pageContext.getOut();
		
		if (pageBar!=null && (!"".equals(pageBar))) {
			if ("buttom".equals(pageBar) || "all".equals(pageBar)) {
				try {
					writer.println(ph.getPageBarHTML());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		String sort=WebUtils.getParameter("Page_sort",request);
		if(sort==null) sort="";
		String order=WebUtils.getParameter("Page_order",request);
		if(order==null) order="";
		
		try {
			writer.println("<input type=\"hidden\" name=\"ie\" value=\"UTF-8\">");
			writer.println("<input type=\"hidden\" name=\"Page_sort\" value=\"" + sort + "\">");
			writer.println("<input type=\"hidden\" name=\"Page_order\" value=\"" + order + "\">");
			if(formName!=null && !formName.equals("") && tableHeadID!=null && !tableHeadID.equals("")){ 
				writer.println("<script>XPTable.Header.onLoaded(" + tableHeadID + ",'" + formName + "','" + sort + "','" + order + "');</script>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageContext.removeAttribute(pageBar);
		return (EVAL_PAGE);
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {
		super.release();
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
	public String getParameterGlean() {
		return parameterGlean;
	}

	/**
	 * @param string
	 */
	public void setParameterGlean(String string) {
		parameterGlean = string;
	}

	/**
	 * @return
	 */
	public String getEndSessionKey() {
		return endSessionKey;
	}

	/**
	 * @return
	 */
	public String getStartSessionKey() {
		return startSessionKey;
	}

	/**
	 * @param string
	 */
	public void setEndSessionKey(String string) {
		endSessionKey = string;
	}

	/**
	 * @param string
	 */
	public void setStartSessionKey(String string) {
		startSessionKey = string;
	}

	public String getPageSizeKey() {
		return pageSizeKey;
	}

	public void setPageSizeKey(String pageSizeKey) {
		this.pageSizeKey = pageSizeKey;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getTableHeadID() {
		return tableHeadID;
	}

	public void setTableHeadID(String tableHeadID) {
		this.tableHeadID = tableHeadID;
	}

}
