package com.lyp.uge.texture;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

import com.lyp.uge.logger.Logger;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {

	private TextureLoader() {
	}
	
	/**
	 * Powered by PNGDecoder
	 * @param fileName
	 */
	public static TextureData decodeFromPNG(String fileName) {
		Logger.d("Loading", fileName);
		int width = 0;
		int height = 0;
		ByteBuffer byteBuffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			byteBuffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(byteBuffer, width * 4, Format.RGBA);
			byteBuffer.flip();
			in.close();
		} catch (Exception e) {
			Logger.e("Could not decode texture flie! " + fileName);
			Logger.e(e);
		}
		return new TextureData(width, height, byteBuffer);
	}
}
