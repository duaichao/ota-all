package cn.sd.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * 日期操作工具类
 * @author Rex
 */
@SuppressWarnings("static-access")
public class DateUtil {
	private static final String defaultFormat = "yyyy-MM-dd";
	private static final Locale defaultLocale = Locale.CHINA;
	
	/**
	 * 获取当前系统日期
	 * @return
	 */
	public static String getNowDate() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	/**
	 * 获取当前系统日期
	 * @return
	 */
	public static String getNowDate(String format) {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 获取当前系统时间
	 * @return
	 */
	public static String getNowTime() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * 获取当前系统日期和时间
	 * @return
	 */
	public static Date getNowDateTime() {
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * 获取当前系统日期和时间
	 * @return
	 */
	public static String getNowDateTimeString() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 获取当前系统日期和时间
	 * @return
	 */
	public static String getCurrDateTimeString() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return sdf.format(date);
	}
	/**
	 * 获取当前年
	 * @return
	 */
	public static Integer getNowYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前月
	 * @return
	 */
	public static Integer getNowMonth() {
		Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		return month;
	}
	/**
	 * 获取当前月之前之后 月
	 * @param int i
	 * @return
	 * **/
	public static String getNowDateMonth(Date d,int i,String format) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(c.MONTH, i);
		if (StringUtils.isEmpty(format)) {
			format="yyyyMM";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(c.getTime());
	}
	/**
	 * 获取当前日
	 * @return
	 */
	public static Integer getNowDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}
	/**
	 * 获取今天之前之后 天
	 * @param int i
	 * @return
	 * **/
	public static String getNowDateDAY(Date d,int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(c.DATE, i);
		return DateUtil.parseDate(c.getTime());
	}
	
	public static String getNowDateSecond(int i) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.getNowDateTime());
		c.add(c.SECOND, i);
		return DateUtil.parseDate(c.getTime(),"");
	}
	/**
	 * 获取系统当前小时（24小时制）
	 * @return
	 */
	public static Integer getNowHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取系统当前分钟
	 * @return
	 */
	public static Integer getNowMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	/**
	 * 获取系统当前秒数
	 * @return
	 */
	public static Integer getNowSecond() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}
	
	/**
	 * 获取系统当前星期
	 * @return
	 */
	public static Integer getNowWeek() {
		Integer temp = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
		if (temp == 0) {
			temp = 7;
		}
		return temp;
	}
	
	/**
	 * 从传入的Date类型中获取年份
	 * @param date
	 * @return
	 */
	public static Integer getYear(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 从传入的Date类型中获取月份
	 * @param date
	 * @return
	 */
	public static Integer getMonth(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 从传入的Date类型中获取日
	 * @param date
	 * @return
	 */
	public static Integer getDay(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}
	
	/**
	 * 从传入的Date类型中获取小时
	 * @param date
	 * @return
	 */
	public static Integer getHour(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 从传入的Date类型中获取分
	 * @param date
	 * @return
	 */
	public static Integer getMinute(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * 从传入的Date类型中获取秒数
	 * @param date
	 * @return
	 */
	public static Integer getSecond(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}
	
	/**
	 * 从传入的Date类型中获取星期
	 * @param date
	 * @return
	 */
	public static Integer getWeek(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer temp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		/*if (temp == 0) {
			temp = 6;
		}*/
		return temp;
	}
	
	/**
	 * Date类型转String类型，包括时间
	 * @param date
	 * @return
	 */
	public static String DateToString(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 获取一周周一的日期
	 * @return
	 */
	public static String getWeekMonday(Date date) {
		Date temp = DateUtil.parseDate(DateUtil.getWeekTuesday(date), "yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(temp);
		calendar.add(Calendar.DATE, -1);
		return DateUtil.parseDate(calendar.getTime());
	}
	
	
	/**
	 * 获取一周周二的日期
	 * @return
	 */
	public static String getWeekTuesday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		return DateUtil.parseDate(calendar.getTime());
	}
	
	/**
	 * 获取一周周六的日期
	 * @return
	 */
	public static String getWeekSaturday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		return DateUtil.parseDate(calendar.getTime());
	}
	
	/**
	 * 获取一周周日的日期
	 * @return
	 */
	public static String getWeekSunDay(Date date) {
		Date temp = DateUtil.parseDate(DateUtil.getWeekSaturday(date), "yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(temp);
		calendar.add(Calendar.DATE, 1);
		return DateUtil.parseDate(calendar.getTime());
	}
	
	/**
	 * 获取月份第一天的日期
	 * @param date
	 * @return
	 */
	public static String getMonthOneDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return DateUtil.parseDate(calendar.getTime());
	}
	
	/**
	 * 获取月份最后一天的日期
	 * @param date
	 * @return
	 */
	public static String getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, max);
		return DateUtil.parseDate(calendar.getTime());
	}
	
	/**
	 * 根据传入的日期，返回指定day天的日期，
	 * 即day为正整数时，返回后day天的日期，
	 * day为负整数时，返回前day天的日期
	 * @param date 当前日期
	 * @param days 经过day天的天数
	 * @return
	 */
	public static Date getNDay(Date date, Integer days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days); //星期天的日期
		return calendar.getTime();
	}
	
	/**
	 * 根据传入的日期时间，返回经过minute分钟后的日期时间
	 * @param date 当前日期
	 * @param minute 经过minute分钟
	 * @return
	 */
	public static Date getNHour(Date date, Integer hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour); //星期天的日期
		return calendar.getTime();
	}
	
	/**
	 * 根据传入的日期时间，返回经过minute分钟后的日期时间
	 * @param date 当前日期
	 * @param minute 经过minute分钟
	 * @return
	 */
	public static Date getNMinue(Date date, Integer minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute); //星期天的日期
		return calendar.getTime();
	}
	
	/**
	 * 根据传入的日期时间，返回经过minute分钟后的日期时间
	 * @param date 当前日期
	 * @param minute 经过minute分钟
	 * @return
	 */
	public static Date getNSecond(Date date, Integer second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second); //星期天的日期
		return calendar.getTime();
	}
	
	public static String getFirstDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance(defaultLocale);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return parseDate(cal.getTime(), null);
	}
	
	public static String getLastDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance(defaultLocale);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return parseDate(cal.getTime(), null);
	}
	
	public static String getFirstDayOfMonth(Date date) {
		// Calendar calendar = new GregorianCalendar();
		Calendar calendar = Calendar.getInstance(defaultLocale);
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		return parseDate(calendar.getTime(), null);
	}
	
	public static String getLastDayOfMonty(Date date) {
		// Calendar calendar = new GregorianCalendar();
		Calendar calendar = Calendar.getInstance(defaultLocale);
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return parseDate(calendar.getTime(), null);

	}
	
	public static String formatString(String date, String format) {
		Date d = parseDate(date);
		if(CommonUtils.checkString(format)){
			return parseDate(d, format);
		}
		return parseDate(d,defaultFormat);
	}
	
	public static String parseDate(Date date) {
		return parseDate(date,defaultFormat);
	}
	public static String parseDate(Date date, String patt) {
		if (StringUtils.isEmpty(patt)) {
			patt="yyyy-MM-dd hh:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patt);
		return sdf.format(date);
	}
	
	public static List<Date> getAllDates(Date start, Date end) {
		Calendar calendar1 = new GregorianCalendar();
		calendar1.setTime(start);
		Calendar calendar2 = new GregorianCalendar();
		calendar2.setTime(end);
		List<Date> list = new ArrayList<Date>();
		list.add(start);
		while (calendar1.compareTo(calendar2) < 0) {
			calendar1.add(Calendar.DATE, 1);
			list.add(calendar1.getTime());
		}
		return list;
	}
	
	public static List<Date> getMonths(Date start, Date end) {
		Calendar calendar1 = new GregorianCalendar();
		calendar1.setTime(start);
		Calendar calendar2 = new GregorianCalendar();
		calendar2.setTime(end);
		List<Date> list = new ArrayList<Date>();
		while (calendar1.compareTo(calendar2) < 0) {
			list.add(calendar1.getTime());
			calendar1.add(Calendar.MONTH, 1);
		}
		list.add(calendar1.getTime());
		return list;
	}
	
	/**
	 * 取得日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstWeekDay(Date date) {
		initCalendar(date);
		gc.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return gc.getTime();
	}
	
	/**
	 * 取得日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastWeekDay(Date date) {
		initCalendar(date);
		gc.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return gc.getTime();
	}
	
	/**
	 * 取得日期所在月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstMonthDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		gc.add(Calendar.DAY_OF_MONTH, 1 - dayOfMonth);
		return gc.getTime();
	}
	
	/**
	 * 取得日期所在月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastMonthDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		int maxDaysOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		gc.add(Calendar.DAY_OF_MONTH, maxDaysOfMonth - dayOfMonth);
		return gc.getTime();
	}
	
	/***************************************************************************
	 * 取得日期所在旬的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstTenDaysDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth <= 10) {
			gc.set(Calendar.DAY_OF_MONTH, 1);
		} else if (dayOfMonth > 20) {
			gc.set(Calendar.DAY_OF_MONTH, 21);
		} else {
			gc.set(Calendar.DAY_OF_MONTH, 11);
		}
		return gc.getTime();
	}
	
	/**
	 * 取得日期所在旬的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastTenDaysDay(Date date) {
		initCalendar(date);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth <= 10) {
			gc.set(Calendar.DAY_OF_MONTH, 10);
		} else if (dayOfMonth > 20) {
			gc.set(Calendar.DAY_OF_MONTH, gc
					.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			gc.set(Calendar.DAY_OF_MONTH, 19);
		}
		return gc.getTime();
	}
	
	/**
	 * 取得一年的第一天
	 * @param date
	 */
	public static Date getFirstDayOfYear(Date date){
		initCalendar(date);
		gc.set(Calendar.YEAR, Calendar.YEAR);
		gc.set(Calendar.MONTH,Calendar.JANUARY);
		Date firstDayOfMonth=getFirstMonthDay(gc.getTime());
		return firstDayOfMonth;
	}
	
	/**
	 * 取得一年的最后一天
	 * @param date
	 */
	public static Date getLastDayOfYear(Date date){
		initCalendar(date);
		gc.set(Calendar.YEAR, Calendar.YEAR);
		gc.set(Calendar.MONTH,Calendar.DECEMBER);
		Date firstDayOfMonth=getFirstMonthDay(gc.getTime());
		return firstDayOfMonth;
	}
	
	/**
	 * 将数组类型的星期用英文字母表示
	 * @param num
	 * @return
	 */
	public static String numToWeek(String num) {
		String temp = QuartzUtils.QUERTZMON;
		
		if ("1".equals(num.trim())) {
			temp = QuartzUtils.QUERTZMON;
		} else if ("2".equals(num.trim())) {
			temp = QuartzUtils.QUERTZTUE;
		} else if ("3".equals(num.trim())) {
			temp = QuartzUtils.QUERTZWED;
		} else if ("4".equals(num.trim())) {
			temp = QuartzUtils.QUERTZTHU;
		} else if ("5".equals(num.trim())) {
			temp = QuartzUtils.QUERTZFRI;
		} else if ("6".equals(num.trim())) {
			temp = QuartzUtils.QUERTZSAT;
		} else if ("7".equals(num.trim())) {
			temp = QuartzUtils.QUERTZSUN;
		} else {
			temp = QuartzUtils.QUERTZMON;
		}
		
		return temp;
	}
	/**
	 * 时间比较函数,传入两个日期,比较他们的日期大小
	 * 此方法在比较时,如果传入的日期包含时间,那么时间也在比较之内
	 * date 小于 compareDate 返回 1
	 * date 等于 compareDate 返回 0
	 * date 大于 compareDate 返回 -1
	 * @param date
	 * @param compareDate
	 * @return 
	 */
	public static int dateCompare(Date date, Date compareDate){
		int i = date.compareTo(compareDate);
		return i;
	}
	
	public static Date parseDate(String strDate){
		return parseDate(strDate,defaultFormat);
	}
	
	public static Date subDate(String date){
		return parseDate(date.substring(0,4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8));
	}
	
	public static Date parseDate(String strDate,String sformat){
		SimpleDateFormat format = new SimpleDateFormat(sformat);
		if (StringUtils.isNotBlank(strDate)) {
			try {
				return format.parse(strDate);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}
	
	private static void initCalendar(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("argument date must be not null");
		}

		gc.clear();
		gc.setTime(date);
	}
	
	private static GregorianCalendar gc = null;
	static {
		gc = new GregorianCalendar(defaultLocale);
		gc.setLenient(true);
		gc.setFirstDayOfWeek(Calendar.MONDAY);
	}
	
	
	
	
	public static String getFormatDate(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	public static Date str2date(String dateStr,String format){
		DateFormat sdf=new SimpleDateFormat(format);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String selfFormatDate(Date date,String format){
		String[] weekStr = {"日","一","二","三","四","五","六"}; 
		int week = DateUtil.getWeek(date);
		return DateUtil.getFormatDate(date, format)+"(周"+weekStr[week]+")";
	}
	public static Date addDay(Date d,long day)  {

		  long time = d.getTime(); 
		  day = day*24*60*60*1000; 
		  time+=day; 
		  
		  return DateUtil.parseDate(DateUtil.parseDate(new Date(time)));

	}
	
	public static Date intFormatDate(long UNIXDate){
		return new Date(UNIXDate * 1000);
	}
	
	public static void main(String[] args){
		

	}

}
