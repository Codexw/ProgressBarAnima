package com.xu.progressanima.progressbar;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.xu.progressanima.R;


/**
 * 积分水平进度条
 * Created by xu on 2018-5-8.
 */

public class HorProgress extends View {

    private int mBgColor,mTextLevelColor, mProgressColor;
    private int mProgressHeight, mWidth, mMargin; //进度条高度
    private int mTextLevelH = 40; //距离等级文字高度
    private Paint mBgPaint,mLinePaint,mProgressPaint;
    private Paint mTextLevelPaint;
    private RectF mBgRectF, mForRectF;
    private float rX, rY;

    private int mWindowWidth;//屏幕宽度
    private int xWidth,xMargin,wLine =5;//进度条总长度,间隔,线宽
    private float progress = 0;
    private int mSection, mLevel;
    private float mSectionPrecent;

    private final int MaxLevel = 10;

    private int pgb_start_color,pgb_end_color;

    public HorProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mWindowWidth = outMetrics.widthPixels;
        xMargin = mWindowWidth / (MaxLevel + 2);
//        int height = outMetrics.heightPixels;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorProgress);
        mProgressHeight = typedArray.getInt(R.styleable.HorProgress_app_progress_height,30);
//        mWidth = typedArray.getInt(R.styleable.IntegralHorProgress_app_progress_width,30);
//        mMargin = typedArray.getInt(R.styleable.IntegralHorProgress_app_progress_margin,20);
        rX = typedArray.getFloat(R.styleable.HorProgress_app_x_radius,14);
        rY = typedArray.getFloat(R.styleable.HorProgress_app_y_radius,12);
        mBgColor = typedArray.getColor(R.styleable.HorProgress_app_progress_bg_color, Color.LTGRAY);
        mTextLevelColor = typedArray.getColor(R.styleable.HorProgress_app_level_text_color, Color.BLUE);
        mProgressColor = typedArray.getColor(R.styleable.HorProgress_app_progress_color, Color.BLUE);
        pgb_start_color = typedArray.getColor(R.styleable.HorProgress_app_pgb_start_color, Color.BLUE);
        pgb_end_color = typedArray.getColor(R.styleable.HorProgress_app_pgb_end_color, Color.BLUE);

        mBgRectF = new RectF();
        mForRectF = new RectF();

        mBgPaint = new Paint();
        mLinePaint = new Paint();
        mProgressPaint = new Paint();
        mTextLevelPaint = new Paint();

        //背景
        mBgPaint.setStyle(Paint.Style.FILL);//充满
        mBgPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mBgPaint.setColor(mBgColor);

        //分隔
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.WHITE);

        //进度
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(mProgressColor);

        mTextLevelPaint = new Paint();
        mTextLevelPaint.setTextSize(30);
        mTextLevelPaint.setColor(mTextLevelColor);
        mTextLevelPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBanner(canvas);
    }

    private void drawBanner(Canvas canvas) {

        mBgRectF.left = xMargin;
        mBgRectF.top = mTextLevelH;
        mBgRectF.right = mWindowWidth - xMargin;
        mBgRectF.bottom = mProgressHeight + mTextLevelH;
        canvas.drawRoundRect(mBgRectF, rX, rY, mBgPaint);//第二个参数是x半径，第三个参数是y半径

        LinearGradient lg = new LinearGradient(xMargin, mTextLevelH, xMargin * (mLevel + 1), mProgressHeight + mTextLevelH, pgb_start_color, pgb_end_color, Shader.TileMode.MIRROR);
        mProgressPaint.setShader(lg);

        mForRectF.left = xMargin;
        mForRectF.top = mTextLevelH;
        mForRectF.right = wLine + xMargin + progress;
        mForRectF.bottom = mProgressHeight + mTextLevelH;
        canvas.drawRoundRect(mForRectF, rX, rY, mProgressPaint);

        //画分割线和数字等级
        for(int i = 0 ; i <= MaxLevel; i++){
            canvas.drawText(i + "" , xMargin * (i+1) , mTextLevelH / 5 * 3 , mTextLevelPaint);
            if(i == 0 || i == MaxLevel){
                continue;
            }
            Rect rect = new Rect();
            rect.left = xMargin * (i+1);
            rect.top = mTextLevelH;
            rect.right = xMargin * (i+1) + wLine;
            rect.bottom = mProgressHeight + mTextLevelH;
            canvas.drawRect(rect,mLinePaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mWindowWidth;
        int height = getPaddingTop() + getPaddingBottom() + mProgressHeight + mTextLevelH;
        width = resolveSize(width, widthMeasureSpec);
        xWidth = width;
//        xMargin = width / 10;
        height = resolveSize(height, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public float getScoreLevel() {
        return progress;
    }

    public void setScoreLevel(float progress) {
        this.progress = xMargin * (mLevel + mSectionPrecent) * progress / mSectionPrecent;
        invalidate();
    }

    public void setProgressWithAnimation(int section, float progress, int level) {
        if(progress == 0.0){
            level--;
            section = section / 2;
            progress = 1;
        }
        this.mSection = section;
        this.mLevel = level;
        this.mSectionPrecent = progress;
        setProgressWithAnimation(progress, 1500);
    }

    @SuppressLint("ObjectAnimatorBinding")
    public void setProgressWithAnimation(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "ScoreLevel", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

}
