package cn.sd.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("all")
public class PropertyUtils {
	
	public static void setValue(Object o, HttpServletRequest r){
		
		List<String> parameter_names = new ArrayList<String>();
		Enumeration<String> e =  r.getParameterNames();
		if(r.getParameterNames() != null){
			while (e.hasMoreElements()) {
				String parameter_name = (String)e.nextElement();
				parameter_names.add(parameter_name);
			}
		}
		
		try {
			Class c = Class.forName(o.getClass().getName());
			
			Class super_c1 = c.getSuperclass();
			if(super_c1.getName().indexOf("cn.jianghu") != -1){
				
			}
			Field[] fs = c.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
	            f.setAccessible(true); //设置些属性是可以访问的
//				Object val = f.get(o); //得到属性值
	            if(parameter_names.size() > 0){
	            	for (String parameter_name : parameter_names) {
	            		if(f.getName().equals(parameter_name)){
	            			if(r.getParameter(parameter_name) != null){
	            				if(f.getType() == int.class){
	            					f.set(o, Integer.parseInt(r.getParameter(parameter_name)));       //给属性设值
	            				}else{
	            					f.set(o, r.getParameter(parameter_name)); 
	            				}
	            			}
						}
					}
	            }
	            f.setAccessible(false);
			}
			
			Class super_c = Class.forName(o.getClass().getSuperclass().getName());
			
			Field[] super_fs = super_c.getDeclaredFields();
			for (int i = 0; i < super_fs.length; i++) {
				Field super_f = super_fs[i];
				super_f.setAccessible(true); //设置些属性是可以访问的
				Object val = super_f.get(o); //得到属性值
//				System.out.println(super_f.getName()+"---"+val);
				if(parameter_names.size() > 0){
	            	for (String parameter_name : parameter_names) {
	            		if(super_f.getName().equals(parameter_name)){
	            			if(r.getParameter(parameter_name) != null){
	            				if(super_f.getType() == int.class){
	            					super_f.set(o, Integer.parseInt(r.getParameter(parameter_name)));       //给属性设值
	            				}else{
	            					super_f.set(o, r.getParameter(parameter_name)); 
	            				}
	            			}
						}
					}
	            }
				super_f.setAccessible(false);
			}
			
			
			Class super_super_c = Class.forName(o.getClass().getSuperclass().getSuperclass().getName());
			
			Field[] super_super_fs = super_super_c.getDeclaredFields();
			for (int i = 0; i < super_super_fs.length; i++) {
				Field super_f = super_super_fs[i];
				super_f.setAccessible(true); //设置些属性是可以访问的
				Object val = super_f.get(o); //得到属性值
//				System.out.println(super_f.getName()+"---"+val);
				if(parameter_names.size() > 0){
	            	for (String parameter_name : parameter_names) {
	            		if(super_f.getName().equals(parameter_name)){
	            			if(r.getParameter(parameter_name) != null){
	            				if(super_f.getType() == int.class){
	            					super_f.set(o, Integer.parseInt(r.getParameter(parameter_name)));       //给属性设值
	            				}else{
	            					super_f.set(o, r.getParameter(parameter_name)); 
	            				}
	            			}
						}
					}
	            }
				super_f.setAccessible(false);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static void setRequestValue(Object o, HttpServletRequest r){
		try {
			Class c = Class.forName(o.getClass().getName());
			
			Field[] fs = c.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
	            f.setAccessible(true); //设置些属性是可以访问的
				Object val = f.get(o); //得到属性值
	            r.setAttribute(f.getName(), val);
	            f.setAccessible(false);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
