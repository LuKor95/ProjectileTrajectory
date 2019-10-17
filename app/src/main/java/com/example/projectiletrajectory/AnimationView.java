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
import android.view.View;
import java.util.ArrayList;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;

public class AnimationView extends View {

    Paint paint;
    Bitmap image, image_background;
    Path path;
    PathMeasure pathMeasure;
    Matrix matrix;

    int image_offsetX, image_offsetY;
    float pathLength;
    float step;
    float distance;
    float[] pos;
    float[] tan;

    private ArrayList<Coordinates> coords;

    public AnimationView(Context context) {
        super(context);
        createAnimation();
    }

    public AnimationView(Context context, ArrayList<Coordinates> coords) {
        super(context);
        this.coords = coords;
        createAnimation();
    }

    public void createAnimation() {
        float offsetX = 100;
        float offsetY = 1400;
        float scale = 10;

        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        image = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        image_background = BitmapFactory.decodeResource(getResources(),R.drawable.solar_system);

        image_offsetX = image.getWidth() / 2;
        image_offsetY = image.getHeight() / 2;

        path = new Path();
        path.moveTo(offsetX, offsetY);
        for (Coordinates c : coords) {
            path.lineTo(offsetX + ((float) c.getX() * scale), offsetY - ((float) c.getY() * scale));
        }

        pathMeasure = new PathMeasure(path, false);
        pathLength = pathMeasure.getLength();

        step = 5;
        distance = 0;
        pos = new float[2];
        tan = new float[2];

        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawBitmap(image_background,0,0,null);

        if (distance < pathLength) {
            pathMeasure.getPosTan(distance, pos, tan);
            matrix.reset();
            float degrees = (float) (atan2(tan[1], tan[0]) * 180.0 / PI) + 90;
            matrix.postRotate(degrees, image_offsetX, image_offsetY);
            matrix.postTranslate(pos[0] - image_offsetX, pos[1] - image_offsetY);

            canvas.drawBitmap(image, matrix, null);

            distance += step;
            invalidate();
        } else {
            distance = 0;
            canvas.drawBitmap(image, matrix, null);
        }
    }
}