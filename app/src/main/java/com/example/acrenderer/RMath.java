package com.example.acrenderer;

public class RMath {

    static float[] vec3() {
        return new float[3];
    }

    static float[] vec4() {
        return new float[]{0,0,0,1};
    }

    static float[] sub(float[] v1, float[] v2) {
        float[] result = new float[v1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    static float[] svMul(float f, float[] v) {
        float[] result = new float[v.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = f * v[i];
        }
        return result;
    }

//    static float[] vvMul(float[] v1, float[] v2) {
//        float[] result = new float[v1.length];
//        for (int i = 0; i < result.length; i++) {
//            result[i] = v1[i] * v2[i];
//        }
//        return result;
//    }

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
        return svMul(1/norm(v), v);
    }

    static float[] mat4() {
        return new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1};
    }

    static float[] mvMul(float[] m, float[] v) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i] += m[4*i+j]*v[j];
            }
        }

        return result;
    }

    static float[] mmMul(float[] m1, float[] m2) {
        float[] result = new float[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[4*i+j] += m1[4*i+k]*m2[4*k+j];
                }
            }
        }
        return result;
    }
}
