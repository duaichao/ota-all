/*
 * 说明：
 * @author qyh
 * @version 1.0
 * 创建日期 2006-1-20 14:29:48
 */
package cn.sd.core.util.encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 说明：
 * @author qyh
 * @version 1.0
 * 创建日期 2006-1-20 14:29:48
 */
public class EncodeUtil {
	public static String Encrypt(String strSrc, String encName) {
		//parameter strSrc is a string will be encrypted,
		//parameter encName is the algorithm name will be used.
		//encName dafault to "MD5"
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); //to HexString
		} catch (NoSuchAlgorithmException e) {
			//System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	public static void main(String[] args) {
		String strSrc = "8002";
		System.out.println("Source String:" + strSrc);
		System.out.println("Encrypted String:");
		System.out.println("Use Def:" + EncodeUtil.Encrypt(strSrc, null));
		System.out.println("Use MD5:" + EncodeUtil.Encrypt(strSrc, "MD5"));
		System.out.println("Use SHA:" + EncodeUtil.Encrypt(strSrc, "SHA-1"));
		System.out.println("Use SHA-256:" + EncodeUtil.Encrypt(strSrc, "SHA-256"));
	}
}
