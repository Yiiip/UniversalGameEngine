package com.lyp.uge.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.lyp.uge.logger.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		glValidateProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String varName) {
		glBindAttribLocation(programID, attribute, varName);
	}
	
	public void start() {
		glUseProgram(programID);
	}
	
	public void stop() {
		glUseProgram(0);
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSrcCode = new StringBuilder();
		int lineNumber = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSrcCode.append(line).append("\n");
				lineNumber++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			Logger.e("Could not find shader file !");
			Logger.e(e);
			System.exit(-1);
		} catch (IOException e) {
			Logger.e("Could not read shader file !");
			Logger.e(e);
			System.exit(-1);
		}
		Logger.d("Loading", file + " [" + lineNumber + " lines]");
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSrcCode);
		glCompileShader(shaderID);
		if (GL_FALSE == glGetShaderi(shaderID, GL_COMPILE_STATUS)) {
			Logger.w(glGetShaderInfoLog(shaderID, 500));
			Logger.e("Could not compile shader source code !");
		}
		return shaderID;
	}
	
	public void cleanUp() {
		stop();
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}
}
