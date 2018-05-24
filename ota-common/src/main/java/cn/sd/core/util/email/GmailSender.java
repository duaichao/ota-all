package cn.sd.core.util.email;

import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 使用gmail发用邮件
 * 
 */
@SuppressWarnings({"finally"})
public class GmailSender {
	
	/**
	 * 发送有邮件 必须使用gmail作为发送邮件的邮箱
	 * @param parameters 
	 * email 			发送邮件的邮箱地址
	 * userName 		发送邮件的邮箱用户名
	 * password 		发送邮件的邮箱密码
	 * toEmail 			接受邮件的邮箱地址
	 * title 			邮件标题
	 * content 			邮件内容
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static Map<Object, Object> sender(Map<String, Object> parameters){
		Map<Object, Object> result = new HashMap<Object, Object>();
		String email = (String)parameters.get("email"); 
		final String userName = (String)parameters.get("userName"); 
		final String password = (String)parameters.get("password"); 
		String toEmail = (String)parameters.get("toEmail"); 
		String title = (String)parameters.get("title"); 
		String content = (String)parameters.get("content"); 
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props,
			new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});
		Message msg = new MimeMessage(session);
		try {
			msg.setContent(content, "text/html; charset=utf-8"); 
			msg.setFrom(new InternetAddress(email));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			msg.setSubject(title);
//			msg.setText(content);
			msg.setSentDate(new Date());
			Transport.send(msg);
			result.put("isSuccess", "true");
		} catch (AddressException e){
			e.printStackTrace();
			result.put("isSuccess", "false");
		} catch (MessagingException e){
			e.printStackTrace();
			result.put("isSuccess", "false");
		}finally{
			return result;
		}
	}
}
