package com.example.dietreport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SignatureView extends View {

    private final Paint paint = new Paint();
    private final Path path = new Path();

    private boolean isSigned = false;

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);

        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                isSigned = true;
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                isSigned = true;
                break;

            case MotionEvent.ACTION_UP:
                performClick();
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void clear() {
        path.reset();
        isSigned = false;
        invalidate();
    }

    public boolean isEmpty() {
        return !isSigned;
    }

    /**
     * Returns a landscape-oriented signature bitmap
     */
    public Bitmap getSignatureBitmap() {
        int w = getWidth();
        int h = getHeight();

        if (w == 0 || h == 0) return null;

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.WHITE);
        canvas.drawPath(path, paint);

        // Rotate to landscape if portrait
        if (h > w) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap landscapeBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
            bitmap.recycle(); // free original bitmap
            return landscapeBitmap;
        }

        return bitmap;
    }
}