package cn.sd.core.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class QuartzUtils {
	private static final Log log = LogFactory.getLog(QuartzUtils.class);
	/**默认tirgger组名称*/
	public static final String DEFAULTTIGGERGROUPNAME = "TDBTrigger";
	
	/**默认trigger名称*/
	public static final String DEFAULTTRIGGERNAME = "TDBCronTrigger";
	
	public static final String DEFAULTJOBGROUPNAME = "TDB";
	
	/**
	 * Quertz表达式中代表所有的值。
	 * “*”在子表达式（月）里表示每个月的含义。
	 * “*”在子表达式（天（星期））表示星期的每一天。
	 */
	public static final String QUERTZSTAR = "*";
	
	/**Quertz表达式中，字符仅被用于天（月）和天（星期）两个子表达式，表示不指定值*/
	public static final String QUERTZWHY = "?";
	
	/**
	 * 字符仅被用于天（月）和天（星期）两个子表达式，它是单词“last”的缩写。
	 * 在天（月）子表达式中，“L”表示一个月的最后一天。
	 * 在天（星期）自表达式中，“L”表示一个星期的最后一天，也就是SAT
	 * 如果在“L”前有具体的内容，它就具有其他的含义了。
	 * 例如：“6L”表示这个月的倒数第６天，“ＦＲＩＬ”表示这个月的最一个星期五。
	 * 注意：在使用“L”参数时，不要指定列表或范围，因为这会导致问题
	 */
	public static final String QUERTZL = "L";
	
	
	/**
	 * “/”字符用来指定数值的增量。
	 * 在子表达式（分钟）里的“0/15”表示从第0分钟开始，每15分钟。
	 */
	public static final String QUERTZINCLINE = "/";
	
	/**星期常量*/
	public static final String QUERTZMON = "MON";
	public static final String QUERTZTUE = "TUE";
	public static final String QUERTZWED = "WED";
	public static final String QUERTZTHU = "THU";
	public static final String QUERTZFRI = "FRI";
	public static final String QUERTZSAT = "SAT";
	public static final String QUERTZSUN = "SUN";
	
	/**执行频率*/
	/*每秒执行*/
	public static final int QUERTZEVERYSECOND = 1;
	/*每分执行*/
	public static final int QUERTZEVERYMINUE = 2;
	/*每时执行*/
	public static final int QUERTZEVERYHOUR = 3;
	/*每天执行*/
	public static final int QUERTZEVERYDAY = 4;
	/*每周执行*/
	public static final int QUERTZEVERYWEEK = 5;
	/*每月执行*/
	public static final int QUERTZEVERYMONTH = 6;
	/*只运行一次*/
	public static final int QUERTZONETIME = 7;
	
	/*秒（0~59）*/
	private String second;
	/*分（0~59）*/
	private String minute;
	/*时（0~23）*/
	private String hour;
	/*日（0~31，但是你需要考虑你月的天数）*/
	private String date;
	/*月（0~11）*/
	private String month;
	/*年（可选）, 1970-2099*/
	private String year;
	/*星期（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）*/
	private String week;
	
	/*Quertz规则*/
	private String quertzRule;
	
	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getQuertzRule() {
		return quertzRule;
	}

	public void setQuertzRule(String quertzRule) {
		this.quertzRule = quertzRule;
	}

	public QuartzUtils() {
		initQuertzRule();
	}
	
	public QuartzUtils(String second, String minute, String hour, String date,
			String month, String week) {
		super();
		this.second = second;
		this.minute = minute;
		this.hour = hour;
		this.date = date;
		this.month = month;
		this.week = week;
		this.year = week;
		this.quertzRule =  getSecond()
		+ " " + getMinute()
		+ " " + getHour()
		+ " " + getDate()
		+ " " + getMonth()
		+ " " + getWeek();
	}

	private void initQuertzRule() {
		setSecond("*");
		setMinute("*");
		setHour("*");
		setDate("*");
		setMonth("*");
		setYear("*");
		setWeek("*");
		
		String rule = getSecond()
			+ " " + getMinute()
			+ " " + getHour()
			+ " " + getDate()
			+ " " + getMonth()
			+ " " + getWeek();
		setQuertzRule(rule);
	}
	
	/**
	 * 将Date转换成Quertz规则
	 * @param date
	 * @return
	 */
	public static String getQuertzRule(Date date, int runCount) {
		if (date == null) {
			return null;
		}
		
		String second = String.valueOf(DateUtil.getSecond(date));
		String minute = String.valueOf(DateUtil.getMinute(date));
		String hour = String.valueOf(DateUtil.getHour(date));
		String day = String.valueOf(DateUtil.getDay(date));
		String month = String.valueOf(DateUtil.getMonth(date));
		String week = DateUtil.numToWeek(String.valueOf(DateUtil.getWeek(date)));
		String year = String.valueOf(DateUtil.getYear(date));
		switch (runCount) {
		case QuartzUtils.QUERTZEVERYSECOND:
			second = QuartzUtils.QUERTZSTAR;
			minute = QuartzUtils.QUERTZSTAR;
			hour = QuartzUtils.QUERTZSTAR;
			day = QuartzUtils.QUERTZSTAR;
			month = QuartzUtils.QUERTZSTAR;
			week = QuartzUtils.QUERTZWHY;
			year = "";
			log.info("每秒");
			break;
		case QuartzUtils.QUERTZEVERYMINUE:
			minute = QuartzUtils.QUERTZSTAR;
			hour = QuartzUtils.QUERTZSTAR;
			day = QuartzUtils.QUERTZSTAR;
			month = QuartzUtils.QUERTZSTAR;
			week = QuartzUtils.QUERTZWHY;
			year = "";
			log.info("每分");
			break;
		case QuartzUtils.QUERTZEVERYHOUR:
			hour = QuartzUtils.QUERTZSTAR;
			day = QuartzUtils.QUERTZSTAR;
			month = QuartzUtils.QUERTZSTAR;
			week = QuartzUtils.QUERTZWHY;
			year = "";
			log.info("每时");
			break;
		case QuartzUtils.QUERTZEVERYDAY:
			day = QuartzUtils.QUERTZSTAR;
			month = QuartzUtils.QUERTZSTAR;
			week = QuartzUtils.QUERTZWHY;
			year = "";
			log.info("每天");
			break;
		case QuartzUtils.QUERTZEVERYWEEK:
			day = "?";
			month = QuartzUtils.QUERTZSTAR;
			year = "";
			log.info("每周");
			break;
		case QuartzUtils.QUERTZEVERYMONTH:
			month = QuartzUtils.QUERTZSTAR;
			week = QuartzUtils.QUERTZWHY;
			year = "";
			log.info("每月");
			break;
		case QuartzUtils.QUERTZONETIME:
			log.info("一次");
			week = QuartzUtils.QUERTZWHY;
			break;
		default:
			log.info("其他");
			week = QuartzUtils.QUERTZWHY;
			break;
		}
		
		String temp = second + " " + minute + " " + hour 
						+ " " + day + " " + month + " " + week + " " + year;
		temp = temp.trim();
		
		log.info(">>>Quertz表达式定时规则：" + temp);
		
		return temp;
	}
	
	/**
	 * 获取当前系统时间定时规则
	 * @return
	 */
	public static String getSysNowTimeRule() {
		String second = String.valueOf(DateUtil.getNowSecond());
		String minute = String.valueOf(DateUtil.getNowMinute());
		String hour = String.valueOf(DateUtil.getNowHour());
		String day = String.valueOf(DateUtil.getNowDay());
		String month = String.valueOf(DateUtil.getNowMonth());
		String week = "?";
		String year = String.valueOf(DateUtil.getNowYear());
		
		String temp = second + " " + minute + " " + hour 
						+ " " + day + " " + month + " " + week + " " + year;
		
		log.info(">>>当前系统时间的Quertz表达式定时规则：" + temp);
		
		return temp;
	}
	
	/**
	 * 根据当前传入的日期和经过的天数设置前后一个提醒的天数
	 * @param date
	 * @param day
	 * @return
	 */
	public static String createRule(Date date, Integer day) {
		String toTime = "";
		String dayToDay = "";
		String rule = "" + DateUtil.getMonth(date).toString() + " ? " + DateUtil.getYear(date).toString();
		
		toTime = DateUtil.getSecond(date).toString() + " "
					+ DateUtil.getMinute(date).toString() + " "
					+ DateUtil.getHour(date).toString();
		
		if (day == 0) {
			dayToDay = DateUtil.getDay(date).toString();
		} else {
			Date sDate = DateUtil.getNDay(date, -day);
			Date eDate = DateUtil.getNDay(date, day);
			String sDay = DateUtil.getDay(sDate).toString();
			String eDay = DateUtil.getDay(eDate).toString();
			
			dayToDay = sDay + "/" + eDay;
		}
		
		rule = toTime + " " + dayToDay + " " + rule;
		
		log.info("rule>>>" + rule);
		
		return rule;
	}
	
	/**
	 * 根据当前传入的日期和经过的天数设置提前提醒的天数开始提醒
	 * @param date
	 * @param day
	 * @return
	 */
	public static String createPreDay(Date date, Integer day) {
		String toTime = "";
		String dayToDay = "";
		String rule = "" + DateUtil.getMonth(date).toString() + " ? " + DateUtil.getYear(date).toString();
		
		toTime = DateUtil.getSecond(date).toString() + " "
					+ DateUtil.getMinute(date).toString() + " "
					+ DateUtil.getHour(date).toString();
		
		if (day < 0) {
			Date sDate = DateUtil.getNDay(date, day);
			Date eDate = date;
			String sDay = DateUtil.getDay(sDate).toString();
			String eDay = DateUtil.getDay(eDate).toString();
			
			dayToDay = sDay + "/" + eDay;
		} else if (day > 0) {
			Date sDate = date;
			Date eDate = DateUtil.getNDay(date, day);
			String sDay = DateUtil.getDay(sDate).toString();
			String eDay = DateUtil.getDay(eDate).toString();
			
			dayToDay = sDay + "/" + eDay;
		} else {
			dayToDay = DateUtil.getDay(date).toString();
		}
		
		rule = toTime + " " + dayToDay + " " + rule;
		
		log.info("rule>>>" + rule);
		
		return rule;
	}
}
