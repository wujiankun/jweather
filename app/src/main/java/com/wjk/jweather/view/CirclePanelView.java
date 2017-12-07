package com.wjk.jweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 空气质量指数表盘
 * Created by wujiankun on 2016/6/24.
 */
public class CirclePanelView extends View {

    private Paint paint_arc_bg;
    private Paint paint_arc;
    private Paint paint_text;
    private RectF innerRectf;
    private Paint paint_grey;
    /**
     * 基准标尺
     */
    private float common_size;
    /**
     * 内环
     */
    private float innerLeft;
    private float innerTop;
    private float innerRight;
    private float innerBottom;

    /**
     * 字体大小
     */
    private float textSize;

    /**
     * 画字体时的X坐标
     */
    private float textX;
    private float textY;

    /**
     * 圆心坐标
     */
    private float cX;
    private float cY;
    /**
     * 外圆半径
     */
    private float circleRadius;

    /**
     * 整个总布局长宽度
     */
    private int totalSize;

    DashPathEffect effect = new DashPathEffect(new float[]{3, 6}, 0);
    //private int arcBgColor = Color.parseColor("#40ffffff");
    private int arcBgColor = Color.parseColor("#FF53C44F");
    private int arcColor = Color.parseColor("#ffffff");
    //private int outerCircleColor = Color.parseColor("#d4d4d4");
    private int outerCircleColor = arcColor;



    public CirclePanelView(Context context) {
        this(context, null);
        init();
    }

    public CirclePanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        innerRectf = new RectF();

        paint_arc_bg = new Paint();
        paint_arc_bg.setColor(arcBgColor);
        paint_arc_bg.setAntiAlias(true);
        paint_arc_bg.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_arc_bg.setStyle(Paint.Style.STROKE);

        paint_arc = new Paint();
        paint_arc.setColor(arcColor);
        paint_arc.setAntiAlias(true);
        paint_arc.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_arc.setStyle(Paint.Style.STROKE);


        paint_arc_bg.setPathEffect(effect);
        paint_arc.setPathEffect(effect);

        paint_grey = new Paint();
        paint_grey.setColor(outerCircleColor);
        paint_grey.setAntiAlias(true);
        paint_grey.setStyle(Paint.Style.STROKE);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setTextAlign(Paint.Align.CENTER);
        paint_text.setColor(arcColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = (width > height) ? height : width;
        /** 整个总布局宽度 */
        totalSize = size;
        common_size = totalSize / 18f;
        /** 内环左部 */
        innerLeft = common_size * 1f;
        innerTop = common_size * 1f;
        innerRight = common_size * 17f;
        innerBottom = common_size * 17f;

        arcStrokeWidth = common_size * 1.8f;
        paint_arc.setStrokeWidth(arcStrokeWidth);

        /** 字体大小 */
        textSize = common_size * 3f;
        paint_arc_bg.setStrokeWidth(arcStrokeWidth);

        /** 画字体时的X坐标 */
        textX = (innerRight - innerLeft) / 2f + innerLeft;
        textY = (innerBottom - innerTop) / 2f + innerTop + textSize
                / 2f;

        innerRectf.set(innerLeft+arcStrokeWidth/2, innerTop+arcStrokeWidth/2, innerRight-arcStrokeWidth/2, innerBottom-arcStrokeWidth/2);

        paint_grey.setStrokeWidth(2.0f);
        paint_text.setTextSize(textSize);

        /** 圆心坐标 */
        cX = (innerRight - innerLeft) / 2f + innerLeft;
        cY = (innerBottom - innerTop) / 2f + innerTop;
        circleRadius = (innerBottom - innerTop) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDial(canvas);
        String text = getTextToDraw();
        canvas.drawText(text, textX, textY, paint_text);
        // 画圈
        canvas.drawCircle(cX, cY, circleRadius, paint_grey);
    }

    private String getTextToDraw() {
        return String.valueOf((int)mProgress);
    }


    /**
     * 画短线、表盘刻度
     *
     * @param canvas
     */
    public void drawDial(Canvas canvas) {
        canvas.drawArc(innerRectf, 150f, 240f, false, paint_arc_bg);
        canvas.drawArc(innerRectf, 150f, currentAngle, false, paint_arc);
    }


    private float currentAngle = 0;
    private int maxProgress = 500;
    private float mProgress;
    private float arcStrokeWidth;

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public float getmProgress() {
        return mProgress;
    }
    float tempMax;
    public void setProgress(final float progress) {
        tempMax= maxProgress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (tempMax>progress){
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProgress = tempMax;
                    currentAngle = (mProgress * 240) / maxProgress;
                    tempMax--;
                    postInvalidate();
                }
            }
        }).start();
    }


}

