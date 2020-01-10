package com.example.acrenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Vector;

public class RenderDisplay extends View {
    private final String TAG = "RenderDisplay";
    private Paint currentPaint;
    ModelLoader model;
    private int width;
    private int height;

    public RenderDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = new ModelLoader(this.getContext(), "african_head.obj");

        // set appropriate width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        width = (int)(displayMetrics.widthPixels * 0.9);
        height = width;

        currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentPaint.setColor(Color.RED);
        currentPaint.setStrokeWidth(2);
        currentPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw objects on the screen
        super.onDraw(canvas);
        drawModel(canvas);
    }

    void drawModel(Canvas canvas) {
        for (int i = 0; i < model.numFaces(); i++) {
            Vector<Integer> face = model.getFace(i);
            for (int j = 0; j < 3; j++) {
                Vec3 v0 = model.getVertex(face.get(j));
                Vec3 v1 = model.getVertex(face.get((j + 1) % 3));
                int x0 = (int)((v0.x + 1) * width / 2);
                int y0 = height - (int)((v0.y + 1) * height / 2);
                int x1 = (int)((v1.x + 1) * width / 2);
                int y1 = height - (int)((v1.y + 1) * height / 2);
                canvas.drawLine(x0, y0, x1, y1, currentPaint);
            }
        }
    }

    @Override
    protected  void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
