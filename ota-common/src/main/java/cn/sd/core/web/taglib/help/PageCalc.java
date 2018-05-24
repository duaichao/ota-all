package cn.sd.core.web.taglib.help;

/**
 * 说明：分页计算。计算当前页　开始记录数，结束记录数；索引页开始位置，结束位置。 * @author   qyh
 * @version  1.0
 * 创建日期   2005-3-9
 */
public class PageCalc {
    private long l_start;		//开始纪录
    private long l_end;			//结束纪录
    private long l_curpage;		//当前页数
    private long l_totalnum;	//总记录数
    private int l_pageSize = 10;//每页10条
    private long l_totalpage;	//总的页数
	private long l_offset;		//重复　上一页　多少行
	
    private boolean hasPrepage;		//是否有上一页
    private boolean hasNextPage;		//是否有下一页
    
    private long l_indexStart = 0;		//索引页开始位置
	private long l_indexEnd = 0;		//索引页结束位置
	private int l_indexSize = 0;		//每页显示索引个数
	private int l_indexOffset = 0;		//索引页感知点
	
	private long l_lastPage = 0;		//最后一次的页码

    /**
     * 说明：计算分页数据
     * @param long currentpage 当前页
     * @param long totalnum 总记录数
     * @param int pageSize  页面大小
     * @param int offset	 重复　上一页　多少行
     * @author qyh
     */
    public void Init(long currentpage,long totalnum,int pageSize,int offset) {
        l_curpage = currentpage;
        l_totalnum = totalnum;
		l_pageSize = pageSize;
		l_offset = offset;
		
		//整理offset，offset不能大于或等于l_pageSize,totalnum
		if (offset>=pageSize || (long)offset>=totalnum) offset = 0;
		
		if (totalnum!=0) {
			//totalnum!=0时计算页数
			
			if (l_totalnum <= ((long)pageSize)){
				//当记录数不足分页时，直接得出分页数据
				l_totalpage = 1;
				l_start = 1;
				l_end = l_totalnum;
				l_curpage = 1;
			} else {
				//当记录数可以分页时，计算分页数据
				
				//得到总页数
				l_totalpage = (l_totalnum + (long)(pageSize - offset - 1)) / (long)(pageSize - offset);
				if (offset!=0){
					//当最后一页的记录数小于offset时，说明最后一页是一个无效页。
					if ((long)(getPageEnd(l_totalpage) - getPageStart(l_totalpage)) + 1 <= offset) l_totalpage = l_totalpage-1;
				}
				
				//整理当前页
			    if (currentpage >= 1) {
		            if (currentpage >= l_totalpage){
		                l_curpage = l_totalpage;
		            } else {
		                l_curpage = currentpage;
		            }
		        } else {
		            l_curpage = 1;
		        }
		        
		        //取得当前页的开始位置
		        if (l_curpage==1){
					l_start = 1;
		        } else {
					l_start = (l_curpage-1) * (long)(pageSize - offset) + 1L;
		        }
		        
				//取得当前页的结束位置
		        l_end = l_start + (long)pageSize -1L;
				if (l_end > l_totalnum) l_end = l_totalnum;
			}
			
		} else {
			//totalnum==0时不必计算
			l_totalpage = 0;
			l_curpage = 0;
			l_start = 0;
			l_end = 0;
		}
        
        //查检是否有上一页
		if (l_curpage <= 1) {
			hasPrepage = false;
		} else {
			hasPrepage = true;
		}
		//查检是否有下一页
		if (l_curpage >= l_totalpage) {
			hasNextPage = false;
		} else {
			hasNextPage = true;
		}
		if (l_totalpage<=1) {
			hasPrepage = false;
			hasNextPage = false;
		}
    }
	
	/**
	 * 说明：初始化分页跳跃索引，生成 1 2 3 4 5 6 7 8 9 
	 * @param indexNum 　保持索引的个数
	 * @param offset   　在第几个索引时跳跃
	 * @param indexend 　上一次索引的结束位置，以在自动Scroll时
	 * @param autoScroll 索引自动滚动
	 * 注意：此方法产生的结果可以用getPageIndexStart或getPageIndexEnd方法取得
	 */
	public void InitPageIndex(int indexNum,int offset,long indexend,boolean autoScroll){
		InitPageIndex(l_curpage,indexNum,offset,indexend,autoScroll);
	}
	public void InitPageIndex(long cpage,int indexNum,int offset,long indexend,boolean autoScroll){
		l_indexSize=indexNum;
		l_indexOffset=offset;
		
		if (l_totalpage==0) {
			l_indexStart = 0;
			l_indexEnd = 0;
		} else if (l_totalpage==1){
			l_indexStart = 1;
			l_indexEnd = 1;
		} else {
			if (offset==0) autoScroll = false;
			if (!autoScroll) {
				//利用分页对象取得当前页的索引落点页码
				PageCalc tempIndexPage = new PageCalc();
				tempIndexPage.Init(0,cpage,indexNum - offset,offset);
				long iip =tempIndexPage.getTotalpage();

				//取得索引开始位置和结束位置
				PageCalc IndexPage = new PageCalc();
				IndexPage.Init(iip,l_totalpage,indexNum - offset,offset);
				l_indexStart = IndexPage.getPageStart(IndexPage.getCurpage());
				l_indexEnd = IndexPage.getPageEnd(IndexPage.getCurpage());
				l_indexEnd = l_indexEnd + offset;
			} else {
				long last_indexend = 0;
				long last_indexstart = 0;
				if (indexend==0){
					last_indexstart = 1;
					last_indexend = indexNum;
				} else {
					last_indexstart = indexend - indexNum + 1;
					last_indexend = indexend;
					if (last_indexstart<1) last_indexstart =1;
				}
				
				l_indexEnd = last_indexend;
				l_indexStart = last_indexstart;
				
				//System.out.println(cpage + "原范围：" + last_indexstart + "--" + last_indexend);
				
				if (cpage >= last_indexstart + offset && cpage <= last_indexend - offset){
					//System.out.println("在原范围内，不作修改！");
				} else {
					//System.out.println("不在原范围内，需要对索引页作修改！");
					if (cpage - last_indexstart < offset){
						//System.out.println("前");
						if (cpage<=offset){
							l_indexStart =1;
							l_indexEnd = indexNum;
						} else {
							l_indexStart = cpage - offset;
							l_indexEnd = l_indexStart + indexNum;
							if (l_indexEnd - l_indexStart + 1< offset) {
								l_indexEnd = indexNum;
							}
						}
						//System.out.println("前" + (l_indexEnd + "," + l_indexStart)); 
					} else if (last_indexend - cpage < offset){
						//System.out.println("后");
						l_indexEnd =cpage + offset;
						if (l_indexEnd - l_indexStart < indexNum){
							l_indexEnd = indexNum;
						}
					}
				}
			}
			//整理索引开始位置和结束位置
			if (l_indexStart<1) l_indexStart = 1;
			if (l_indexEnd>l_totalpage) l_indexEnd = l_totalpage;
			if (l_indexEnd - l_indexStart + 1!=indexNum) {
				l_indexStart = l_indexEnd - indexNum + 1;
			}
			if (l_indexStart<1) l_indexStart = 1;
			
			//System.out.println("修正后：" + l_indexStart + "--" + l_indexEnd);
		}
	}
	
	/**
	 * 说明：取得当前页应该输出的索引页 开始位置
	 * @return 开始页
	 */
	public long getPageIndexStart(){
		return l_indexStart;
	}
	
	/**
	 * 说明：取得当前页应该输出的索引页 结束位置
	 * @return 结束页
	 */
	public long getPageIndexEnd(){
		return l_indexEnd;
	}
	
	/**
	 * 说明：取得某页开始位置。
	 * @param pagenum 页码
	 * @return 开始位置
	 */
	public long getPageStart(long pagenum){
		long s = 0;
		if (pagenum==1){
			s = 1;
		} else {
			s = (pagenum-1L) * (long)(l_pageSize -l_offset) + 1L;
		}
		return s;
	}
	
	/**
	 * 说明：取得某页结束位置。
	 * @param pagenum  页码
	 * @return	结束位置
	 */
	public long getPageEnd(long pagenum){
		long s = getPageStart(pagenum);
		long e = 0;
		e = s + l_pageSize -1;
		if (e > l_totalnum) e = l_totalnum;
		return e;
	}
	
	/**
	 * 说明：是否有上、下pagenum页，相对于当前页
	 * @param pagenum  页数
	 * @return	有|没有
	 */
	public boolean getHasPage(long pagenum){
		boolean returnValue;
		if (l_totalpage<=1) {
			returnValue = false;
		} else {
			if ((l_curpage + pagenum < 1L) || (l_curpage + pagenum > l_totalpage)) {
				returnValue = false;
			} else {
				returnValue = true;
			}
		}
		return returnValue;
	}
	
	/**
	 * 说明：是否有上一页
	 * @return 有｜没有
	 */
	public boolean getHasPrepage(){
		return hasPrepage;
	}
	
	/**
	 * 说明：是否有下一页
	 * @return 有｜没有
	 */
	public boolean getHasNextpage(){
		return hasNextPage;
	}
	
	/**
	 * 取得当前页的页码
	 * @return 当前页的页码
	 */
    public long getCurpage() {
        return l_curpage;
    }
	
	/**
	 * 说明：取得上一页的页码
	 * @return 上一页的页码
	 */
    public long getPrepage() {
        if (l_curpage -1L >= 1L) {
            return l_curpage -1L;
        } else {
            return 1L;
        }
    }
	
	/**
	 * 说明：取得下一页的页码
	 * @return 下一页的页码
	 */
    public long getNextpage() {
        if (l_curpage + 1L <= l_totalpage) {
            return l_curpage + 1L;
        } else {
            return l_totalpage;
        }
    }
	
	/**
	 * 说明：取得总记录数
	 * @return 总记录数
	 */
    public long getTotalnum() {
        return l_totalnum;
    }
    
	/**
	 * 说明：取得总页数
	 * @return 总页数
	 */
    public long getTotalpage() {
        return l_totalpage;
    }
	
	/**
	 * 说明：取得本次分页　开始位置，同getPageStart（getCurpage（））
	 * @return	开始位置
	 */
    public long getStart() {
        return l_start;
    }
	
	/**
	 * 说明：取得本次分页的　结束位置，同getPageEnd（getCurpage（））
	 * @return	结束位置
	 */
    public long getEnd() {
        return l_end;
    }
    
}

