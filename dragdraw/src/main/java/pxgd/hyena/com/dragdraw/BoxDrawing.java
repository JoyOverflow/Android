package pxgd.hyena.com.dragdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawing extends View {

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private static final String TAG = "BoxDrawing";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();


    public BoxDrawing(Context context) {
        this(context, null);
    }
    public BoxDrawing(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //定义画笔
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }


    /**
     * 处理触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //触摸时的X和Y坐标
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                //设置起点
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    //不断更新当前触摸点
                    mCurrentBox.setCurrent(current);
                    //强制视图重绘（调用onDraw）
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        //记录日志
        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
        //事件已被处理
        return true;
    }

    /**
     * 视图绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画背景的画笔
        canvas.drawPaint(mBackgroundPaint);

        //遍历矩形集合
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            //画矩形
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

}
