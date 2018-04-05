package com.lyp.uge.terrain;

import java.util.Random;

import com.lyp.uge.math.MathTools;

public class HeightGenerator {

	public static float AMPLITUTE = 70.0f; // 振幅
	public static float FREQUENCY = 0.25f; // 频率

	private static final int SEEDS_FACTOR_X = 49632;
	private static final int SEEDS_FACTOR_Z = 32517;

	public static int mSeed;
	private Random mRandom = new Random();

	public HeightGenerator() {
		mSeed = mRandom.nextInt(1000000000);
	}

	public float generateHeight(int x, int z) {
		float noise1 = getInterpolateNoise(x * FREQUENCY * 1, z * FREQUENCY * 1) * AMPLITUTE;
		float noise2 = getInterpolateNoise(x * FREQUENCY * 2, z * FREQUENCY * 2) * AMPLITUTE / 3f;
		float noise3 = getInterpolateNoise(x * FREQUENCY * 4, z * FREQUENCY * 4) * AMPLITUTE / 9f;
		float total = noise1 + noise2 + noise3;
		return total;
	}

	public float getNoise(int x, int z) {
		mRandom.setSeed(x * SEEDS_FACTOR_X + z * SEEDS_FACTOR_Z + mSeed); // 增大两坐标间的种子值的差距，避免随机值过于近似
		return mRandom.nextFloat() * 2f - 1f; // [-1, 1]
	}

	private float getSmoothNoise(int x, int z) {
		float lt = getNoise(x - 1, z - 1); // Left top
		float rt = getNoise(x + 1, z - 1); // Right top
		float lb = getNoise(x - 1, z + 1); // Left bottom
		float rb = getNoise(x + 1, z + 1); // Right bottom
		float corners = (lt + lb + rt + rb) / 16f;

		float lm = getNoise(x - 1, z); // Left middle
		float rm = getNoise(x + 1, z); // Right middle
		float tm = getNoise(x, z - 1); // Top middle
		float bm = getNoise(x, z + 1); // Bottom middle
		float sides = (lm + rm + tm + bm) / 8f;

		float center = getNoise(x, z) / 4f;

		float average = corners + sides + center;
		return average;
	}
	
	private float getInterpolateNoise(float x, float z) {
		int xi = (int) x; //整数部分
		int zi = (int) z;
		float fractionalX = x - xi; //小数部分
		float fractionalZ = z - zi;
		
		float p1 = getSmoothNoise(xi, zi);
		float p2 = getSmoothNoise(xi + 1, zi);
		float p3 = getSmoothNoise(xi, zi + 1);
		float p4 = getSmoothNoise(xi + 1, zi + 1);
		float i1 = cosInterpolator(p1, p2, fractionalX);
		float i2 = cosInterpolator(p3, p4, fractionalX);
		float i = cosInterpolator(i1, i2, fractionalZ);
		return i;
	}
	
	private float cosInterpolator(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) / 2f; // Range cos[-1, 1] -> [0, 2] -> [0, 1]
		return MathTools.mix(a, b, f);
	}
}
