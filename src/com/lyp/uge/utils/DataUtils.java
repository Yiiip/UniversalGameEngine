package com.lyp.uge.utils;

public class DataUtils {
	
	public static final String OBJ_DRAGON = "dragon.obj";
	public static final String OBJ_RABBIT = "rabbit.obj"; //斯坦福兔
	public static final String OBJ_TREE = "tree.obj";
	public static final String OBJ_CUBE = "cube.obj";
	public static final String OBJ_BILLBOARD = "billboard.obj";
	public static final String OBJ_STALL = "stall.obj";
	public static final String OBJ_F16 = "f16.obj";
	public static final String OBJ_SUZANNE = "suzanne.obj"; //猴子
	public static final String OBJ_ARMADILLO = "armadillo.obj"; //怪兽
	public static final String OBJ_TYRA = "tyra.obj"; //恐龙
	
	public static final String TEX_COLOR_YELLOW_GRAY = "color_yellow_gray.png";
	public static final String TEX_COLOR_LIGHT_GRAY = "color_light_gray.png";
	public static final String TEX_GRASS = "grass.png";
	public static final String TEX_IMAGE_PREVIEW = "image_preview.png";
	public static final String TEX_MC_DIRT_GRASS = "mc_dirt_grass.png";
	public static final String TEX_TREE = "tree.png";
	public static final String TEX_FLAPPY_BIRD = "flappy_bird.png";
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
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,		
			
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,
			
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
	};
	public static float[] CUBE_TEXTURE_COORDS = {
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			
			0, 0,
			0, 1,
			1, 1,
			1, 0,
			
			0, 0,
			0, 1,
			1, 1,
			1, 0
	};
	public static int[] CUBE_INDICES = {
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6,
			8,9,11,
			11,9,10,
			12,13,15,
			15,13,14,	
			16,17,19,
			19,17,18,
			20,21,23,
			23,21,22
	};
	public static float[] CUBE_NORMALS = { //TODO need to calculate normal
			-1, 0, 0,
			1, 0, 0,
			0, -1, 0,
			0, 1, 0,
			0, 0, -1,
			0, 0, 1
	};
}
