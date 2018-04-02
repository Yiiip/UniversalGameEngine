package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL33;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.texture.CubeMapTexture;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.BufferUtils;

public class Loader {
	
	public static final int ATTR_POSITIONS = 0;
	public static final int ATTR_COORDINATES = 1;
	public static final int ATTR_NORMALS = 2;
	
	//store id
	private List<Integer> vaos;
	private List<Integer> vbos;
	private List<Integer> textures;
	
	//cache java object
	private Map<String, Texture> textureCache;
	
	public Loader() {
		vaos = new ArrayList<Integer>();
		vbos = new ArrayList<Integer>();
		textures = new ArrayList<Integer>();
		textureCache = new HashMap<>();
	}

	public RawModel loadToVAO(float[] positions, int dimension) {
		int vaoID = createVAO();
		storeDataInAttributeList(ATTR_POSITIONS, dimension, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/dimension);
	}
	
	public RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
		storeDataInAttributeList(ATTR_POSITIONS, 3, positions);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
		storeDataInAttributeList(ATTR_POSITIONS, 3, positions);
		storeDataInAttributeList(ATTR_COORDINATES, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		storeIndicesBuffer(indices);
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
		if (textureCache.containsKey(filePath) && textureCache.get(filePath) != null) {
			return textureCache.get(filePath);
		}
		Texture texture = new Texture(filePath);
		int textureID = texture.getID();
		textures.add(textureID);
		textureCache.put(filePath, texture); //TODO 缓存键名
		return texture;
	}
	
	public CubeMapTexture loadTextureCubeMap(String[] textureFiles) {
		CubeMapTexture cubeMapTexture = new CubeMapTexture(textureFiles);
		int textureID = cubeMapTexture.getID();
		textures.add(textureID);
		return cubeMapTexture;
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
		FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, count, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vbos.add(vboID);
	}
	
	private void storeIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers(); //创建缓冲区
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID); //绑定缓冲区
		IntBuffer dataBuffer = BufferUtils.createIntBuffer(indices); 
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW); //存储缓冲区数据
		vbos.add(vboID);
	}
	
	private void unbindVAO() {
		glBindVertexArray(0);
	}
	
	public int createVBO(int floatCount) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, floatCount * 4, GL_STREAM_DRAW); //float -> 4 byte
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	public void addInstancedAttribute(int vao, int vbo, int attributeIndex, int dataSize, int instancedDataLength, int offsetPointer) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindVertexArray(vao);
		glVertexAttribPointer(attributeIndex, dataSize, GL_FLOAT, false, instancedDataLength * 4, offsetPointer * 4); //float -> 4 byte
		GL33.glVertexAttribDivisor(attributeIndex, 1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void updateVBO(int vbo, float[] data, FloatBuffer floatBuffer) {
		floatBuffer.clear();
		floatBuffer.put(data);
		floatBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, floatBuffer.capacity() * 4, GL_STREAM_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, floatBuffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
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
		textureCache.clear();
	}
}
