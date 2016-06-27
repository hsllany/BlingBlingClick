package com.hsllany.blingbling;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hsllany on 16/6/27.
 */
public class BlingBlingClickSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private static final String TAG = "FlashClick";

    private SurfaceHolder mHolder;

    private List<DrawEvent> mDrawEvent = new LinkedList<>();

    private static Paint sRedPaint = new Paint();

    static {
        sRedPaint.setColor(Color.RED);
        sRedPaint.setAntiAlias(true);
    }


    public BlingBlingClickSurfaceView(Context context) {
        super(context);

        init();
    }

    public BlingBlingClickSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BlingBlingClickSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BlingBlingClickSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        setOnTouchListener(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRenderThread = new RenderThread();
        if (mRenderThread != null && !mRenderThread.isAlive())
            mRenderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRenderThread.isRun = false;
        mRenderThread.interrupt();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                DrawEvent drawevent = new DrawEvent(event.getX(), event.getY());
                synchronized (mDrawEvent) {
                    mDrawEvent.add(drawevent);
                }

                synchronized (mRenderThread) {
                    mRenderThread.notify();
                }
                break;
        }
        return false;
    }

    private RenderThread mRenderThread;

    private class RenderThread extends Thread {

        volatile boolean isRun = true;

        @Override
        public void run() {
            while (isRun) {
                Canvas c = null;
                try {
                    synchronized (mHolder) {
                        c = mHolder.lockCanvas();
                        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        synchronized (mDrawEvent) {
                            Iterator<DrawEvent> itr = mDrawEvent.iterator();
                            while (itr.hasNext()) {
                                DrawEvent drawEvent = itr.next();
                                drawEvent.decrementTransparency();

                                Log.d(TAG, "Draw " + drawEvent.x + "," + drawEvent.y + "," + drawEvent.transparency);

                                if (drawEvent.transparency == 0) {
                                    itr.remove();
                                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                    Log.d(TAG, "remove " + drawEvent.x + "," + drawEvent.y);
                                } else {
                                    sRedPaint.setColor(Color.argb(drawEvent.transparency, 200, 200, 200));
                                    c.drawCircle(drawEvent.x, drawEvent.y, drawEvent.radius, sRedPaint);
                                }


                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null)
                        mHolder.unlockCanvasAndPost(c);
                }


                try {
                    int waitTimes = -1;
                    synchronized (mDrawEvent) {
                        if (mDrawEvent.size() > 0) {
                            waitTimes = (int) (1000 / 60.f);
                        }

                    }
                    synchronized (this) {
                        if (waitTimes > 0)
                            wait(waitTimes);
                        else
                            wait();
                    }
                } catch (InterruptedException e) {
                    break;
                }

            }
        }
    }

    ;

    private static class DrawEvent {
        float x, y;
        int transparency;
        float radius;

        public DrawEvent(float x, float y) {
            this.x = x;
            this.y = y;

            this.transparency = 255;
            radius = 5;

        }

        public void decrementTransparency() {
            transparency -= 25;
            radius += 5.f;

            if (transparency < 0)
                transparency = 0;
        }


    }


}
