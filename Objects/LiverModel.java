package com.example.cody.liverhub.Objects;

import com.example.cody.liverhub.Data.VertexArray;
import com.example.cody.liverhub.Program.ColorShaderProgram;
import com.example.cody.liverhub.Program.TextureShaderProgram;
import com.example.cody.liverhub.util.Geometry;

import java.util.List;

import static com.example.cody.liverhub.Constant.BYTES_PER_FLOAT;
import static com.example.cody.liverhub.Constant.VERT_PER_FLUSH;

public class LiverModel {
    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            +TEXTURE_COORDINATES_COMPONENT_COUNT+NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final int STRIDE_NUM = (POSITION_COMPONENT_COUNT
            +TEXTURE_COORDINATES_COMPONENT_COUNT+NORMAL_COMPONENT_COUNT);
    private int model_id;
    private String name;
    private List<Float> vertices;
    private List<Float> ref_point;
    private List<Float> normals;
    private List<Integer> facets;

    public List<Float> getRef_point() {
        return ref_point;
    }

    public void setRef_point(List<Float> ref_point) {
        this.ref_point = ref_point;
    }

    private List<Integer> size;
    private float facet_number;

    public float getFacet_number() {
        return facet_number;
    }

    public void setFacet_number(float facet_number) {
        this.facet_number = facet_number;
    }

    private float scale;
    private VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public LiverModel(){
        vertexArray = null;
        drawList = null;
    }

    public LiverModel(LiverModel liverModel){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createLiverModel(liverModel, 0, STRIDE_NUM);
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public LiverModel(LiverModel liverModel, int startIndex){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createLiverModel(liverModel, startIndex, STRIDE_NUM);
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void resetDataset(LiverModel liverModel, int startIndex){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createLiverModel(liverModel, startIndex, STRIDE_NUM);
        vertexArray = new VertexArray(generatedData.vertexData);
    }

    public List<Float> getVertices() {
        return vertices;
    }

    public void setVertices(List<Float> vertices) {
        this.vertices = vertices;
    }

    public List<Integer> getFacets() {
        return facets;
    }

    public void setFacets(List<Integer> facets) {
        this.facets = facets;
    }

    public String getName() {
        return name;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int id){
        this.model_id=id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT,
                textureProgram.getNormalAttributeLocation(),
                NORMAL_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }

    public void setNormals(List<Float> normals) {
        this.normals = normals;
    }

    public List<Float> getNormals() {
        return normals;
    }
}
