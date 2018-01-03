package com.lyp.game.minecraft.util;

import static com.lyp.game.minecraft.util.WorldConstants.*;

import java.util.Random;

public class NoiseGenerator {

	private int mSeed;
	private NoiseParameters mNoiseParameters;

	public NoiseGenerator(int seed) {
		mSeed = seed;
		mNoiseParameters = new NoiseParameters();
		mNoiseParameters.octaves = 7;
		mNoiseParameters.amplitude = 70;
		mNoiseParameters.smoothness = 235;
		mNoiseParameters.heightOffset = -5;
		mNoiseParameters.roughness = 0.53;
	}

	private double getNoise(double n) {
		int intN = (int) n;
		intN += mSeed;
		intN = (intN << 13) ^ intN;
		double newN = (intN * (intN * intN * 60493 + 19990303) + 1376312589) & 0x7fffffff;
		return 1.0 - (newN / 1073741824.0);
	}

	private double getNoise(double x, double z) {
		return getNoise(x + z * 57);
	}

	private double lerp(double a, double b, double z) {
		double mu2 = (1 - Math.cos(z * 3.14)) / 2;
		return (a * (1 - mu2) + b * mu2);
	}

	private double noise(double x, double z) {
		double floorX = x; // This is kinda a cheap way to floor a double integer.
		double floorZ = z;

		double s = getNoise(floorX, floorZ);
		double t = getNoise(floorX + 1, floorZ);
		double u = getNoise(floorX, floorZ + 1);// Get the surrounding values to calculate the transition.
		double v = getNoise(floorX + 1, floorZ + 1);

		double rec1 = lerp(s, t, x - floorX);// Interpolate between the values.
		double rec2 = lerp(u, v, x - floorX);// Here we use x-floorX, to get 1st dimension. Don't mind the x-floorX
		double rec3 = lerp(rec1, rec2, z - floorZ);// Here we use y-floorZ, to get the 2nd dimension.
		return rec3;
	}

	public double getHeight(int x, int z, int chunkX, int chunkZ) {
		int newX = (x + (chunkX * CHUNK_SIZE));
		int newZ = (z + (chunkZ * CHUNK_SIZE));

		if (newX < 0 || newZ < 0) { return WATER_LEVEL - 1; }

		double totalValue = 0.0;

		for (int a = 0; a < mNoiseParameters.octaves - 1; a++) { // This loops trough the octaves.
			double frequency = Math.pow(2.0, a); // This increases the frequency with every loop of the octave.
			double amplitude = Math.pow(mNoiseParameters.roughness, a); // This decreases the amplitude with every loop of the octave.
			totalValue += noise(
					newX * frequency / mNoiseParameters.smoothness,
					newZ * frequency / mNoiseParameters.smoothness) * amplitude;
		}

		double val = (((totalValue / 2.1) + 1.2) * mNoiseParameters.amplitude) + mNoiseParameters.heightOffset;

		return val > 0.0 ? val : 1.0;
	}

	public void setNoiseParameters(NoiseParameters noiseParameters) {
		this.mNoiseParameters = noiseParameters;
	}

	class NoiseParameters {
		int octaves;
		int amplitude;
		int smoothness;
		int heightOffset;
		double roughness;
	}
	
	public static void main(String[] args) {
		int seed = new Random().nextInt(32767);
		NoiseGenerator noiseGen = new NoiseGenerator(seed);
		System.out.println("seed = " + seed);
		for (int x = 0; x < CHUNK_SIZE + 1; x++) {
		    for (int z = 0; z < CHUNK_SIZE + 1; z++) {
		        int h = (int) noiseGen.getHeight(x, z, x, z);
		        for (int i = 0; i < h; i++) {
		        	System.out.print("#");
				}
		        System.out.print("\n");
		    }
		}
	}
}
