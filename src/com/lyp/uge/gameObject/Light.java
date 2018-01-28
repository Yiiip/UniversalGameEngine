package com.lyp.uge.gameObject;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

import static com.lyp.uge.input.Keyboard.*;

public class Light extends GameObject {

	protected Vector3f lightColor;
	protected Vector3f lightAttenuation = new Vector3f(1.f, 0.f, 0.f); //光的衰减，lightAttenuation=(attenu1)+(attenu2*d)+(attenu3*d*d)，d = distance from light
	
	protected boolean renderSelf = true; //是否将光源自己可视化

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.lightColor = color;
		this.speed = 0.06f;
	}
	
	public Light(Vector3f position, Vector3f color, Loader loader) {
		this(position, color);
		
		Texture tex = loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_YELLOW_GRAY)
				.setShineDamper(4.0f)
				.setReflectivity(10.0f);
		this.model = new TextureModel(OBJLoader.loadObjModel(DataUtils.OBJ_SPHERE_LOW_QUALITY, loader), tex); //光源自己的可视模型
		this.scale = 0.65f;
	}
	
	public Light(Vector3f position, Vector3f color, Loader loader, boolean renderSelf) {
		this(position, color, loader);
		this.renderSelf = renderSelf;
	}
	
	public Light(Vector3f position, Vector3f color, Loader loader, Vector3f attenuation) {
		this(position, color, loader);
		this.lightAttenuation = attenuation;
	}
	
	public Light(Vector3f position, Vector3f color, Loader loader, Vector3f attenuation, boolean renderSelf) {
		this(position, color, loader);
		this.lightAttenuation = attenuation;
		this.renderSelf = renderSelf;
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
	public void render(Renderer renderer, Shader shader) {
		if (this.renderSelf) {
			glCullFace(GL_FRONT); //变为渲染背面
			renderer.render(this, (StaticShader) shader);
			glCullFace(GL_BACK); //变为渲染正面
		}
	}
	
	@Override
	public void destory() {
	}
	
	public void setRenderSelf(boolean renderSelf) {
		this.renderSelf = renderSelf;
	}

	public Vector3f getColor() {
		return lightColor;
	}
	
	public void setColor(Vector3f color) {
		this.lightColor = color;
	}
	
	public Vector3f getLightAttenuation() {
		return lightAttenuation;
	}
	
	public void setLightAttenuation(Vector3f lightAttenuation) {
		this.lightAttenuation = lightAttenuation;
	}
}
