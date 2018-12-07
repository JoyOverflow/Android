package pxgd.hyena.com.testmodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class ClockView extends View {

    //表盘图片
    private Drawable clockDrawable;
    //表盘中心点
    private Drawable centerDrawable;
    //表指针
    private Drawable hourDrawable;
    private Drawable minuteDrawable;
    private Drawable seconddDrawable;

    //private Paint paint;

    //获取系统时间
    private Time time;
    private Thread clockThread;
    private boolean isChange;

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



        time = new Time();









//        paint = new Paint();
//        paint.setColor(Color.parseColor("#ff3333"));
//        paint.setTypeface(Typeface.DEFAULT_BOLD);
//        paint.setFakeBoldText(true);
//        paint.setAntiAlias(true);


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

        //获取表盘图片的高度和宽度(即图片的高度和宽度px)
        final Drawable dial = clockDrawable;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        //计算view的中心位置
        int v_left=getLeft();
        int v_top=getTop();
        int v_right=getRight();
        int v_bottom=getBottom();
        int viewCenterX = (v_right - v_left) / 2;
        int viewCenterY = (v_bottom - v_top) / 2;

        //绘制表盘
        dial.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - (h / 2),
                viewCenterX + (w / 2),
                viewCenterY + (h / 2)
        );
        dial.draw(canvas);
        canvas.save();


        /*
        //设置文字大小和是否使用粗体
        paint.setTextSize(30f);
        paint.setFakeBoldText(true);
        int textWidth =(int)paint.measureText("12");
        float a=viewCenterX - (textWidth / 2);
        float b=(float) (viewCenterY - (h / 3.5));

        float c=(float) (viewCenterY - (h / 2)+0);
        canvas.drawText("12", a, b, paint);

        int textWidth1 =(int)paint.measureText("6");
        canvas.drawText(
                "6",
                viewCenterX - (textWidth1 / 2),
                (float) (viewCenterY + (h / 2.8)),
                paint
        );
        canvas.drawText("9", (float) (viewCenterX - (w / 2.7)), viewCenterY + 10, paint);
        canvas.drawText("3", (float) (viewCenterX + (w / 3)), viewCenterY + 10, paint);
        canvas.save();
        */










        //设置当前的时间（不断刷新）
        time.setToNow();
        int hour1 = time.hour;
        int minute1=time.minute;
        int second1=time.second;

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
                time.hour / 12.0f * 360.0f,
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
        canvas.save();


        //绘制分针
        canvas.rotate(
                time.minute / 60.0f * 360.0f,
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
        canvas.save();

        //绘制秒针
        canvas.rotate(
                time.second / 60.0f * 360.0f,
                viewCenterX,
                viewCenterY
        );
        Drawable mSecond = seconddDrawable;
        w = mSecond.getIntrinsicWidth();
        h = mSecond.getIntrinsicHeight();
        mSecond.setBounds(
                viewCenterX - (w / 2),
                viewCenterY - h + 50,
                viewCenterX + (w / 2),
                viewCenterY + 10
        );
        mSecond.draw(canvas);
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
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
