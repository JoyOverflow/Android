package pxgd.hyena.com.dragdraw;

import android.graphics.PointF;

public class Box {

    //原始触摸点
    private PointF mOrigin;
    //当前触摸点
    private PointF mCurrent;

    /**
     * 构造：首先原始和当前触摸点为同一个
     * @param origin
     */
    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    //设置当前点
    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getCurrent() {
        return mCurrent;
    }
    public PointF getOrigin() {
        return mOrigin;
    }
}
