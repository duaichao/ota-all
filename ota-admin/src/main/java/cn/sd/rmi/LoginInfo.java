package cn.sd.rmi;

import java.util.HashMap;
import java.util.Map;

public class LoginInfo {

	private static Map<Object, Object> s = new HashMap<Object, Object>();
	private static Map<Object, Object> c = new HashMap<Object, Object>();
	private static Map<Object, Object> u = new HashMap<Object, Object>();
	private static Map<Object, Object> t = new HashMap<Object, Object>();
	
	public static Object o = new Object();
	
	public static Map<Object, Object> getS() {
		return s;
	}
	public static void setS(Map<Object, Object> s) {
		LoginInfo.s = s;
	}
	public static Map<Object, Object> getC() {
		return c;
	}
	public static void setC(Map<Object, Object> c) {
		LoginInfo.c = c;
	}
	public static Map<Object, Object> getU() {
		return u;
	}
	public static void setU(Map<Object, Object> u) {
		LoginInfo.u = u;
	}
	public static Map<Object, Object> getT() {
		return t;
	}
	public static void setT(Map<Object, Object> t) {
		LoginInfo.t = t;
	}
	
	
}
