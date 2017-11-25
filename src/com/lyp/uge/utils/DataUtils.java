package com.lyp.uge.utils;

public class DataUtils {

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
}
