package com.wjk.jweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 空气质量柱状图
 * Created by wujiankun on 2016/6/24.
 */
public class TawerView extends View {

    private Paint paintMain;
    private Paint paintBg;
    private Paint paintText;
    private RectF outterRectf;
    private RectF outterRectfBg;
    private RectF innererRectf;
    /**
     * 基准标尺
     */
    private float common_size;

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
     * 整个总布局长宽度
     */
    private int totalHeight;
    private int totalWidth;
    //private int bgColor = Color.TRANSPARENT;
    private int bgColor =  Color.parseColor("#aa53C44F");
    private int mainColor = Color.parseColor("#ffffff");
    private int textColor = Color.parseColor("#ffffff");

    public TawerView(Context context) {
        this(context, null);
        init();
    }

    public TawerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //bgColor = getResources().getColor(R.color.colorBgPrimary);
        init();
    }

    private void init() {
        paintMain = new Paint();
        paintMain.setColor(mainColor);
        paintMain.setAntiAlias(true);
        paintMain.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintMain.setStyle(Paint.Style.FILL);

        paintBg = new Paint();
        paintBg.setColor(bgColor);
        paintBg.setAntiAlias(true);
        paintBg.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintBg.setStyle(Paint.Style.FILL);

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setColor(textColor);
        outterRectf = new RectF();
        innererRectf = new RectF();
        outterRectfBg = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        /** 整个总布局宽度 */
        totalHeight = height;
        totalWidth = width;
        common_size = totalHeight / 20f;
        textSize = common_size * 2.5f;
        outterRectf.set(totalWidth/4f,textSize*2,totalWidth*3/4f,totalHeight);

        /** 画字体时的X坐标 */
        textX = (totalWidth) / 2f;
        textY = textSize;
        paintText.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = totalWidth/4;
        if(mProgress<=10){
            innererRectf.set(totalWidth/4f,totalHeight,totalWidth*3/4f,totalHeight);
        }else {
            innererRectf.set(totalWidth/4f,(totalHeight-textSize*2)*(1.0f-mProgress/maxProgress)+textSize*2,totalWidth*3/4f,totalHeight);
        }
        canvas.drawRoundRect(outterRectf,radius,radius,paintBg);
        canvas.drawRoundRect(innererRectf,10,10,paintMain);
        String text = getTextToDraw();
        canvas.drawText(text, textX, textY, paintText);
    }

    private String getTextToDraw() {
        return String.valueOf((int)mProgress);
    }

    private float maxProgress = 300.0f;
    private float mProgress = 0;
    private int progressPercent;
    private float temp;


    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public float getmProgress() {
        return mProgress;
    }

    public void setProgress(final float progress) {
        temp = maxProgress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (temp>=progress){
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProgress = temp;
                    postInvalidate();
                    temp--;
                }
            }
        }).start();
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }
}

