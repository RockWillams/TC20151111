package com.taichang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil
{
	private SecurityUtil()
	{
	}

	/**
	 * @Title: MD5Encode
	 * @Description: MD5加密，主要用于密码文本加密
	 * @return: String
	 * @param origin
	 * @author: Psyche
	 * @date: 2015年8月10日
	 */
	public final static String MD5Encode(String origin)
	{
		String result = null;
		try
		{
			result = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byteToString(md.digest(origin.getBytes()));
		} catch (NoSuchAlgorithmException ex)
		{
			ex.printStackTrace();
		}
		return result;
	}

	private final static String[] strDigits =
	{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	// 返回形式为数字跟字符串
	private final static String byteToArrayString(byte bByte)
	{
		int iRet = bByte;
		if (iRet < 0)
		{
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 转换字节数组为16进制字串
	private final static String byteToString(byte[] bByte)
	{
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++)
		{
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}
}
