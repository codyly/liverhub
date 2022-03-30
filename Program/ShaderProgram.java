package com.example.cody.liverhub.Program;

import android.content.Context;

import com.example.cody.liverhub.util.ShaderHelper;
import com.example.cody.liverhub.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {

    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_KD = "u_Kd";
    protected static final String U_LD = "u_Ld";
    protected static final String U_PROJECTION_MATRIX = "u_ProjectionMatrix";
    protected static final String U_NORMAL_MATRIX = "u_NormalMatrix";
    protected static final String U_MODEL_MATRIX = "u_ModelMatrix";
    protected static final String U_LIGHT_POSITION = "u_LightPosition";
    // Attribute constants
    protected static final String A_NORMAL = "a_Normal";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    // Shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId) {
// Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(
                        context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(
                        context, fragmentShaderResourceId));
    }
    public void useProgram() {
// Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }
}
