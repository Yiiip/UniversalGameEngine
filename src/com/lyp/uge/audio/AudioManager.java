package com.lyp.uge.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.camera.Camera;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class AudioManager {
	
	private static AudioManager mInstance;

	private long mDevice;

	private long mContext;

	private AudioListener mListener;

	private final List<AudioBuffer> soundBufferList;

	private final Map<String, AudioSource> soundSourceMap;

	private AudioManager() {
		soundBufferList = new ArrayList<>();
		soundSourceMap = new HashMap<>();
	}
	
	public static AudioManager GetInstance()
	{
		if (mInstance == null) {
			mInstance = new AudioManager();
			mInstance.init();
		}
		return mInstance;
	}

	private void init() {
		this.mDevice = alcOpenDevice((ByteBuffer) null);
		if (mDevice == NULL) {
			throw new IllegalStateException("Failed to open the default OpenAL device.");
		}
		ALCCapabilities deviceCaps = ALC.createCapabilities(mDevice);
		this.mContext = alcCreateContext(mDevice, (IntBuffer) null);
		if (mContext == NULL) {
			throw new IllegalStateException("Failed to create OpenAL context.");
		}
		alcMakeContextCurrent(mContext);
		AL.createCapabilities(deviceCaps);
	}

	public void addSoundSource(String name, AudioSource soundSource) {
		this.soundSourceMap.put(name, soundSource);
	}

	public AudioSource getSoundSource(String name) {
		return this.soundSourceMap.get(name);
	}

	public void playSoundSource(String name) {
		AudioSource soundSource = this.soundSourceMap.get(name);
		if (soundSource != null && !soundSource.isPlaying()) {
			soundSource.play();
		}
	}

	public void removeSoundSource(String name) {
		this.soundSourceMap.remove(name);
	}

	public void addSoundBuffer(AudioBuffer soundBuffer) {
		this.soundBufferList.add(soundBuffer);
	}

	public AudioListener getListener() {
		return this.mListener;
	}

	public void setListener(AudioListener listener) {
		this.mListener = listener;
	}

	public void updateListenerPosition(Camera camera) { //TODO
		mListener.setPosition(camera.getPosition());
		Vector3f facing = camera.getFacingVector();
		Vector3f up = camera.getUpwardVector();
		mListener.setOrientation(facing, up);
	}

	public void setAttenuationModel(int model) {
		alDistanceModel(model);
	}
	
	public void pauseAll() {
		for (AudioSource soundSource : soundSourceMap.values()) {
			soundSource.pause();
		}
	}
	
	public void playAll() {
		for (AudioSource soundSource : soundSourceMap.values()) {
			if (!soundSource.isPlaying()) {
				soundSource.play();
			}
		}
	}

	public void cleanup() {
		for (AudioSource soundSource : soundSourceMap.values()) {
			soundSource.cleanup();
		}
		soundSourceMap.clear();
		for (AudioBuffer soundBuffer : soundBufferList) {
			soundBuffer.cleanup();
		}
		soundBufferList.clear();
		if (mContext != NULL) {
			alcDestroyContext(mContext);
		}
		if (mDevice != NULL) {
			alcCloseDevice(mDevice);
		}
	}
}
