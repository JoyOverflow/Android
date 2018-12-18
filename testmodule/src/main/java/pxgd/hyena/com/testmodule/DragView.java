package pxgd.hyena.com.testmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DragView extends View {

    private Bitmap mBitmap;
    private Rect rect;
    private static Paint paint;

    private int WIDTH;
    private int heigh, width;
    private int deltaX,deltaY;
    private int mStartX,mStartY;


    float moveX;
    float moveY;

    public DragView(Context context) {
        this(context, null);
    }
    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        WIDTH = 220;
        rect = new Rect(0, 0, WIDTH, WIDTH);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //没有在矩形上点击，不处理触摸消息
                if(!rect.contains(x, y)) {
                   return false;
                }
                moveX = event.getX();
                moveY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:


                float a=getX();
                float b=getY();
                float c=getLeft();
                float d=getTop();

                setTranslationX(getX() + (event.getX() - moveX));
                setTranslationY(getY() + (event.getY() - moveY));

                break;

            case MotionEvent.ACTION_UP:
                break;

        }

        //已处理，事件不再传递
        return true;
    }


    public void fetchImage(int resId){

    }
    public void fetchImage(String url){
        if (url.startsWith("http://")||url.startsWith("https://")){

        }
    }


}
