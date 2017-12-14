package com.wjk.jweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wjk.jweather.R;
import com.wjk.jweather.util.CommonUtil;

/**
 * 温度曲线
 * Created by wujiankun on 2016/6/24.
 */
public class WithLineTempView extends View {

    private boolean mSetupPending;
    private boolean mReady;
    private Paint paint_line;
    private Paint paint_dot;
    private Paint paint_text;
    private Path mLinePathHigh;
    private Path mLinePathLow;
    /**
     * 默认行数2
     */
    private boolean singleRow;
    /**
     * 默认宽度 单位dp
     */
    private int defaultWidth = 45;
    /**
     * 默认高度 单位dp
     */
    private int defaultHeight = 100;
    /**
     * 基准标尺
     */
    private float common_size;
    /**
     * 字体大小
     */
    private float textSize;

    /**
     * 气温点默认半径 单位dp
     */
    private float dotRadius = 3;
    private int dotColor = Color.parseColor("#ffffff");
    private int lineColor = Color.parseColor("#ffffff");
    private int highTempColor = Color.parseColor("#FFDFA11A");
    private int textColor = Color.parseColor("#ffffff");
    private int[] tempsLast;
    private int[] tempsNext;


    public WithLineTempView(Context context) {
        super(context);
        init();
    }

    public WithLineTempView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WithLineTempView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        dotRadius = CommonUtil.dip2px(context,dotRadius);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WithLineTempView, defStyle, 0);
        dotRadius = a.getDimension(R.styleable.WithLineTempView_dot_radius, dotRadius);
        dotColor = a.getColor(R.styleable.WithLineTempView_dot_color, dotColor);
        lineColor = a.getColor(R.styleable.WithLineTempView_line_color, lineColor);
        textColor = a.getColor(R.styleable.WithLineTempView_text_color, textColor);
        singleRow = a.getBoolean(R.styleable.WithLineTempView_single_row, false);
        if (singleRow) {
            defaultHeight /= 2;
        }
        defaultHeight = CommonUtil.dip2px(context,defaultHeight);
        defaultWidth = CommonUtil.dip2px(context,defaultWidth);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        defaultWidth = Math.max(defaultWidth,screenWidth/7);
        a.recycle();
        init();
    }

    private void init() {
        paint_line = new Paint();
        paint_line.setColor(lineColor);
        paint_line.setAntiAlias(true);
        paint_line.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_line.setStyle(Paint.Style.STROKE);
        mLinePathHigh = new Path();
        mLinePathLow = new Path();

        paint_dot = new Paint();
        paint_dot.setColor(dotColor);
        paint_dot.setAntiAlias(true);
        paint_dot.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint_dot.setStyle(Paint.Style.FILL);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setTextAlign(Paint.Align.CENTER);
        paint_text.setColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("wjk","view onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            defaultWidth = MeasureSpec.getSize(widthMeasureSpec);

        }
        if (heightMode == MeasureSpec.EXACTLY) {
            defaultHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        common_size = defaultWidth / 10f;
        paint_line.setStrokeWidth(common_size /4f);

        textSize = common_size * 2.5f;
        paint_text.setTextSize(textSize);



        /*
        *   气温数组的倒数第一个值是最高气温，倒数第二个值是最低气温
        *   气温范围(拿到最高和最低气温)共n个点，总高度减去两个字体高度得出的值是气温点的绘制区域范围
        *   计算气温点的坐标（主要是y坐标）
        *   再根据气温点坐标计算出字体坐标
        *   分别算出前一天和后一天最高、最低气温点的坐标，向两个边界方向绘制线段
        * */

        for(int i=0;i<temps.length-2;i++){
            int temp = temps[i];
            dotY[i]= calculateDotY(temp);
            if(tempsLast!=null){
                int tempLast = tempsLast[i];
                dotLast[i]= calculateDotY(tempLast);
            }
            if(tempsNext!=null){
                int tempNext = tempsNext[i];
                dotNext[i]= calculateDotY(tempNext);
            }
        }

        setMeasuredDimension(defaultWidth, defaultHeight);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("wjk","view onDraw");
        super.onDraw(canvas);
        if(dotY!=null&&temps!=null&&temps.length>=3){
            for(int i=0;i<temps.length-2;i++){
                String[] text = getTextToDraw(temps);
                if(i==0){
                    canvas.drawText(text[i], defaultWidth/2, dotY[i]-textSize/2, paint_text);
                    if(dotLast!=null){
                        mLinePathHigh.moveTo(-defaultWidth/2,dotLast[i]);
                        mLinePathHigh.lineTo(defaultWidth/2,dotY[i]);
                    }else{
                        mLinePathHigh.moveTo(defaultWidth/2,dotY[i]);
                    }
                    if(dotNext!=null){
                        mLinePathHigh.lineTo(defaultWidth*3f/2,dotNext[i]);
                    }
                    paint_line.setColor(highTempColor);
                    canvas.drawPath(mLinePathHigh,paint_line);
                }else{
                    canvas.drawText(text[i], defaultWidth/2, dotY[i]+textSize*5f/4, paint_text);
                    if(dotLast!=null){
                        mLinePathLow.moveTo(-defaultWidth/2,dotLast[i]);
                        mLinePathLow.lineTo(defaultWidth/2,dotY[i]);
                    }else{
                        mLinePathLow.moveTo(defaultWidth/2,dotY[i]);
                    }
                    if(dotNext!=null){
                        mLinePathLow.lineTo(defaultWidth*3f/2,dotNext[i]);
                    }
                    paint_line.setColor(lineColor);
                    canvas.drawPath(mLinePathLow,paint_line);
                }
                canvas.drawCircle(defaultWidth/2, dotY[i], dotRadius, paint_dot);
            }
        }
    }

    private float calculateDotY(int temp) {
        int highTemp = temps[temps.length-1];
        int lowTemp = temps[temps.length-2];
        int dotPos= highTemp-lowTemp+1;
        float dotUnit = (defaultHeight-textSize*2.5f)/dotPos;
        return textSize*1.3f+(highTemp-temp)*dotUnit;
    }

    private int[] temps;

    private String[] getTextToDraw(int[] temps) {
        String [] results = new String[temps.length];
        for (int i=0;i<temps.length;i++){
            results[i] = temps[i]+"℃";
        }
        return results;
    }

    float [] dotY = null;
    float [] dotLast= null;
    float [] dotNext= null;

    /**
     * 给view设置气温数据组，倒数第一个值是最高气温，倒数第二个值是最低气温
     *
     * @param temps
     */
    public void setData(int[] temps, int[] tempsLast, int[] tempsNext) {
        Log.i("wjk","view Set data");
        if (temps.length < 3) {
            return;
        }
        this.temps = temps;
        this.tempsLast = tempsLast;
        this.tempsNext = tempsNext;
        dotY = new float[temps.length-2];
        if(tempsLast!=null){
            dotLast= new float[temps.length-2];
        }

        if(tempsNext!=null){
            dotNext= new float[temps.length-2];
        }


        //invalidate();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                *//*while (tempMax>progress){
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProgress = tempMax;
                    currentAngle = (mProgress * 240) / maxProgress;
                    tempMax--;
                    postInvalidate();
                }*//*
            }
        }).start();*/
    }


}

