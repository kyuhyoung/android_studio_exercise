package com.example.sensorex02;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class CompassView extends View {

    private Drawable mCompass;
    private float mAzimuth = 0;
    private int padding = 3;

    public CompassView(Context context)
    {
        super(context);
        this.mCompass = context.getResources().getDrawable(R.drawable.compass_icon);
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
