package com.example.acrenderer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class RenderDisplay extends View {
    private final String TAG = "RenderDisplay";

    private Paint currentPaint;

    private int size;

    private int[] minBoundBox;
    private int[] maxBoundBox;
    private int[] clamp;
    private float[] P;

    private float[] screenCoords;
    private float[] worldCoords;
    private float[] zBuffer;

    private float[] worldUp;
    private float[] cameraPos;
    private float[] lightDir;
    private float[] viewMat;
    private float[] projectionMat;
    private float[] cameraMat;

    private float[] vPositions;
    private float[] vTextures;
    private float[] vNormals;

    private float[] opV1;
    private float[] opV2;
    private float[] opM1;
    private float[] opM2;

    private final String headObj = "african_head.obj";
    private final String cubeObj = "cube.obj";

    public RenderDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        ModelLoader model = new ModelLoader(this.getContext(), headObj);
        vPositions = model.vPositions;
        //vTextures = model.vTextures;

        Resources res = getResources();
        size = (int)res.getDimension(R.dimen.render_display_size);

        minBoundBox = new int[2];
        maxBoundBox = new int[2];
        clamp = new int[]{size - 1, size - 1};
        P = new float[3];

        screenCoords = new float[9];
        worldCoords = new float[9];
        zBuffer = new float[size * size];
        Arrays.fill(zBuffer, -Float.MAX_VALUE);

        worldUp = new float[]{0,1,0};
        cameraPos = new float[]{0.5f,0,1.6f};
        lightDir = new float[]{0, 0, -1};

        viewMat = Camera.viewport(size, size);
        projectionMat = Camera.perspective(-1, 1, -1,1,-1,1);
        cameraMat = Camera.lookAt(cameraPos, worldUp);

        opV1 = RMath.vec4();

        currentPaint = new Paint();
        currentPaint.setStrokeWidth(1);

        //Transform.translate(vPositions, -0.5f, -0.5f, 0f);

        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw objects on the screen
        super.onDraw(canvas);
        canvas.scale(1, -1, size / 2, size / 2);
        drawModel(canvas);
    }

    void drawModel(Canvas canvas) {
        for (int i = 0; i < vPositions.length; i+=9) {
            for (int j = 0; j < 3; j++) {
                int idx = 3*j+i;
                opV1[0] = vPositions[idx];
                opV1[1] = vPositions[idx+1];
                opV1[2] = vPositions[idx+2];

                opV2 = RMath.mvMul(
                        RMath.mmMul(RMath.mmMul(viewMat, projectionMat), cameraMat),
                        opV1);

                screenCoords[3*j]   = opV2[0]/opV2[3];
                screenCoords[3*j+1] = opV2[1]/opV2[3];
                screenCoords[3*j+2] = opV2[2]/opV2[3];

                worldCoords[3*j]   = vPositions[idx];
                worldCoords[3*j+1] = vPositions[idx+1];
                worldCoords[3*j+2] = vPositions[idx+2];

//                screenCoords[j] = (j % 3 == 2) ? vPositions[i+j]
//                        : (int)((vPositions[i+j] + 1) * size / 2 + 0.5);
//
//                worldCoords[j] = vPositions[i+j];
            }

            float[] n = RMath.normalize(
                    RMath.cross(
                    RMath.sub(
                            Arrays.copyOfRange(worldCoords, 6, 9),
                            Arrays.copyOfRange(worldCoords, 0, 3)),
                    RMath.sub(
                            Arrays.copyOfRange(worldCoords, 3, 6),
                            Arrays.copyOfRange(worldCoords, 0, 3)
                    )));

            float intensity = RMath.dot(n, lightDir);

            if (intensity > 0) {
                triangle(canvas, screenCoords,
                        Color.rgb((int) (intensity * 255),
                                (int) (intensity * 255),
                                (int) (intensity * 255)));
            }
        }
    }

    void triangle(Canvas canvas, float[] points, int paintColor) {
        minBoundBox[0] = size - 1;
        minBoundBox[1] = size - 1;
        maxBoundBox[0] = 0;
        maxBoundBox[1] = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                minBoundBox[j] = Math.max(0,        Math.min(minBoundBox[j], (int)points[3*i + j]));
                maxBoundBox[j] = Math.min(clamp[j], Math.max(maxBoundBox[j], (int)points[3*i + j]));
            }
        }

        for (P[0] = minBoundBox[0]; P[0] <= maxBoundBox[0]; P[0]++) {
            for (P[1] = minBoundBox[1]; P[1] <= maxBoundBox[1]; P[1]++) {
                float[] bc_screen = barycentric(points, P);
                if (bc_screen[0] < 0 || bc_screen[1] < 0 || bc_screen[2] < 0) continue;
                P[2] = 0;
                for (int i = 0; i < 3; i++)
                    P[2] += points[3*i + 2] * bc_screen[i];
                if (zBuffer[(int)(P[0] + P[1] * size)] < P[2]) {
                    zBuffer[(int)(P[0] + P[1] * size)] = P[2];
                    currentPaint.setColor(paintColor);
                    canvas.drawPoint(P[0], P[1], currentPaint);
                }
            }
        }
    }

    float[] barycentric(float[] points, float[] P) {
        float[] u = RMath.cross(
                new float[]{points[6] - points[0], points[3] - points[0], points[0] - P[0]},
                new float[]{points[7] - points[1], points[4] - points[1], points[1] - P[1]});
        if (Math.abs(u[2]) > 1e-2) {
            float x = u[0], y = u[1], z = u[2];
            u[0] = 1 - (x + y) / z;
            u[1] = y / z;
            u[2] = x / z;
        }
        else {
            u[0] = -1;
            u[1] = 1;
            u[2] = 1;
        }
        return u;
    }

    @Override
    protected  void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
