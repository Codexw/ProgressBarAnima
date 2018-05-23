package com.xu.progressanima.progressbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.xu.progressanima.R;


/**
 * 积分圆形进度条
 * Created by Xu on 2018-5-8.
 */
public class CirProgress extends View {

    private float mProgress = 0;
    private float mStrokeWidth = 10;
    private float mBgStrokeWidth = 10;
    private int color = Color.BLACK;
    private int backgroundColor = Color.GRAY;

    private int startAngle = 90;
    private RectF mRectF;
    private Paint mBgPaint;
    private Paint mForPaint;
    private Paint mTextPaint, mNumPaint;

    private float tvX,tvY;
    private int mLackScore;
    private float mTotalPrecent;
    private int mTextDistance = 60;

    public CirProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CirProgress, 0, 0);
        try {
//            mProgress = typedArray.getFloat(R.styleable.IntegralCirProgress_app_progress, mProgress);
            mStrokeWidth = typedArray.getDimension(R.styleable.CirProgress_app_progressbar_width, mStrokeWidth);
            mBgStrokeWidth = typedArray.getDimension(R.styleable.CirProgress_app_background_progressbar_width, mBgStrokeWidth);
            color = typedArray.getInt(R.styleable.CirProgress_app_progressbar_color, color);
            backgroundColor = typedArray.getInt(R.styleable.CirProgress_app_background_progressbar_color, backgroundColor);
        } finally {
            typedArray.recycle();
        }

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(backgroundColor);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mBgStrokeWidth);

        mForPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForPaint.setColor(color);
        mForPaint.setStyle(Paint.Style.STROKE);
        mForPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(color);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mNumPaint = new Paint();
        mNumPaint.setTextSize(40);
        mNumPaint.setColor(Color.BLACK);
        mNumPaint.setTextAlign(Paint.Align.CENTER);

//        picPaint = new Paint();
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mypoints);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(mRectF, mBgPaint);
        float angle = 360 * mProgress;
        canvas.drawArc(mRectF, startAngle, angle, false, mForPaint);
//        canvas.drawBitmap(bitmap, 0, 35, picPaint);

        String score;
        if(mProgress == 0.0){
            score = String.valueOf(mLackScore);
        }else {
            score = String.format("%.0f", mLackScore * mProgress / mTotalPrecent);
        }
        String stitle = "还需";
        canvas.drawText(stitle,getWidth()/2 , getHeight()/2 , mNumPaint);
        canvas.drawText(score , getWidth()/2 , getHeight()/2 + mTextDistance , mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (mStrokeWidth > mBgStrokeWidth) ? mStrokeWidth : mBgStrokeWidth;
        tvX = width / 2;
        tvY = height / 2;
        mRectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float precent) {
        this.mProgress = (precent <= 1) ? precent : 1;
        invalidate();
    }

    public void setProgressWithAnimation(int requirScore, float precent) {
        this.mLackScore = requirScore;
        mTotalPrecent = precent;
        setProgressWithAnimation(precent, 1500);
    }

    public void setProgressWithAnimation(float precent, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", precent);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
