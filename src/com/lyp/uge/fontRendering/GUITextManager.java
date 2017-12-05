package com.lyp.uge.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyp.uge.fontMeshCreator.FontType;
import com.lyp.uge.fontMeshCreator.GUIText;
import com.lyp.uge.fontMeshCreator.TextMeshData;
import com.lyp.uge.renderEngine.Loader;

public class GUITextManager {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer fontRenderer;

	public static void init(Loader loader) {
		GUITextManager.loader = loader;
		fontRenderer = new FontRenderer();
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void render() {
		fontRenderer.render(texts);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp() {
		fontRenderer.cleanUp();
	}
}
