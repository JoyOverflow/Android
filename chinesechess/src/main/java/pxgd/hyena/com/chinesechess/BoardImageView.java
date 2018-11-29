package pxgd.hyena.com.chinesechess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class BoardImageView extends AppCompatImageView {

    Bitmap[][] mChessBitmaps = new Bitmap[2][7];
    Bitmap mSelectBitmap = null;
    float mLaticeLen = -1 ;
    float mLaticeLen2;
    float mChesslen ;
    float mChessLen2;
    int mChessFrom = -1;
    int mChessTo = -1;
    float mStartBoardX;
    float mStartBoardY;
    boolean mIsComputerThinking =false;
    // back up of ai status for drawing
    int[] mPieces = new int[AI.BOARD_SIZE];
    int[] mColors = new int[AI.BOARD_SIZE];
    AI mAi = new AI();
    Context mContext;
    TextView mInfoTextView;


    public BoardImageView(Context context) {
        super(context);
        init(context);
    }
    public BoardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    void init(Context context){
        mContext = context;
        newGame();
    }
    void newGame() {
        mAi.init();
        mChessFrom = -1;
        mChessTo = -1;
        mIsComputerThinking = false;

        //修改数组mPieces和mColors得到棋子和颜色信息（那一方）
        System.arraycopy(mAi.piece, 0, mPieces, 0, mPieces.length);
        System.arraycopy(mAi.color, 0, mColors, 0, mColors.length);
        postInvalidate();
    }
    private int canvasCoord2ChessIndex(PointF point) {
        Point logicPoint = new Point(
                (int)((point.x - mStartBoardX + mLaticeLen2)/mLaticeLen),
                (int)((point.y - mStartBoardY +mLaticeLen2)/mLaticeLen)
        );
        int index =logicPoint.x + logicPoint.y * 9;
        if (index >= AI.BOARD_SIZE || index < 0) {
            return -1;
        }
        return index;
    }



//    protected int dp2px(int dp){
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,getResources().getDisplayMetrics());
//    }
    public int px2dp(float pxValue) {
        final float scale =  mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLaticeLen <0) {

            //加载棋子图像
            Bitmap chessBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qz);

            //返回px
            int width=getWidth();
            int height=getHeight();
            int qz_width=chessBitmap.getWidth();

            int width3=px2dp(width);//320
            int height3=px2dp(height);//354
            int qz_width3=px2dp(qz_width);//490

            //一个棋子的大小
            mLaticeLen = chessBitmap.getWidth() / 14;
            mChesslen = mLaticeLen*1.005f;//将棋子大小放大倍数

            mChessLen2 = mChesslen /2.0f;
            mStartBoardX = width * 20.0f / 320;
            mStartBoardY = height * 20.0f /354;

            int stepH = chessBitmap.getHeight() / 3;
            int stepW = chessBitmap.getWidth() / 14;
            for (int i = 0; i < 7; i++) {
                mChessBitmaps[AI.LIGHT][i] = Bitmap.createBitmap(chessBitmap, i * stepW, 0, stepW, stepH);
                mChessBitmaps[AI.DARK][i] = Bitmap.createBitmap(chessBitmap, (i+7) * stepW, 0, stepW, stepH);
            }
            //二种颜色的棋子(二方)
            chessBitmap = mChessBitmaps[0][0];
            mChessBitmaps[0][0] = mChessBitmaps[0][6];
            mChessBitmaps[0][6] = chessBitmap;
            chessBitmap = mChessBitmaps[1][0];
            mChessBitmaps[1][0] = mChessBitmaps[1][6];
            mChessBitmaps[1][6] = chessBitmap;
            chessBitmap = mChessBitmaps[0][4];
            mChessBitmaps[0][4] = mChessBitmaps[0][5];
            mChessBitmaps[0][5] = chessBitmap;
            chessBitmap = mChessBitmaps[1][4];
            mChessBitmaps[1][4] = mChessBitmaps[1][5];
            mChessBitmaps[1][5] = chessBitmap;
            mSelectBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sel);
        }


        //画每一个棋子（空白位内不绘制）
        int k=0;
        for(int i =0 ; i < AI.BOARD_SIZE ; i++) {
            if (mPieces[i] != AI.EMPTY) {
                PointF point = chessIndex2CanvasCoord(i);
                Bitmap bmp = mChessBitmaps[mColors[i]][mPieces[i]];
                /*
                //ouyj（仅显示一边的棋盘）
                if(point.y>500)
                    continue;
                */

                canvas.drawBitmap(bmp,
                        null,
                        new RectF(
                                point.x - mChessLen2,
                                point.y - mChessLen2,
                                point.x + mChessLen2,
                                point.y + mChessLen2
                        ),
                        null
                );
                k+=1;
//                if(k>2)
//                  break;

            }
        }

        // draw selected positions
        if (mChessFrom >=0 ) {
            PointF point = chessIndex2CanvasCoord(mChessFrom);
            canvas.drawBitmap(mSelectBitmap, null,new RectF(point.x - mChessLen2, point.y - mChessLen2, point.x + mChessLen2, point.y + mChessLen2), null);
        }
        if (mChessTo >=0 ) {
            PointF point = chessIndex2CanvasCoord(mChessTo);
            canvas.drawBitmap(mSelectBitmap, null,new RectF(point.x - mChessLen2, point.y - mChessLen2, point.x + mChessLen2, point.y + mChessLen2), null);
        }

        super.onDraw(canvas);
    }
    private PointF chessIndex2CanvasCoord(int i) {
        PointF point = new PointF(chessIndex2LogicPoint(i));

        float x=point.x;
        float y=point.y;

        //point.x *= mLaticeLen ;
        point.x *=mChesslen; //每棋子的宽度
        point.x += mStartBoardX; //棋子间的水平间隔
        //point.y *= mLaticeLen ;
        point.y *= mChesslen ;
        point.y += mStartBoardY;//棋子间的垂直间隔
        return point;
    }
    //i=0到89
    private Point chessIndex2LogicPoint(int i) {
        //return new Point(i%9,i/9);
        return new Point(i%9,i/9);
    }
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
