package pxgd.hyena.com.customclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class ClockView extends View {

    //钟表图片资源
    private Drawable clockDrawable;
    private Drawable centerDrawable;
    private Drawable hourDrawable;
    private Drawable minuteDrawable;
    private Drawable seconddDrawable;

    private Thread clockThread;
    private boolean isChange;
    private Rect rect;

    float moveX;
    float moveY;

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
        centerDrawable = ta.getDrawable(R.styleable.ClockStyleable_center);
        hourDrawable = ta.getDrawable(R.styleable.ClockStyleable_hour);
        minuteDrawable = ta.getDrawable(R.styleable.ClockStyleable_minute);
        seconddDrawable = ta.getDrawable(R.styleable.ClockStyleable_second);
        ta.recycle();

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

        //计算view的中心位置
        int viewCenterX = (getRight() - getLeft()) / 2;
        int viewCenterY = (getBottom() -getTop()) / 2;


        //获取表盘图片的高度和宽度(即图片的高度和宽度px)
        final Drawable dial = clockDrawable;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        int left=viewCenterX - (w / 2);
        int top=viewCenterY - (h / 2);
        int right=viewCenterX + (w / 2);
        int bottom=viewCenterY + (h / 2);

        //绘制表盘
        dial.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - (h / 2),
                viewCenterX + (w / 2),
                viewCenterY + (h / 2)
        );
        dial.draw(canvas);
        canvas.save();

        rect = new Rect(left,top,right,bottom);


        //获得系统时间（毫秒）
        //返回从1970.1.1午夜开始经过的毫秒数（UTC）
        long millis=System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);


        //绘制时针
        canvas.rotate(
                hour / 12.0f * 360.0f,
                viewCenterX,
                viewCenterY
        );
        Drawable mHour = hourDrawable;
        h=mHour.getIntrinsicHeight();
        w=mHour.getIntrinsicWidth();
        mHour.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - h + 20,
                viewCenterX + (w / 2),
                viewCenterY + 10
        );
        mHour.draw(canvas);
        //防止重复旋转对画布造成影响
        canvas.restore();
        canvas.save();



        //绘制分针
        canvas.rotate(
                minute / 60.0f * 360.0f,
                viewCenterX,
                viewCenterY
        );
        Drawable mMinute = minuteDrawable;
        w = mMinute.getIntrinsicWidth();
        h = mMinute.getIntrinsicHeight();
        mMinute.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - h,
                viewCenterX + (w / 2),
                viewCenterY + 10
        );
        mMinute.draw(canvas);
        canvas.restore();
        canvas.save();



        //绘制秒针
        canvas.rotate(
                second / 60.0f * 360.0f,
                viewCenterX,
                viewCenterY
        );
        Drawable mSecond = seconddDrawable;
        w = mSecond.getIntrinsicWidth();
        h = mSecond.getIntrinsicHeight();

        int s1=viewCenterX - (w / 2);
        int s2=viewCenterY - h + 50;
        int s3=viewCenterX + (w / 2);
        int s4=viewCenterY + 10;
        mSecond.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - h + 20,
                viewCenterX + (w / 2),
                viewCenterY + 10
        );
        mSecond.draw(canvas);
        canvas.restore();
        canvas.save();


        //绘制中心点
        Drawable mCenter = centerDrawable;
        w = mCenter.getIntrinsicWidth();
        h = mCenter.getIntrinsicHeight();
        mCenter.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - (h / 2),
                viewCenterX + (w / 2),
                viewCenterY + (h / 2)
        );
        mCenter.draw(canvas);
        canvas.save();
    }



    @Override
    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        super.addChildrenForAccessibility(outChildren);
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        isChange = true;
        clockThread.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        isChange = false;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                //没有在矩形上点击，不处理触摸消息
                if(!rect.contains(x, y)) {
                    return false;
                }
                else{
                    moveX = x;
                    moveY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                setTranslationX(getX() + (event.getX() - moveX));
                setTranslationY(getY() + (event.getY() - moveY));
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        //已处理，事件不再传递
        return true;
    }
}
