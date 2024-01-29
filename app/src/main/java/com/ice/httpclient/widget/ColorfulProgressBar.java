package com.ice.httpclient.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ice.httpclient.R;
import com.ice.httpclient.utils.UIUtils;


/**
 * Desc:漂亮的ProgressBar
 * Created by icewater on 2021/12/06.
 */
public class ColorfulProgressBar extends View {

    private int mReachedColor;//进度条到达了的颜色
    private int mUnReachedColor;//进度条未到达的颜色
    private float mReachedHeight;//进度条的到达的高度
    private float mUnReachedHeight;//进度条未到达的高度
    private float mProgressTextOffset;
    private float mProgressTextSize;

    private Paint mReachPaint;//到达画笔
    private Paint mUnReachPaint;//未到达画笔
    private Paint mTextPaint;//文本画笔
    private int width;
    private int height;
    private long mCurrentProgress;
    private long mTotalProgress = 100;
    private int textWith;

    public ColorfulProgressBar(Context context) {
        this(context, null);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfulProgressBar);
        mReachedColor = typedArray.getColor(R.styleable.ColorfulProgressBar_reachedColor, Color.parseColor("#DC143C"));
        mUnReachedColor = typedArray.getColor(R.styleable.ColorfulProgressBar_unReachedColor, Color.rgb(204, 204, 204));
        mReachedHeight = typedArray.getDimension(R.styleable.ColorfulProgressBar_reachedHeight, UIUtils.dp2px(context, 3.5f));
        mUnReachedHeight = typedArray.getDimension(R.styleable.ColorfulProgressBar_unReachedHeight, UIUtils.dp2px(context, 2.5f));
        mProgressTextOffset = typedArray.getDimension(R.styleable.ColorfulProgressBar_progressTextOffset, UIUtils.dp2px(context, 3.0f));
        mProgressTextSize = typedArray.getDimension(R.styleable.ColorfulProgressBar_progressTextSize, UIUtils.sp2px(context, 14.0f));

        typedArray.recycle();

        init();
    }

    private void init() {
        mReachPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachPaint.setColor(mReachedColor);
        mReachPaint.setStrokeWidth(mReachedHeight);

        mUnReachPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnReachPaint.setColor(mUnReachedColor);
        mUnReachPaint.setStrokeWidth(mUnReachedHeight);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mReachedColor);
        mTextPaint.setTextSize(mProgressTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawUnReachLine(canvas);
        drawText(canvas);
        drawReachLine(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            int exceptHeight = (int) (getPaddingTop() + getPaddingBottom() + Math
                    .max(Math.max(mReachedHeight,
                            mUnReachedHeight), Math.abs(textHeight)));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight, MeasureSpec.EXACTLY);

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 画文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        String text = mCurrentProgress * 100 / mTotalProgress + "%";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        textWith = rect.right - rect.left;
        int x = (int) (((float) mCurrentProgress / mTotalProgress) * width + mProgressTextOffset);
        int y =  height/2 + (rect.bottom - rect.top)/2;

        if(x >= width-textWith){
            x = width - textWith;
        }

        canvas.drawText(text,x,y,mTextPaint);
    }


    /**
     * 画未到达的直线
     *
     * @param canvas
     */
    private void drawUnReachLine(Canvas canvas) {
        float startX = 0;
        float startY = height/2;
        float endX = width;
        float endY = height/2;
        canvas.drawLine(startX, startY, endX, endY, mUnReachPaint);
    }

    /**
     * 画到达了的直线
     *
     * @param canvas
     */
    private void drawReachLine(Canvas canvas) {
        float startX = 0;
        float startY = height/2;
        float endX = ((float) mCurrentProgress / mTotalProgress) * width;
        float endY = height/2;

        if(endX >= width-textWith){
            endX = width - textWith;
        }

        canvas.drawLine(startX, startY, endX, endY, mReachPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    public void setProgress(long currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

    public void setMax(long totalProgress) {
        mTotalProgress = totalProgress;
    }
}
