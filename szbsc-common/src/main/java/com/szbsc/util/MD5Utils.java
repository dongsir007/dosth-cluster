package com.szbsc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("restriction")
public class MD5Utils {
	
	/**
	 * 编码
	 * @param text 文本
	 * @return
	 */
	public static String compress(String text) {
		if (text == null || "".equals(text)) {
			return text;
		}
		return org.apache.commons.codec.binary.Base64.encodeBase64String(text.getBytes());
	}
	
	/**
	 * 解码
	 * @param text
	 * @return
	 */
	public static String uncompress(String text) {
		if (text == null || "".equals(text)) {
			return null;
		}
		return new String(org.apache.commons.codec.binary.Base64.decodeBase64(text));
	}

	/**
	 * 压缩
	 * @param text
	 * @return
	 */
	public static String gzip(String text) {
		if (text == null || "".equals(text)) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out, 1024);
			gzip.write(text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
	}

	/**
	 * 解压
	 * @param text
	 * @return
	 */
	public static String ungzip(String text) {
		if (text == null || "".equals(text)) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream gzipin = null;
		byte[] compressed = null;
		String decompressed = null;
		
		try {
			compressed = new sun.misc.BASE64Decoder().decodeBuffer(text);
			in = new ByteArrayInputStream(compressed);
			gzipin = new GZIPInputStream(in);
			byte[] buf = new byte[1024];
			int offset;
			while ((offset = gzipin.read(buf)) != -1) {
				out.write(buf, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzipin != null) {
				try {
					gzipin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return decompressed;
	}
}