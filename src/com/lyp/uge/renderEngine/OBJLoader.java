package com.lyp.uge.renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.model.RawModel;

public class OBJLoader {

	public static RawModel loadObjModel(String fileName, Loader loader) {
		String filePath = "res/obj/" + fileName;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(new File(filePath));
		} catch (FileNotFoundException e) {
			Logger.e("Could not load " + fileName + " file !");
			Logger.e(e);
		}
		Logger.d("Loading", filePath);
		BufferedReader reader = new BufferedReader(fileReader);
		String line = null;
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		float[] verticesArray = null;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indicesArray = null;

		try {
			while (true) {
				line = reader.readLine();
				String[] lineData = line.split(" ");
				if (line.startsWith("v ") && lineData[0].equals("v")) {
					vertices.add(new Vector3f(Float.parseFloat(lineData[1]), Float.parseFloat(lineData[2]), Float.parseFloat(lineData[3])));
				} else if (line.startsWith("vt ") && lineData[0].equals("vt")) {
					textures.add(new Vector2f(Float.parseFloat(lineData[1]), Float.parseFloat(lineData[2])));
				} else if (line.startsWith("vn ") && lineData[0].equals("vn")) {
					normals.add(new Vector3f(Float.parseFloat(lineData[1]), Float.parseFloat(lineData[2]), Float.parseFloat(lineData[3])));
				} else if (line.startsWith("f ") && lineData[0].equals("f")) {
					texturesArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] lineData = line.split(" ");
				String[] v1 = lineData[1].split("/");
				String[] v2 = lineData[2].split("/");
				String[] v3 = lineData[3].split("/");
				processVertex(v1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(v2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(v3, indices, textures, normals, texturesArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			Logger.e(e);
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		int vertexPointer = 0;
		for (Vector3f vertex: vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,
			List<Vector2f> textures, List<Vector3f> normals, float[] texturesArray,
			float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1; //obj file number start from 0, so -1
		indices.add(currentVertexPointer);
		if (vertexData[1].equals("") || vertexData[1] == null) {
			vertexData[1] = "1";
		}
		if (textures.size() == 0) {
			for (int i = 0; i < indices.size(); i++) {
				textures.add(new Vector2f(1, 1));
			}
		}
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		texturesArray[currentVertexPointer * 2] = currentTex.x;
		texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y; //blender start with bottom left, opengl start from top left, so 1-
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
}
