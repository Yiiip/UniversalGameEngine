package com.lyp.uge.utils;

public class DataUtils {
	
	public static final String OBJ_DRAGON = "dragon.obj";
	public static final String OBJ_RABBIT = "rabbit.obj"; //斯坦福兔
	public static final String OBJ_TREE = "tree.obj";
	public static final String OBJ_TREE_BIG = "treeBig.obj";
	public static final String OBJ_CUBE = "cube.obj";
	public static final String OBJ_CUBE2 = "cube2.obj";
	public static final String OBJ_SPHERE_LOW_QUALITY = "sphere_low_quality.obj";
	public static final String OBJ_SPHERE_HIGH_QUALITY = "sphere_high_quality.obj";
	public static final String OBJ_BILLBOARD = "billboard.obj";
	public static final String OBJ_STALL = "stall.obj";
	public static final String OBJ_F16 = "f16.obj";
	public static final String OBJ_SUZANNE = "suzanne.obj"; //猴子
	public static final String OBJ_ARMADILLO = "armadillo.obj"; //怪兽
	public static final String OBJ_TYRA = "tyra.obj"; //恐龙
	public static final String OBJ_GRASS_REAL = "grassRealModel.obj"; //小草
	public static final String OBJ_FERN = "fern.obj"; //蕨类植物
	public static final String OBJ_WINE_GLASS = "WineGlass.obj"; //高脚杯
	public static final String OBJ_CRATE = "crate.obj"; //板条箱
	
	public static final String TEX_COLOR_YELLOW_GRAY = "color_yellow_gray.png";
	public static final String TEX_COLOR_LIGHT_GRAY = "color_light_gray.png";
	public static final String TEX_GRASS = "grass.png";
	public static final String TEX_GRASS2 = "grass2.png";
	public static final String TEX_GRASS_REAL = "grass_real.png";
	public static final String TEX_IMAGE_PREVIEW = "image_preview.png";
	public static final String TEX_MC_DIRT_GRASS = "mc_dirt_grass.png";
	public static final String TEX_MC_CUBE = "mc_cube_texture.png";
	public static final String TEX_TREE = "tree.png";
	public static final String TEX_TREE_BIG = "treeBig.png";
	public static final String TEX_FERN = "fern.png";
	public static final String TEX_FLOWER = "flower.png";
	public static final String TEX_STALL = "stallTexture.png";
	public static final String TEX_FLAPPY_BIRD = "flappy_bird.png";
	public static final String TEX_GROUND01 = "ground01.png";
	
	public static final String TEX_FONT_PRODUCT_SANS = "font_ProductSans.png";
	public static final String TEX_FONT_PIXELED = "font_Pixeled.png";
	public static final String TEX_FONT_ARIAL = "font_Arial.png";
	
	public static final String FONT_PRODUCT_SANS = "font_ProductSans.fnt";
	public static final String FONT_PIXELED = "font_Pixeled.fnt";
	public static final String FONT_ARIAL = "font_Arial.fnt";

	public static float[] RECT_VERTICES = {
		-0.5f, 0.5f, 0, //V0
		-0.5f, -0.5f, 0,//V1 
		0.5f, -0.5f, 0, //V2
		0.5f, 0.5f, 0f  //V3
	};
	public static int[] RECT_INDICES = {
		0, 1, 3, 
		3, 1, 2 
	};
	public static float[] RECT_TEXTURE_COORDS = {
		0, 0, //V0
		0, 1, //V1
		1, 1, //V2
		1, 0  //V3
	};
	
	public static float[] CUBE_VERTICES = {
			// V0 <-
		    -0.5f,  0.5f,  0.5f,
		    // V1 <-
		    -0.5f, -0.5f,  0.5f,
		    // V2 <-
		    0.5f, -0.5f,  0.5f,
		    // V3 <-
		     0.5f,  0.5f,  0.5f,
		    // V4 <-
		    -0.5f,  0.5f, -0.5f,
		    // V5 <-
		     0.5f,  0.5f, -0.5f,
		    // V6 <-
		    -0.5f, -0.5f, -0.5f,
		    // V7 <-
		     0.5f, -0.5f, -0.5f,
	};
	public static int[] CUBE_INDICES = {
			// Front face
		    0, 1, 3, 3, 1, 2,
		    // Top Face
		    4, 0, 3, 5, 4, 3,
		    // Right face
		    3, 2, 7, 5, 3, 7,
		    // Left face
		    6, 1, 0, 6, 0, 4,
		    // Bottom face
		    2, 1, 6, 2, 6, 7,
		    // Back face
		    7, 6, 4, 7, 4, 5,
	};
	public static float[] CUBE_TEXTURE_COORDS = {
			// Front
		    0.0f,  0.0f,
		    1.0f,  0.0f,
		    1.0f,  1.0f,
		    0.0f,  1.0f,
		    // Back
		    0.0f,  0.0f,
		    1.0f,  0.0f,
		    1.0f,  1.0f,
		    0.0f,  1.0f,
		    // Top
		    0.0f,  0.0f,
		    1.0f,  0.0f,
		    1.0f,  1.0f,
		    0.0f,  1.0f,
		    // Bottom
		    0.0f,  0.0f,
		    1.0f,  0.0f,
		    1.0f,  1.0f,
		    0.0f,  1.0f,
		    // Right
		    0.0f,  0.0f,
		    1.0f,  0.0f,
		    1.0f,  1.0f,
		    0.0f,  1.0f,
		    // Left
		    0.0f,  0.0f,
		    1.0f,  0.0f,
		    1.0f,  1.0f,
		    0.0f,  1.0f,
	};
	public static float[] CUBE_NORMALS = { //TODO need to calculate normal
			-1, 0, 0,
			1, 0, 0,
			0, -1, 0,
			0, 1, 0,
			0, 0, -1,
			0, 0, 1
	};
	
	public static float[] CUBE_VERTICES_II = { //24
			// Front face
			-0.5f,  0.5f,  0.5f,  //0
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
	public static int[] CUBE_INDICES_II = { //6 sides, 12 triangles
			0,  1,  2,			0,  2,  3,			// Front face
			4,  6,  7,			4,  7,  5,			// Top face
			8,  10,  9,		9,  10, 11,		// Right face
			12, 14, 15,		15, 13, 12,		// Left face
			17, 18, 19,		16, 18, 17,		// Bottom face
			21, 20, 22,		23, 21, 22,		// Back face
	};
	public static float[] CUBE_TEXTURE_COORDS_II = { //24
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
}
