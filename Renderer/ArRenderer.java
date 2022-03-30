package com.example.cody.liverhub.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.cody.liverhub.Constant;
import com.example.cody.liverhub.Objects.LiverModel;
import com.example.cody.liverhub.Program.ColorShaderProgram;
import com.example.cody.liverhub.Program.TextureShaderProgram;
import com.example.cody.liverhub.R;
import com.example.cody.liverhub.util.Geometry;
import com.example.cody.liverhub.util.MatrixHelper;
import com.example.cody.liverhub.util.TextureHelper;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import static android.content.ContentValues.TAG;
import static android.opengl.GLES10.glClearDepthf;
import static android.opengl.GLES10.glDepthFunc;
import static android.opengl.GLES10.glDepthMask;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.transposeM;
import static com.example.cody.liverhub.Constant.VERT_PER_FLUSH;
import static javax.microedition.khronos.opengles.GL10.GL_CULL_FACE;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;
import static javax.microedition.khronos.opengles.GL10.GL_LEQUAL;
import static javax.microedition.khronos.opengles.GL10.GL_LESS;

public class ArRenderer implements GLSurfaceView.Renderer{
    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];
    private final float[] normalMatrix = new float[16];
    private float[] lightMatrix = new float[4];
    private float[] kdMatrix = new float[3];
    private float[] ldMatrix = new float[3];

    private List<LiverModel> raw_liverList = null;
    private LiverModel liverModel;
    private LiverModel tumorModel;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;
    private int loopTime;
    private Geometry.Point modelPosition;
    private boolean modelPressed = false;
    private int startIndex;
    private float plateAngle;
    private float scale;
    private boolean hasTumorModel = false;
    private boolean hasLiverModel = false;
    private boolean displayTumorModel = true;
    private boolean displayLiverModel = true;

    public ArRenderer(Context context){
        this.context = context;
    }

    public ArRenderer(Context context, List<LiverModel> liverList){
        this.context = context;
        this.raw_liverList = liverList;
        startIndex = 0;
        plateAngle = 0;
        scale = 1.0f;
    }
    public void setdisplayTumuerModel(){
        displayTumorModel = ! displayTumorModel;
    }
    public void setdisplayLiverModel(){
        displayLiverModel = ! displayLiverModel;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f,0f, 0f,0.0f);
        glEnable(GL_CULL_FACE|GL_DEPTH_TEST);
//        if(raw_liverList == null){
//            return;
//        }
        liverModel = null;
        tumorModel = null;
        textureProgram = new TextureShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.texture);
        colorProgram = new ColorShaderProgram(context);
        modelPosition = new Geometry.Point(0.0f,0.0f,0.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        glViewport(0,0,width,height);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float)height, 0.5f, 5f);
        setLookAtM(viewMatrix, 0, 0f, 0.0f, 3.0f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
//        glEnable(GL_DEPTH_TEST);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);
        if(textureProgram != null && raw_liverList.size() != 0){
            if(hasLiverModel && displayLiverModel){
                positionLiverInScene(-raw_liverList.get(0).getRef_point().get(0),
                        -raw_liverList.get(0).getRef_point().get(1),
                        -raw_liverList.get(0).getRef_point().get(2),1.0f);
                lightMatrix[0] = 0.0f; lightMatrix[1] = -2f; lightMatrix[2] = 0f; lightMatrix[3] = 0.0f;
                kdMatrix[0] = 0.5f; kdMatrix[1] = 0.5f; kdMatrix[2] = 0.5f;
                ldMatrix[0] = 2.0f; ldMatrix[1] = 2.0f; ldMatrix[2] = 2.0f;
                textureProgram.useProgram();
                textureProgram.setUniforms(modelViewProjectionMatrix, projectionMatrix, normalMatrix,
                        modelViewMatrix, lightMatrix, kdMatrix, ldMatrix, texture);
                loopTime = (int) liverModel.getFacet_number() / VERT_PER_FLUSH + 1;
                for(int i=0; i<loopTime;i++){
                    liverModel.resetDataset(raw_liverList.get(0), startIndex);
                    liverModel.bindData(textureProgram);
                    liverModel.draw();
                    startIndex = startIndex + VERT_PER_FLUSH;
                    if(startIndex >= raw_liverList.get(0).getFacet_number()){
                        startIndex = 0;
                        break;
                    }
                }
            }
            if(hasTumorModel && displayTumorModel){
                positionLiverInScene(0.0f,0.0f,0.0f, 0.4f);
                lightMatrix[0] = 0.0f; lightMatrix[1] = -2f; lightMatrix[2] = 0f; lightMatrix[3] = 0.0f;
                kdMatrix[0] = 0.5f; kdMatrix[1] = 0.5f; kdMatrix[2] = 0.5f;
                ldMatrix[0] = 2.0f; ldMatrix[1] = 2.0f; ldMatrix[2] = 2.0f;
                textureProgram.useProgram();
                textureProgram.setUniforms(modelViewProjectionMatrix, projectionMatrix, normalMatrix,
                        modelViewMatrix, lightMatrix, kdMatrix, ldMatrix, texture);
                loopTime = (int) tumorModel.getFacet_number() / VERT_PER_FLUSH + 1;
                for(int i=0; i<loopTime;i++){
                    tumorModel.resetDataset(raw_liverList.get(1), startIndex);
                    tumorModel.bindData(textureProgram);
                    tumorModel.draw();
                    startIndex = startIndex + VERT_PER_FLUSH;
                    if(startIndex >= raw_liverList.get(1).getFacet_number()){
                        startIndex = 0;
                        break;
                    }
                }
            }
        }
        if(!(hasTumorModel && hasLiverModel) && Constant.dynamic_model.size() != 0){
            raw_liverList = Constant.dynamic_model;
            hasLiverModel = true;
            liverModel = new LiverModel(raw_liverList.get(0), startIndex);
            if(raw_liverList.size()>1){
                if(raw_liverList.get(0).getFacet_number()<raw_liverList.get(1).getFacet_number()){
                    LiverModel tmp = raw_liverList.get(0);
                    raw_liverList.set(0, raw_liverList.get(1));
                    raw_liverList.set(1, tmp);
                }
                liverModel = new LiverModel(raw_liverList.get(0), startIndex);
                tumorModel = new LiverModel(raw_liverList.get(1), startIndex);
                hasTumorModel=true;
            }
        }

    }

    private void positionLiverInScene(float x, float y, float z, float scaler){
        setIdentityM(modelMatrix, 0);
        setIdentityM(normalMatrix, 0);
        float[] tmpMatrix = new float[16];
        float[] tmpMatrix_1 = new float[16];
        float[] tmpMatrix_2 = new float[16];
        float[] tmpMatrix_3 = new float[16];
        setIdentityM(tmpMatrix, 0);
        setIdentityM(tmpMatrix_1, 0);
        translateM(tmpMatrix_1, 0, tmpMatrix, 0, x,y,z);
        setIdentityM(tmpMatrix, 0);
        scaleM(tmpMatrix, 0, scale*scaler,scale*scaler,scale*scaler);
        multiplyMM(tmpMatrix_2,0,tmpMatrix,0,tmpMatrix_1,0);
        setIdentityM(tmpMatrix, 0);
        setRotateM(tmpMatrix, 0, plateAngle, 0, 1,0);
        setIdentityM(tmpMatrix_1, 0);
        multiplyMM(tmpMatrix_3,0,tmpMatrix,0,tmpMatrix_1,0);

        multiplyMM(modelMatrix, 0,tmpMatrix_3, 0,tmpMatrix_2,0);

        multiplyMM(modelViewMatrix, 0, viewMatrix,
                0, modelMatrix, 0);

        invertM(tmpMatrix,0,modelViewMatrix,0);
        transposeM(normalMatrix,0, tmpMatrix,0);

//        setRotateM(tmpMatrix, 0, plateAngle, 0, 1,0);
//        setIdentityM(tmpMatrix_1, 0);
//        multiplyMM(normalMatrix, 0, tmpMatrix, 0,tmpMatrix_1,0);

        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
//        multiplyMM(normalMatrix, 0, viewProjectionMatrix,
////                0, normalMatrix, 0);
    }

    private void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    private Geometry.Ray convertNormalized2DPointToRay(
            float normalizedX, float normalizedY) {
        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc = {normalizedX, normalizedY, 1, 1};
        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];
        multiplyMV(
                nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(
                farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);
        divideByW(nearPointWorld);
        divideByW(farPointWorld);
        Geometry.Point nearPointRay =
                new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPointRay =
                new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
        return new Geometry.Ray(nearPointRay,
                Geometry.vectorBetween(nearPointRay, farPointRay));
    }

    private float clamp(float value, float min, float max) {
        return Math.min(max, Math.max(value, min));
    }
    public void handleTouchPress(float normalizedX, float normalizedY) {
        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
        // Now test if this ray intersects with the mallet by creating a
        // bounding sphere that wraps the mallet.
        Geometry.Sphere malletBoundingSphere = new Geometry.Sphere(new Geometry.Point(
                modelPosition.x,
                modelPosition.y,
                modelPosition.z),
                liverModel.getScale()*1.0f/2.0f);
        // If the ray intersects (if the user touched a part of the screen that
        // intersects the mallet's bounding sphere), then set malletPressed =
        // true.
        modelPressed = Geometry.intersects(malletBoundingSphere, ray);
        modelPressed = true;
    }
    public void handleTouchDrag(float normalizedX, float normalizedY) {
        if (modelPressed) {
            plateAngle -= (normalizedX - 0.5) * 60;
            scale = 1.0f * (1.0f + (normalizedY-0.5f) * 1.0f);
        }
    }

}
