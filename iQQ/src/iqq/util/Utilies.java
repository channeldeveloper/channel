/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package iqq.util;

import java.io.File;
import java.text.DecimalFormat;

public class Utilies {

	public static final String TEMP_DIR = System.getProperty("java.io.tmpdir"); // 临时目录

	public static final int IMAGE_WIDTH = 300, IMAGE_HEIGHT = 225;//图片的宽、高度(4:3)
	public static final int MAX_IMAGE_WIDTH = 700, MAX_IMAGE_HEIGHT = 525;//最大图片的宽、高度(4:3)
	public static final DecimalFormat IMAGE_FORMATTER = new DecimalFormat("0.00");

	/**
	 * get email attachment temp store dir
	 * 
	 * @param fileID
	 * @param fileName
	 * @return
	 */
	public static String getTempDir(Object fileID, String fileName) {
		// int suffix = fileName.lastIndexOf(".");

		// return new File(TEMP_DIR, fileID
		// + (suffix == -1 ? "" :
		// fileName.substring(suffix))).toURI().toString();
		return new File(TEMP_DIR, fileName).toURI().toString();
	}

	/**
	 * 自动缩放图片的大小
	 * 
	 * @param oldWidth
	 * @param oldHeight
	 * @return 缩小后的大小，分别为宽度、高度、缩放比例
	 */
	public static String[] scale(int oldWidth, int oldHeight) {
		// 如果设定宽、高度和图片原来的宽、高度一致，就不需要调整了
		if (oldWidth == IMAGE_WIDTH && oldHeight == IMAGE_HEIGHT)
			return new String[] { "" + oldWidth, "" + oldHeight, "1.0" };

		double d1 = oldWidth / (double) oldHeight, 
				d2 = IMAGE_WIDTH / (double) IMAGE_HEIGHT;

		double scale = 1.0d;
		String scaleValue = "1.0";
		if (d2 >= d1) { // 以高度为准
			if (oldHeight > IMAGE_HEIGHT) {
				scale = IMAGE_HEIGHT / (double) oldHeight;
				scaleValue = IMAGE_FORMATTER.format(scale);

				oldHeight = IMAGE_HEIGHT;
				oldWidth = (int) (oldHeight * d1);
			}
		} else {// 以宽度为准
			if (oldWidth > IMAGE_WIDTH) {
				scale = IMAGE_WIDTH / (double) oldWidth;
				scaleValue = IMAGE_FORMATTER.format(scale);

				oldWidth = IMAGE_WIDTH;
				oldHeight = (int) (oldWidth / d1);
			}
		}
		return new String[] { "" + oldWidth, "" + oldHeight, scaleValue };
	}

	/**
	 * 还原原始图片的宽、高度
	 * @param scales
	 * @return
	 */
	public static int[] unScale(String[] scales)
	{
		if(scales != null && scales.length == 3)
		{
			 int width = Integer.parseInt(scales[0]),
        			 height = Integer.parseInt(scales[1]);
			 float scale = Float.parseFloat(scales[2]);
        	 if(scale == 1.0f)
        		 return new int[]{width, height};
        	 
        	 //计算原始宽、高度
        	 width = (int)(width / scale);
        	 height = (int)(height/ scale);
        	 
        	 if(width > MAX_IMAGE_WIDTH) {
        		 height = MAX_IMAGE_WIDTH * height / width;
        		 width = MAX_IMAGE_WIDTH;
        	 }
        	 return new int[]{width, height};
		}
		return null;
	}

}
