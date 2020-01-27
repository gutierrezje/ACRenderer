package com.example.acrenderer;

public class Camera {

    static float[] lookAt(float[] eyePosition, float[] viewUp) {
        float[] cameraDir = RMath.normalize(eyePosition);
        float[] cameraRight = RMath.normalize(RMath.cross(viewUp, cameraDir));
        float[] cameraUp = RMath.normalize(RMath.cross(cameraDir, cameraRight));

        float[] cameraSpace = RMath.mat4();
        float[] cameraPos = RMath.mat4();

        cameraSpace[0] = cameraRight[0];
        cameraSpace[1] = cameraRight[1];
        cameraSpace[2] = cameraRight[2];
        cameraSpace[4] = cameraUp[0];
        cameraSpace[5] = cameraUp[1];
        cameraSpace[6] = cameraUp[2];
        cameraSpace[8] = cameraDir[0];
        cameraSpace[9] = cameraDir[1];
        cameraSpace[10] = cameraDir[2];

        cameraPos[3] = -eyePosition[0];
        cameraPos[7] = -eyePosition[1];
        cameraPos[11] = -eyePosition[2];

        return RMath.mmMul(cameraSpace, cameraPos);
    }

    static float[] viewport(int w, int h) {
        float[] vp = RMath.mat4();

        vp[0] = w/2;
        vp[3] = (w - 1)/2;
        vp[5] = h/2;
        vp[7] = (h - 1)/2;

        return vp;
    }

    static float[] perspective(int l, int r, int b, int t, int n, int f) {
        float[] per = RMath.mat4();

        per[0] = 2f*n/(r-l);
        per[2] = (float)(l+r)/(l-r);
        per[5] = 2f*f*n/(t-b);
        per[6] = (float)(b+t)/(b-t);
        per[10] = (float)(f+n)/(n-f);
        per[11] = 2f*f*n/(f-n);
        per[14] = 1;
        per[15] = 0;

        return per;
    }

    static float[] orthographic(int l, int r, int b, int t, int n, int f) {
        float[] orth = RMath.mat4();

        orth[0] = 2f/(r -l);
        orth[3] = (float)-(r+l)/(r-l);
        orth[5] = 2f/(t-b);
        orth[7] = (float)-(t+b)/(t-b);
        orth[10] = 2f/(n-f);
        orth[11] = (float)-(n+f)/(n-f);

        return orth;
    }
}
