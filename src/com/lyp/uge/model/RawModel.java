package com.lyp.uge.model;

public class RawModel {

	private int vaoID;
	private int vertexCount;
	private ModelData modelData;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public ModelData getModelData() {
		return modelData;
	}
	
	public void setModelData(ModelData modelData) {
		this.modelData = modelData;
	}
}
