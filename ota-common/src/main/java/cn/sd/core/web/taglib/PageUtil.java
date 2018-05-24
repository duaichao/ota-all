package cn.sd.core.web.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.sd.core.web.taglib.help.PageHelp;

public class PageUtil {

	/**
	 * 
	 * @param recordCount	
	 * @param pageSize		
	 * @param recordOffset	
	 * @param indexNum
	 * @param indexSkip
	 * @param pageOffset
	 * @param autoScroll
	 * @param request
	 * @return
	 */
	public static Map<Object, Object> page(int recordCount, int pageSize, int recordOffset, int indexNum, 
			int indexSkip, int pageOffset, boolean autoScroll, HttpServletRequest request){
		
//		recordCount = recordCount == 0 ? 10 : recordCount;
//		recordOffset = recordOffset == 0 ? 10 : recordOffset;
//		indexNum = indexNum == 0 ? 10 : indexNum;
//		indexSkip = indexSkip == 0 ? 2 : indexSkip;
//		pageOffset = pageOffset == 0 ? 10 : pageOffset;
		
		PageHelp pageHelp = new PageHelp();
		pageHelp.setPageSize(pageSize);
		pageHelp.setRecordCount(recordCount);
		pageHelp.setRecordOffset(recordOffset);
		pageHelp.setIndexNum(indexNum);    
		pageHelp.setIndexSkip(indexSkip);
		pageHelp.setPageOffset(pageOffset);
		pageHelp.setAutoScroll(autoScroll);
		pageHelp.CreatePageBar(request);
		request.setAttribute("RecordCount", pageHelp.getRecordCount() + "");
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("start", pageHelp.getStartRecord());
		result.put("end", pageHelp.getEndRecord());
		return result;
	}
}
