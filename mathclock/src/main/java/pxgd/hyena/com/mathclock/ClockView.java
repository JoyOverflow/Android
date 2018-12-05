package pxgd.hyena.com.mathclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

public class ClockView extends View {

    //表盘图片
    private Drawable clockDrawable;
    //表盘中心点
    private Drawable centerDrawable;

    private Drawable hourDrawable;
    private Drawable minuteDrawable;
    private Drawable seconddDrawable;



    private Paint paint;
    private Thread clockThread;
    private boolean isChange;

    //获取系统时间
    private Time time;

    public ClockView(Context context) {
        this(context, null);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //通过TypedArray对象获取自定义属性（必须被在使用后回收资源）
        TypedArray ta = context.obtainStyledAttributes(
                attrs,
                R.styleable.ClockStyleable, //R文件里的数组
                defStyle,
                0
        );
        clockDrawable =ta.getDrawable(R.styleable.ClockStyleable_clock);
        centerDrawable = ta.getDrawable(R.styleable.ClockStyleable_center_clock);
        hourDrawable = ta.getDrawable(R.styleable.ClockStyleable_hour);
        minuteDrawable = ta.getDrawable(R.styleable.ClockStyleable_minute);
        seconddDrawable = ta.getDrawable(R.styleable.ClockStyleable_second);
        ta.recycle();




        paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setFakeBoldText(true);
        paint.setAntiAlias(true);

        time = new Time();


        //另起一个线程不断更新视图界面（1秒一次）
        clockThread = new Thread() {
            public void run() {
                while(isChange){
                    //更新视图
                    postInvalidate();
                    try{
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置当前的时间
        time.setToNow();

        //计算view的中心位置
        int viewCenterX = (getRight() - getLeft()) / 2;
        int viewCenterY = (getBottom() - getTop()) / 2;

        //获取表盘图片的高度和宽度(即图片的高度和宽度)
        final Drawable dial = clockDrawable;
        int h = dial.getIntrinsicHeight();
        int w = dial.getIntrinsicWidth();



        if ((getRight() - getLeft()) < w || (getBottom() - getTop()) < h) {
            float scale = Math.min((float) (getRight() - getLeft()) / w,
                    (float) (getBottom() - getTop()) / h);
            canvas.save();
            canvas.scale(scale, scale, viewCenterX, viewCenterY);
        }



        if (isChange) {
            dial.setBounds(viewCenterX - (w / 2), viewCenterY
                            - (h / 2), viewCenterX + (w / 2),
                    viewCenterY + (h / 2));
        }
        dial.draw(canvas);
        canvas.save();


        if (isChange) {

            //设置文字大小和是否使用粗体
            paint.setTextSize(40f);
            paint.setFakeBoldText(true);

            int textWidth =(int)paint.measureText("12");
            canvas.drawText(
                    "12",
                    viewCenterX - (textWidth / 2),
                    (float) (viewCenterY - (h / 3.5)),
                    paint
            );

            int textWidth1 = (int) paint.measureText("6");
            canvas.drawText(
                    "6",
                    viewCenterX - (textWidth1 / 2),
                    (float) (viewCenterY + (h / 2.7)),
                    paint
            );

            canvas.drawText("9", (float) (viewCenterX - (w / 2.7)), viewCenterY + 10, paint);
            canvas.drawText("3", (float) (viewCenterX + (w / 3)), viewCenterY + 10, paint);
        }
        canvas.save();



        //用canvas 画时针
        canvas.rotate(time.hour / 12.0f * 360.0f, viewCenterX, viewCenterY);
        Drawable mHour = hourDrawable;
        h = mHour.getIntrinsicHeight();
        w= mHour.getIntrinsicWidth();
        if (isChange) {
            mHour.setBounds(viewCenterX - (w / 2), viewCenterY
                            - h + 20, viewCenterX + (w / 2),
                    viewCenterY + 10);
        }
        mHour.draw(canvas);
        canvas.restore();
        canvas.save();
        //用canvas 话分针
        canvas.rotate(time.minute / 60.0f * 360.0f, viewCenterX, viewCenterY);
        Drawable mMinute = minuteDrawable;
        if (isChange) {
            w = mMinute.getIntrinsicWidth();
            h = mMinute.getIntrinsicHeight();
            mMinute.setBounds(viewCenterX - (w / 2), viewCenterY - h, viewCenterX + (w / 2), viewCenterY + 10);
        }
        mMinute.draw(canvas);
        canvas.restore();
        canvas.save();


        //绘制中心点
        Drawable mCenter = centerDrawable;
        if (isChange) {
            //返回以dp为单位的宽和高
            w = mCenter.getIntrinsicWidth();
            h = mCenter.getIntrinsicHeight();

            mCenter.setBounds(
                    viewCenterX - (w / 2),
                    viewCenterY - (h / 2),
                    viewCenterX + (w / 2),
                    viewCenterY + (h / 2)
            );
        }
        mCenter.draw(canvas);
        canvas.save();

        //用 canvas 画秒针
        canvas.rotate(time.second / 60.0f * 360.0f, viewCenterX, viewCenterY);
        Drawable mSecond = seconddDrawable;
        if (isChange) {
            w = mSecond.getIntrinsicWidth();
            h = mSecond.getIntrinsicHeight();
            mSecond.setBounds(viewCenterX - (w / 2), viewCenterY
                            - h + 50, viewCenterX + (w / 2),
                    viewCenterY + 50);
        }
        mSecond.draw(canvas);
        canvas.restore();
        canvas.save();
    }

    /**
     * 执行在onDraw方法之前（在视图附加到窗体上时调用）
     * 此时视图未绘制,可执行一些初始化操作（如进行广播注册）
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isChange = true;
        clockThread.start();
    }
    /**
     * 视图从窗体上分离时调用（此时视图已不可再被绘制）
     * 可销毁视图时调用（如取消广播注册等的操作）
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isChange = false;
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
