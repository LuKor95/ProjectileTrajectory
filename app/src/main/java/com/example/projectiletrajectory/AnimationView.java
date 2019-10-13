package com.example.projectiletrajectory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;

public class AnimationView extends View {

    Paint paint;
    Bitmap bm, bm_background;
    int bm_offsetX, bm_offsetY;

    Path animPath;
    PathMeasure pathMeasure;

    float pathLength;
    float step;
    float distance;
    float[] pos;
    float[] tan;

    Matrix matrix;

    private ArrayList<Coordinates> coords;

    public AnimationView(Context context) {
        super(context);
        Log.i("TEXT", context.toString());
        initMyView();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView();
    }

    public AnimationView(Context context, ArrayList<Coordinates> coords) {
        super(context);
        this.coords = coords;

        initMyView();
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView();
    }


    public void initMyView() {
        float offsetX = 100;
        float offsetY = 1400;

        float scale = 10;

        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        bm_background = BitmapFactory.decodeResource(getResources(),R.drawable.solar_system);
        bm_offsetX = bm.getWidth() / 2;
        bm_offsetY = bm.getHeight() / 2;

        animPath = new Path();
        animPath.moveTo(offsetX, offsetY);
        for (Coordinates c : coords) {
            animPath.lineTo(offsetX + ((float) c.getX() * scale), offsetY - ((float) c.getY() * scale));
        }

        pathMeasure = new PathMeasure(animPath, false);
        pathLength = pathMeasure.getLength();

        step = 2;
        distance = 0;
        pos = new float[2];
        tan = new float[2];

        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(animPath, paint);
        canvas.drawBitmap(bm_background,0,0,null);

        if (distance < pathLength) {
            pathMeasure.getPosTan(distance, pos, tan);

            matrix.reset();
            float degrees = (float) (atan2(tan[1], tan[0]) * 180.0 / PI) + 90;
            matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            matrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);

            canvas.drawBitmap(bm, matrix, null);

            distance += step;
            invalidate();
        } else {
            distance = 0;
            canvas.drawBitmap(bm, matrix, null);
        }
    }
}