package zity.net.basecommonmodule.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import zity.net.basecommonmodule.R;


/**
 * author：hanshaokai
 * date： 2018/6/29 0029 17:53
 * describe：
 */

public class CustomProgressBar extends View {

    private int pbHedth;
    private int mBgColor;
    private int mShadeColor;
    private Paint mBgPaint;
    private Paint mShadePaint;
    private float mPercent = 0;
    private int pbWidth;
    private RectF mOval1;
    private RectF mOval2;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        mBgColor = typedArray.getColor(R.styleable.CustomProgressBar_pbBgColor, Color.LTGRAY);
        mShadeColor = typedArray.getColor(R.styleable.CustomProgressBar_pbShadeColor, Color.GREEN);
        float f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
        float f2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 585, getResources().getDisplayMetrics());
        pbHedth = (int) typedArray.getDimension(R.styleable.CustomProgressBar_pbHeight, f);
        pbWidth = (int) typedArray.getDimension(R.styleable.CustomProgressBar_pbWidth, f2);
        typedArray.recycle();
        initPain();

    }

    private void initPain() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStrokeWidth(pbHedth);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        mShadePaint = new Paint();
        mShadePaint.setColor(mShadeColor);
        mShadePaint.setStrokeWidth(pbHedth);
        mShadePaint.setStrokeCap(Paint.Cap.ROUND);
        mShadePaint.setAntiAlias(true);
        mShadePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //画圆角矩形
        // 设置个新的长方形
        mOval1 = new RectF();
        // 设置个新的长方形
        mOval2 = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mOval1.left = getWidth() / 2 - pbWidth / 2;
        mOval1.top = getHeight() / 2 - pbHedth / 2;
        mOval1.right = getWidth() / 2 + pbWidth / 2;
        mOval1.bottom = getHeight() / 2 + pbHedth / 2;

        mOval2.left = getWidth() / 2 - pbWidth / 2;
        mOval2.top = getHeight() / 2 - pbHedth / 2;
        mOval2.right = pbWidth * mPercent + getWidth() / 2 - pbWidth / 2;
        mOval2.bottom = getHeight() / 2 + pbHedth / 2;
        canvas.drawRoundRect(mOval1, pbHedth / 2, pbHedth / 2, mBgPaint);//第二个参数是x半径，第三个参数是y半径
        if (mPercent != 0)
            canvas.drawRoundRect(mOval2, pbHedth / 2, pbHedth / 2, mShadePaint);//第二个参数是x半径，第三个参数是y半径
    }

    /**
     * @param percent 百分比0到1
     */
    public void setDrawShadePercent(float percent) {
        mPercent = percent;
        postInvalidate();
    }
}
