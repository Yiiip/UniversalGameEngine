package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.BufferUtils;

public class Loader {
	
	public static final int ATTR_POSITIONS = 0;
	public static final int ATTR_COORDINATES = 1;
	public static final int ATTR_NORMALS = 2;
	
	private List<Integer> vaos;
	private List<Integer> vbos;
	private List<Integer> textures;
	
	public Loader() {
		vaos = new ArrayList<Integer>();
		vbos = new ArrayList<Integer>();
		textures = new ArrayList<Integer>();
	}

	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(ATTR_POSITIONS, 3, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/3);
	}
	
	public RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(ATTR_POSITIONS, 3, positions);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(ATTR_POSITIONS, 3, positions);
		storeDataInAttributeList(ATTR_COORDINATES, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(ATTR_POSITIONS, 3, positions);
		storeDataInAttributeList(ATTR_COORDINATES, 2, textureCoords);
		storeDataInAttributeList(ATTR_NORMALS, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public int loadToVAO(float[] positions, float[] textureCoords) { //used for font
		int vaoID = createVAO();
		storeDataInAttributeList(ATTR_POSITIONS, 2, positions);
		storeDataInAttributeList(ATTR_COORDINATES, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	public Texture loadTexture(String filePath) {
		Texture texture = new Texture(filePath);
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return texture;
	}
	
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		vaos.add(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int count, float[] data) {
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, count, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vbos.add(vboID);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = BufferUtils.createIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		vbos.add(vboID);
	}
	
	private void unbindVAO() {
		glBindVertexArray(0);
	}
	
	public void cleanUp() {
		for (Integer vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (Integer vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		for (Integer t : textures) {
			glDeleteBuffers(t);
		}
	}
}
