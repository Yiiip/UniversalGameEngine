package com.lyp.uge.prefab;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class PrefabsManager {

	//缩进表示JSON中的结构关系
	private static final String KEY_LOAD = "load";
	private static final String KEY_PATH_OBJ = "path_obj";
	private static final String KEY_PATH_TEX = "path_tex";
	private static final String KEY_DATA = "data";
		private static final String KEY_NAME = "name";
		private static final String KEY_VBOS = "vbos";
			private static final String KEY_CLASS = "class";
			private static final String KEY_VERTICES = "vertices";
			private static final String KEY_COORDS = "coords";
			private static final String KEY_NORMALS = "normals";
			private static final String KEY_INDICES = "indices";
		private static final String KEY_MODEL = "model";
		private static final String KEY_TEXTURE = "texture";
	private static final String KEY_FEATURES = "features";
		private static final String KEY_SPECULAR_DAMPER = "specular_damper";
		private static final String KEY_SPECULAR_REFLECTIVITY = "specular_reflectivity";
		private static final String KEY_AMBIENT_LIGHTNESS = "ambient_lightness";
		private static final String KEY_HAS_FAKE_LIGHT = "has_fake_lighting";
		private static final String KEY_HAS_TRANSPARENCY = "has_transparency";
		private static final String KEY_FOG_DENSITY = "fog_density";
		private static final String KEY_FOG_GRADIENT = "fog_gradient";
	
	//Default values
	private static final String VALUE_LOAD = "prefabs";
	
	private Map<String, TextureModel> mPrefabs = null;
	private Loader mLoader = null;
	
	public PrefabsManager(Loader loader) {
		this.mPrefabs = new HashMap<String, TextureModel>();
		this.mLoader = loader;
	}
	
	public void clear() {
		if (mPrefabs != null) {
			mPrefabs.clear();
		}
	}
	
	public TextureModel getPrefabByName(String name) {
		return mPrefabs.get(name);
	}
	
	public Set<String> getKeySet() {
		return mPrefabs.keySet();
	}

	/**
	 * Load prefabs from JSON config file.
	 * @param filePath
	 * @return
	 */
	public int loadPrefabs(String filePath) {
		if (mLoader == null) {
			Logger.e("PrefabsManager needs to be initialized Loader!");
			return -1;
		}
		long startTime = System.currentTimeMillis();

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = null;
		
		try {
			Logger.d("Loading", filePath);
			jsonObject = jsonParser.parse(new FileReader(filePath)).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			Logger.e("Could not load prefabs file! " + filePath);
			return -1;
		}
		
		//Verifying JSON file
		JsonElement eLoad = jsonObject.get(KEY_LOAD);
		JsonElement eData = jsonObject.get(KEY_DATA);
        if (eLoad == null || eData == null || eLoad.isJsonNull() || eData.isJsonNull() ||
        		!VALUE_LOAD.equalsIgnoreCase(eLoad.getAsString())) {
        	Logger.e("Verified error in prefabs file! " + filePath);
			return -1;
		}

        //Loading file path
        JsonElement ePathObj = jsonObject.get(KEY_PATH_OBJ);
        final String pathObj = (ePathObj == null || ePathObj.isJsonNull()) ? DataUtils.PATH_OBJ : ePathObj.getAsString();
        JsonElement ePathTex = jsonObject.get(KEY_PATH_TEX);
        final String pathTex = (ePathTex == null || ePathTex.isJsonNull()) ? DataUtils.PATH_TEXTURE : ePathTex.getAsString();
        
        //Loading data array
        JsonArray dataArray = eData.getAsJsonArray();
        if (dataArray == null || dataArray.isJsonNull()) {
        	Logger.e("Could not load prefabs file, missing data! " + filePath);
		}
        for (JsonElement element : dataArray) {
			JsonObject dataObject = element.getAsJsonObject();
			
			//Loading prefab name
			JsonElement eName = dataObject.get(KEY_NAME);
			if (eName == null || eName.isJsonNull()) {
				Logger.e("The prefab name must be clarified! " + filePath);
				return -1;
			}
			final String name = eName.getAsString();
			
			//Loading 3d-model in two ways: vbos data or .obj file
			RawModel rawModel = null;
			JsonElement eModel = dataObject.get(KEY_MODEL);
			if (eModel != null && !eModel.isJsonNull()) {
				final String model = eModel.getAsString();
				rawModel = OBJLoader.loadObjModel(pathObj + model, mLoader);
			} else {
				JsonElement eVbos = dataObject.get(KEY_VBOS);
				if (eVbos == null || eVbos.isJsonNull()) {
					Logger.e("Could not load prefabs file, missing 3d-model! " + filePath);
					return -1;
				}
				JsonObject vbos = eVbos.getAsJsonObject();
				if (vbos.size() < 4 || !vbos.keySet().contains(KEY_CLASS)) {
					Logger.e("Could not load prefabs file, incomplete vbos or missing '" + KEY_CLASS + "'! " + filePath);
					return -1;
				}
				final String classFile = vbos.get(KEY_CLASS).getAsString();
				final String vertices = vbos.get(KEY_VERTICES).getAsString();
				final String coords = vbos.get(KEY_COORDS).getAsString();
				final String indices = vbos.get(KEY_INDICES).getAsString();
				JsonElement eNormals = vbos.get(KEY_NORMALS);
				
				try {
					Class<?> clazz = Class.forName(classFile);
					final float[] arrayVertices = (float[]) clazz.getField(vertices).get(clazz);
					final float[] arrayCoords = (float[]) clazz.getField(coords).get(clazz);
					final int[] arrayIndices = (int[]) clazz.getField(indices).get(clazz);
					
					if (eNormals == null || eNormals.isJsonNull()) {
						rawModel = mLoader.loadToVAO(arrayVertices, arrayCoords, arrayIndices);
					} else {
						final String normals = eNormals.getAsString();
						final float[] arrayNormals = (float[]) clazz.getField(normals).get(clazz);
						rawModel = mLoader.loadToVAO(arrayVertices, arrayCoords, arrayNormals, arrayIndices);
					}
				} catch (Exception e) {
					Logger.e("Could not load prefabs file, caused by '" + classFile + "'! " + filePath);
					return -1;
				}
			}
			
			//Loading texture
			JsonElement eTexture = dataObject.get(KEY_TEXTURE);
			if (eTexture == null || eTexture.isJsonNull()) {
				Logger.e("Could not load prefabs file, missing texture! " + filePath);
				return -1;
			}
	        final String tex = eTexture.getAsString();
	        Texture texture = mLoader.loadTexture(pathTex + tex);
			
			//Loading features of texture
	        JsonElement eFeatures = dataObject.get(KEY_FEATURES);
	        if (eFeatures != null && !eFeatures.isJsonNull()) {
	        	JsonObject features = eFeatures.getAsJsonObject();
	        	
	        	JsonElement eSpecularDamper = features.get(KEY_SPECULAR_DAMPER);
	        	if (eSpecularDamper != null && !eSpecularDamper.isJsonNull()) {
	        		texture.setShineDamper(eSpecularDamper.getAsFloat());
	        	}
	        	JsonElement eSpecularReflectivity = features.get(KEY_SPECULAR_REFLECTIVITY);
	        	if (eSpecularReflectivity != null && !eSpecularReflectivity.isJsonNull()) {
	        		texture.setReflectivity(eSpecularReflectivity.getAsFloat());
	        	}
	        	JsonElement eAmbientLightness = features.get(KEY_AMBIENT_LIGHTNESS);
	        	if (eAmbientLightness != null && !eAmbientLightness.isJsonNull()) {
	        		texture.setAmbientLightness(eAmbientLightness.getAsFloat());
	        	}
	        	JsonElement eHasFakeLight = features.get(KEY_HAS_FAKE_LIGHT);
	        	if (eHasFakeLight != null && !eHasFakeLight.isJsonNull()) {
	        		texture.setUseFakeLighting(eHasFakeLight.getAsBoolean());
	        	}
	        	JsonElement eHasTrans = features.get(KEY_HAS_TRANSPARENCY);
	        	if (eHasTrans != null && !eHasTrans.isJsonNull()) {
	        		texture.setHasTransparency(eHasTrans.getAsBoolean());
	        	}
	        	JsonElement eFogDensity = features.get(KEY_FOG_DENSITY);
	        	JsonElement eFogGradient = features.get(KEY_FOG_GRADIENT);
	        	if (eFogDensity != null && !eFogDensity.isJsonNull()
	        			&& eFogGradient != null && !eFogGradient.isJsonNull()) {
	        		texture.addFoggy(eFogDensity.getAsFloat(), eFogGradient.getAsFloat());
	        	}
			}
		
			mPrefabs.put(name, new TextureModel(rawModel, texture));
			//End of once loop in data array
        }
        long endTime = System.currentTimeMillis();
        Logger.d("Duration", "[" + (endTime - startTime) + "ms]" + " Prefabs loading done.");
        
        return (dataArray == null ? -1 : dataArray.size());
	}
	
/**
 * 【附】prefabs.json模板，含全部键值对
 * 
 * {
		"load": "prefabs",			--> 必须与模板一致
		"path_obj": "res/obj/",		--> 可选
		"path_tex": "res/texture/",	--> 可选
		"data": 
		[
			{
				//第一个预制体
				"name": "blcok",		--> 必须
				"model": "cube.obj",	--> 必须（与下者互斥）
				"vbos": {				--> 必须（互上者互斥）
					"class"	: "com.lyp.uge.utils.DataUtils",
					"vertices"	: "CUBE_VERTICES",
					"coords"	: "CUBE_TEXTURE_COORDS",
					"normals"	: null,
					"indices"	: "CUBE_INDICES"
				},
				"texture": "cube.png",	--> 必须
				"features": {			--> 可选
					"specular_damper"		: 1.0,		--> float
					"specular_reflectivity"	: 0.0,		--> float
					"ambient_lightness"		: 0.25,		--> float
					"has_fake_lighting"		: null,		--> boolean
					"has_transparency"		: false,	--> boolean
					"fog_density"			: 0.003,	--> float（与下者共用）
					"fog_gradient"			: 1.5		--> float（与上者共用）
				}
			},
			{
				//第二个预制体
			}
			... //第N个预制体
		]
 * 	}
 * 
 * 【说明】
 * 1. 设为null的值表示使用引擎默认值，并非真的为空。
 * 2. 使用不到的键值对可删除不写，效果与设为null等价。
 * 3. model与vbos二者互斥，只能选用一个作为三维模型。
 * 4. fog_density与fog_gradient二者必须同时设置才会生效。
 */
}
