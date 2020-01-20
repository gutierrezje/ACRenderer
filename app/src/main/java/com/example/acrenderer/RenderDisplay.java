package com.example.acrenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class RenderDisplay extends View {
    private final String TAG = "RenderDisplay";

    private Random rand;
    private Paint currentPaint;
    private Path path;

    private int width;
    private int height;

    private int[] screen_coords;
    private float[] world_coords;
    private float[] light_dir;

    private float[] vPositions;
    private float[] vTextures;
    private float[] vNormals;

    public RenderDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        ModelLoader model = new ModelLoader(this.getContext(), "african_head.obj");
        vPositions = model.vPositions;

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        rand = new Random();

        screen_coords = new int[6];
        world_coords = new float[9];
        light_dir = new float[]{0,0,-1};

        currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentPaint.setStrokeWidth(1);
        currentPaint.setStyle(Paint.Style.FILL);

        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw objects on the screen
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight() / 2;
        drawModel(canvas);
    }

    void drawModel(Canvas canvas) {
        for (int i = 0; i < vPositions.length; i+=9) {
            for (int j = 0; j < 3; j++) {
                int idx = 3*j+i;
                screen_coords[2*j]   = (int)((vPositions[idx] + 1) * width / 2);
                screen_coords[2*j+1] = (int)((vPositions[idx+1] + 1) * height / 2);

                world_coords[3*j]   = vPositions[idx];
                world_coords[3*j+1] = vPositions[idx+1];
                world_coords[3*j+2] = vPositions[idx+2];
            }

            float[] n = RMath.normalize(
                    RMath.cross(
                    RMath.sub(
                            Arrays.copyOfRange(world_coords, 6, 9),
                            Arrays.copyOfRange(world_coords, 0, 3)),
                    RMath.sub(
                            Arrays.copyOfRange(world_coords, 3, 6),
                            Arrays.copyOfRange(world_coords, 0, 3)
                    )));

            float intensity = RMath.dot(n, light_dir);

            if (intensity > 0) {
                triangle(canvas, screen_coords,
                        Color.rgb((int) (intensity * 255),
                                (int) (intensity * 255),
                                (int) (intensity * 255)));
            }
        }
    }

    void triangle(Canvas canvas, int[] points, int paintColor) {
        currentPaint.setColor(paintColor);

        path.moveTo(points[0], height - points[1]);
        for(int i = 2; i < points.length; i+=2) {
            path.lineTo(points[i], height - points[i+1] );
        }
        path.close();
        canvas.drawPath(path, currentPaint);
        path.reset();
    }

    @Override
    protected  void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
