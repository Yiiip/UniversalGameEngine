package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.window.WindowManager;

/**
 * 渲染GameObject对象
 * @author LYP
 *
 */
public class Renderer2d {
	
	private static float FIELD_OF_VIEW_ANGLE = 45;
	private static float NEAR_PLANE = 0.01f;
	private static float FAR_PLANE = 1000.0f;

	private Matrix4f mProjectionMatrix;
	
	public Renderer2d() {
		if (Global.mode_culling_back) { RendererManager.enableCulling();}
		this.mProjectionMatrix = MathTools.createProjectionMatrix(FIELD_OF_VIEW_ANGLE, NEAR_PLANE, FAR_PLANE, (float) WindowManager.getWindowWidth(), (float) WindowManager.getWindowHeight());
	}

	public void render(Map<TextureModel, List<Sprite2D>> objects, Camera camera) {
		for (TextureModel textureModel : objects.keySet()) {
			
			prepareTextureModel(textureModel);
			
			List<Sprite2D> tempObjs = objects.get(textureModel);
			for (Sprite2D tempObj : tempObjs) {
				tempObj.getShader().start();
				tempObj.getShader().loadViewMatrix(camera);
				prepareInstance(tempObj);
				glDrawElements(GL_TRIANGLES, textureModel.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
				tempObj.getShader().stop();
			}
			unbindTextureModel();
		}
	}
	
	private void prepareTextureModel(TextureModel textureModel) {
		RawModel rawModel = textureModel.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		
		if (textureModel.getTexture() != null) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getID());
		}
	}
	
	private void prepareInstance(Sprite2D object) {
		Matrix4f transformationMatrix = MathTools.createTransformationMatrix(
				object.getPosition(), 
				object.getRotateX(), 
				object.getRotateY(), 
				object.getRotateZ(), 
				object.getScale());
		object.getShader().loadProjectionMatrix(mProjectionMatrix);
		object.getShader().loadModelMatrix(transformationMatrix);
		object.render(null, object.getShader());
	}

	private void unbindTextureModel() {
		if (Global.mode_culling_back) { RendererManager.enableCulling();}
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glBindVertexArray(0);
	}
}
