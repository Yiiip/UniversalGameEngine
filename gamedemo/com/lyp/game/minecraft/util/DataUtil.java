package com.lyp.game.minecraft.util;

public class DataUtil {

	public static final String PATH_RES = "gamedemo/com/lyp/game/minecraft/res/";
	public static final String PATH_TEXTURE = PATH_RES + "texture/";
	
	public static final String TEX_GRASS_WITH_DIRT = PATH_TEXTURE + "tex_grass_with_dirt.png";
	
	/* cube */
	public static float[] CUBE_VERTICES = { //24
			// Front face
			-0.5f,  0.5f,  0.5f, //0
			-0.5f, -0.5f,  0.5f, //1
			0.5f, -0.5f,  0.5f, //2
			0.5f,  0.5f,  0.5f, //3
			// Top face
			-0.5f,  0.5f, -0.5f, //4
			0.5f,  0.5f, -0.5f, //5
			-0.5f,  0.5f,  0.5f, //6 -> 0
			0.5f,  0.5f,  0.5f, //7 -> 3
			// Right face
			0.5f,  0.5f,  0.5f, //8 -> 3
			0.5f,  0.5f, -0.5f, //9 -> 5
			0.5f, -0.5f,  0.5f, //10 -> 2
			0.5f, -0.5f, -0.5f, //11 -> 7
			// Left face
			-0.5f,  0.5f, -0.5f, //12 -> 4
			-0.5f,  0.5f,  0.5f, //13 -> 0
			-0.5f, -0.5f, -0.5f, //14 -> 6
			-0.5f, -0.5f,  0.5f, //15 -> 1
			// Bottom face
			-0.5f, -0.5f,  0.5f, //16 -> 1
			0.5f, -0.5f,  0.5f, //17 -> 2
			-0.5f, -0.5f, -0.5f, //18 -> 6
			0.5f, -0.5f, -0.5f, //19 -> 7
			// Back face
			0.5f,  0.5f, -0.5f, //20 -> 5
			-0.5f,  0.5f, -0.5f, //21 -> 4
			0.5f, -0.5f, -0.5f, //22 -> 7
			-0.5f, -0.5f, -0.5f, //23 -> 6
	};
	public static int[] CUBE_INDICES = { //6 sides, 12 triangles
			0,  1,  2,		0,  2,  3,		// Front face
			4,  6,  7,		4,  7,  5,		// Top face
			8,  10,  9,		9,  10, 11,		// Right face
			12, 14, 15,		15, 13, 12,		// Left face
			17, 18, 19,		16, 18, 17,		// Bottom face
			21, 20, 22,		23, 21, 22,		// Back face
	};
	public static float[] CUBE_TEXTURE_COORDS = { //24
			// front
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			// top
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 1.0f,
			0.5f, 1.0f,
			// right
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			// left
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			// bottom
			0.5f, 0.0f,
			1.0f, 0.0f,
			0.5f, 0.5f,
			1.0f, 0.5f,
			// back
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
	};
	public static float[] CUBE_NORMALS = {
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
	};
}
