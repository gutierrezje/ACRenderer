package com.example.acrenderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class RenderDisplay extends View {
    private final String TAG = "RenderDisplay";

    private Paint currentPaint;
    private Path path;

    private int width;
    private int height;

    private int[] minBoundingBox;
    private int[] maxBoundingBox;
    private int[] clamp;
    private int[] P;

    private int[] screenCoords;
    private float[] worldCoords;
    private float[] lightDir;

    private float[] vPositions;
    private float[] vTextures;
    private float[] vNormals;

    public RenderDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        ModelLoader model = new ModelLoader(this.getContext(), "african_head.obj");
        vPositions = model.vPositions;

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        minBoundingBox = new int[2];
        maxBoundingBox = new int[2];
        clamp = new int[2];
        P = new int[2];

        screenCoords = new int[6];
        worldCoords = new float[9];
        lightDir = new float[]{0,0,-1};

        currentPaint = new Paint();
        currentPaint.setStrokeWidth(1);

        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw objects on the screen
        super.onDraw(canvas);
        width = this.getWidth();
        height = this.getHeight();
        clamp[0] = width;
        clamp[1] = height;
        canvas.scale(1, -1, width / 2, height / 2);
        drawModel(canvas);

    }

    void drawModel(Canvas canvas) {
        for (int i = 0; i < vPositions.length; i+=9) {
            for (int j = 0; j < 3; j++) {
                int idx = 3*j+i;
                screenCoords[2*j]   = (int)((vPositions[idx] + 1) * width / 2);
                screenCoords[2*j+1] = (int)((vPositions[idx+1] + 1) * height / 2);

                worldCoords[3*j]   = vPositions[idx];
                worldCoords[3*j+1] = vPositions[idx+1];
                worldCoords[3*j+2] = vPositions[idx+2];
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

    void triangle(Canvas canvas, int[] points, int paintColor) {
        currentPaint.setColor(paintColor);

        minBoundingBox[0] = this.getWidth();
        minBoundingBox[1] = this.getHeight();
        maxBoundingBox[0] = 0;
        maxBoundingBox[1] = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                minBoundingBox[j] = Math.max(0,        Math.min(minBoundingBox[j], points[2*i + j]));
                maxBoundingBox[j] = Math.min(clamp[j], Math.max(maxBoundingBox[j], points[2*i + j]));
            }
        }

        for (P[0] = minBoundingBox[0]; P[0] <= maxBoundingBox[0]; P[0]++) {
            for (P[1] = minBoundingBox[1]; P[1] <= maxBoundingBox[1]; P[1]++) {
                float[] bc_screen = barycentric(points, P);
                if (bc_screen[0] < 0 || bc_screen[1] < 0 || bc_screen[2] < 0) continue;
                canvas.drawPoint(P[0], P[1], currentPaint);
            }
        }
    }

    float[] barycentric(int[] points, int[] P) {
        float[] u = RMath.cross(
                new float[]{points[4] - points[0], points[2] - points[0], points[0] - P[0]},
                new float[]{points[5] - points[1], points[3] - points[1], points[1] - P[1]});
        if (Math.abs(u[2]) < 1) {
            u[0] = -1;
            u[1] = 1;
            u[2] = 1;
        }
        else {
            float x = u[0], y = u[1], z = u[2];
            u[0] = 1 - (x + y) / z;
            u[1] = y / z;
            u[2] = x / z;
        }
        return u;
    }

    @Override
    protected  void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
