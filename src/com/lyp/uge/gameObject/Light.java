package com.lyp.uge.gameObject;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.ShaderProgram;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

import static com.lyp.uge.input.Keyboard.*;

public class Light extends GameObject {

	private Vector3f color;

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		this.speed = 0.06f;
	}
	
	public Light(Vector3f position, Vector3f color, Loader loader) {
		this(position, color);
		RawModel rawModel = OBJLoader.loadObjModel(DataUtils.OBJ_SPHERE_LOW_QUALITY, loader);
		Texture tex = loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_YELLOW_GRAY);
		tex.setShineDamper(4.0f);
		tex.setReflectivity(10.0f);
		this.model = new TextureModel(rawModel, tex);
		this.scale = 0.6f;
	}
	
	@Override
	public void update() {
		if (isKeyPressed(KEY_I)) {
			position.y += speed;
		}
		if (isKeyPressed(KEY_L)) {
			position.x += speed;
		}
		if (isKeyPressed(KEY_J)) {
			position.x -= speed;
		}
		if (isKeyPressed(KEY_K)) {
			position.y -= speed;
		}
		if (isKeyPressed(KEY_P)) {
			position.z -= speed;
		}
		if (isKeyPressed(KEY_SEMICOLON)) {
			position.z += speed;
		}
	}

	@Override
	public void render(Renderer renderer, ShaderProgram shader) {
		glCullFace(GL_FRONT); //变为渲染背面
		renderer.render(this, (StaticShader) shader);
		glCullFace(GL_BACK); //变为渲染正面
	}

	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
}
