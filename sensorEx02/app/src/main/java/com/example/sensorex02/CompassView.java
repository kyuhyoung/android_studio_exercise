package com.example.sensorex02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class CompassView extends View {

    private Drawable mCompass;
    private float mAzimuth = 0;
    private int padding = 3;

    public CompassView(Context context)
    {
        super(context);
        int wid = 100, hei = 100;
        Drawable dr = context.getResources().getDrawable(R.drawable.compass_icon);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to 50 x 50
        this.mCompass = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, wid, hei, true));
        //this.mCompass = context.getResources().getDrawable((R.drawable.compass_icon);
    }
    protected void onDraw(Canvas canvas)
    {
        canvas.save();
        canvas.rotate(360 - mAzimuth,mCompass.getMinimumWidth() / 2, mCompass.getMinimumHeight() / 2);
        mCompass.setBounds(padding, padding, padding + mCompass.getMinimumWidth(), padding + mCompass.getMinimumHeight());
        mCompass.draw(canvas);
        canvas.restore();
        super.onDraw(canvas);
    }

    public void setAzimuth(float azimuth)
    {
        mAzimuth = azimuth;
    }
}
