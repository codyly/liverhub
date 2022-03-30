package com.example.cody.liverhub.Program;

import android.content.Context;

import com.example.cody.liverhub.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class TextureShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int uModelMatrixLocation;
    private final int uNormalMatrixLocation;
    private final int uProjectionMatrixLocation;
    private final int uLightPositionLocation;
    private final int uLdLocation;
    private final int uKdLocation;

    private final int aNormalLocation;
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;
    public TextureShaderProgram(Context context) {
        super(context, R.raw.simle_vertex_shader,
                R.raw.simple_fragment_shader);
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uKdLocation = glGetUniformLocation(program, U_KD);
        uLdLocation = glGetUniformLocation(program, U_LD);
        uLightPositionLocation = glGetUniformLocation(program, U_LIGHT_POSITION);
        uProjectionMatrixLocation = glGetUniformLocation(program, U_PROJECTION_MATRIX);
        uModelMatrixLocation = glGetUniformLocation(program, U_MODEL_MATRIX);
        uNormalMatrixLocation = glGetUniformLocation(program, U_NORMAL_MATRIX);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation =
                glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
    }
    public void setUniforms(float[] mvp_matrix, float[] proj_matrix, float[] norm_matrix,
                            float[] model_matrix, float[] light_matrix,
                            float[] kd, float[] ld, int textureId) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvp_matrix, 0);
        glUniformMatrix4fv(uProjectionMatrixLocation, 1, false, proj_matrix, 0);
        glUniformMatrix4fv(uNormalMatrixLocation, 1, false, norm_matrix, 0);
        glUniformMatrix4fv(uModelMatrixLocation, 1, false, model_matrix, 0);
        glUniform4fv(uLightPositionLocation, 1, light_matrix, 0);
        glUniform3fv(uKdLocation, 1, kd, 0);
        glUniform3fv(uLdLocation, 1, ld, 0);

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
    public int getNormalAttributeLocation(){
        return aNormalLocation;
    }
}
