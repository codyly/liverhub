package com.example.cody.liverhub.Objects;


import android.util.FloatMath;
import android.util.Log;

import com.example.cody.liverhub.util.Geometry;
import com.example.cody.liverhub.util.Geometry.Circle;
import com.example.cody.liverhub.util.Geometry.Point;
import com.example.cody.liverhub.util.Geometry.Cylinder;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static com.example.cody.liverhub.Constant.VERT_PER_FACET;
import static com.example.cody.liverhub.Constant.VERT_PER_FLUSH;
import static java.lang.Math.cos;

public class ObjectBuilder {
    static interface DrawCommand {
        void draw();
    }
    static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawList;
        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }
    private static final int FLOATS_PER_VERTEX = 3;
    private static final int INDICE_PER_FACET =3 ;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    private GeneratedData build() {

        return new GeneratedData(vertexData, drawList);
    }

    private void appendLiverData(LiverModel liverModel){
        final int startVertex = 0;
        int verticeNumber = liverModel.getFacets().size();

        final int numVertices =  verticeNumber* VERT_PER_FACET;
        int i;
        for (i = 0; i < verticeNumber; i++){
            int index = (Integer) liverModel.getFacets().get(i);
            int coor_pointer;
            for(coor_pointer = 0; coor_pointer < 3; coor_pointer += 1){
                int real_index = index * FLOATS_PER_VERTEX + coor_pointer;
                vertexData[offset++] = liverModel.getVertices().get(real_index);
            }
            vertexData[offset++] = 1.0f;
            for(coor_pointer = 0; coor_pointer < 2; coor_pointer += 1){
                vertexData[offset++] = 0.5f;
            }
            for(coor_pointer = 0; coor_pointer < 3; coor_pointer += 1){
                int real_index = index * FLOATS_PER_VERTEX + coor_pointer;
                vertexData[offset++] = liverModel.getNormals().get(real_index);
            }
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    private void appendLiverData(LiverModel liverModel, int startIndex, int stride){
        final int startVertex = 0;
        offset = 0;
        int verticeNumber = Math.min(startIndex + VERT_PER_FLUSH, liverModel.getFacets().size());
//        Log.d("Object builder", "" + liverModel.getFacets().size());

        final int numVertices =  (verticeNumber-startIndex);
        int i;
        for (i = startIndex; i < verticeNumber; i++){
            int index = (Integer) liverModel.getFacets().get(i);
            int coor_pointer;
            for(coor_pointer = 0; coor_pointer < 3; coor_pointer += 1){
                int real_index = index * FLOATS_PER_VERTEX + coor_pointer;
                vertexData[offset++] = liverModel.getVertices().get(real_index);
            }
            vertexData[offset++] = 1.0f;
            for(coor_pointer = 0; coor_pointer < 2; coor_pointer += 1){
                int real_index = index * FLOATS_PER_VERTEX + coor_pointer;
                vertexData[offset++] = liverModel.getVertices().get(real_index);
            }
            for(coor_pointer = 0; coor_pointer < 3; coor_pointer += 1){
                int real_index = index * FLOATS_PER_VERTEX + coor_pointer;
                vertexData[offset++] = liverModel.getNormals().get(real_index);
//                vertexData[offset++] = liverModel.getVertices().get(real_index) - liverModel.getRef_point().get(coor_pointer);
            }
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLES, startVertex, numVertices );
            }
        });
    }

    static GeneratedData createLiverModel(LiverModel liverModel){

        ObjectBuilder builder = new ObjectBuilder(liverModel.getFacets().size() * FLOATS_PER_VERTEX);
        builder.appendLiverData(liverModel);
        return builder.build();
    }

    static GeneratedData createLiverModel(LiverModel liverModel, int startIndex, int stride){
        int verticeNumber = Math.min(startIndex + VERT_PER_FLUSH, liverModel.getFacets().size());
        int numVertices =  (verticeNumber-startIndex);
        ObjectBuilder builder = new ObjectBuilder(numVertices*stride + 1);
        builder.appendLiverData(liverModel, startIndex, stride);
        return builder.build();
    }

}
