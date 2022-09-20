package com.szbsc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import net.coobird.thumbnailator.Thumbnails;

@SuppressWarnings("restriction")
public class ImgUtil {
    /**
     * 图片转化成base64字符串
     * 
     * @param imgFile
     * @return
     */
	public static String convertImageToBase64Data(File imgFile) {
        if (imgFile == null || !imgFile.exists()) {
            return null;
        }
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if (in != null) {
        		try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        // 对字节数组Base64编码
        return new sun.misc.BASE64Encoder().encode(data);// 返回Base64编码过的字节数组字符串
    }
 
    /**
     * base64字符串转化成图片
     * 
     * @param imgData
     * @return
     */
    public static String generatePngImage(String imgData, String filePath) {
        String fileName = new StringBuilder(UUID.randomUUID().toString()).append(".png").toString();
        if (imgData == null) { // 图像数据为空
            throw new RuntimeException("传输图片数据为空!");
        }
        OutputStream out = null;
        try {
            // Base64解码
            byte[] b = new sun.misc.BASE64Decoder().decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成图片
            out = new FileOutputStream(new StringBuffer(filePath).append(fileName).toString());
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("传输图片数据为空!");
        } finally {
        	if (out != null) {
        		try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return fileName;
    }
    
    /**
     * 缩略图
     * @param sourceFile 源文件
     * @param targetFilePath 目标文件路径
     * @param params 压缩高宽
     */
    public static void thumbnails(File sourceFile, String targetFilePath, int... params) {
    	int width = 600;
    	int height= 450;
    	if (params != null && params.length > 0) {
    		width = params[0];
    	}
    	if (params != null && params.length > 1) {
    		height = params[1];
    	}
    	try {
			Thumbnails.of(sourceFile.getAbsolutePath()).size(width, height).toFile(targetFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}