package com.example.acrenderer;

public class Transform {
    static void rotate(float vertices, float degrees, float[] axis) {}

    static void translate(float[] vertices, float dx, float dy, float dz) {
        float[] m = RMath.mat4();
        m[3] = dx;
        m[7] = dy;
        m[11] = dz;

        float[] v = new float[4];
        v[3] = 1;

        float[] res;

        for (int i = 0; i < vertices.length; i+=3) {
            v[0] = vertices[i];
            v[1] = vertices[i+1];
            v[2] = vertices[i+2];

            res = RMath.mvMul(m, v);
            vertices[i] = res[0];
            vertices[i+1] = res[1];
            vertices[i+2] = res[2];
        }
    }

    static void scale(float vertices, float dx, float dy) {}
}
