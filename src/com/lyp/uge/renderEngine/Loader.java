package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.lyp.uge.utils.BufferUtils;

public class Loader {
	
	private List<Integer> vaos;
	private List<Integer> vbos;
	
	public Loader() {
		vaos = new ArrayList<>();
		vbos = new ArrayList<>();
	}

	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/3);
	}
	
	public RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		vaos.add(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, 3, GL_FLOAT, false, 0, 0);
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
	}
}
