package com.example.acrenderer;

public class RMath {
    static float[] sub(float[] v1, float[] v2) {
        float[] result = new float[v1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    static float[] mul(float f, float[] v) {
        float[] result = new float[v.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = f * v[i];
        }
        return result;
    }

    static float[] mul(float[] v1, float[] v2) {
        float[] result = new float[v1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = v1[i] * v2[i];
        }
        return result;
    }

    static float dot(float[] v1, float[] v2) {
        float result = 0;
        for (int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }
        return result;
    }

    static float[] cross(float[] v1, float[] v2) {
        return new float[]{
                v1[1] * v2[2] - v1[2] * v2[1],
                v1[2] * v2[0] - v1[0] * v2[2],
                v1[0] * v2[1] - v1[1] * v2[0]
        };
    }

    static float norm(float[] v) {
        float result = 0;
        for (float f : v) {
            result += f*f;
        }

        return (float)Math.sqrt(result);
    }

    static float[] normalize(float[] v) {
        return mul(1/norm(v), v);
    }
}
