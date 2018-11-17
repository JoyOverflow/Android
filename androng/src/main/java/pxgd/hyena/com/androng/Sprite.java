package pxgd.hyena.com.androng;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;

public class Sprite
{
   protected double xPosition;
   protected double yPosition;
   protected DrawableResourceCollection drawableResourceCollection;
   private Bitmap internalBitmap;
   private int canvasWidth;
   private int canvasHeight;

   protected Velocity velocity;
   private int currentFrame;
   private double animationSpeedInFpms;
   private long startTimeInMillis;
   private boolean animationForward;

   public Sprite(DrawableResourceCollection drawableResourceCollection, int canvasWidth, int canvasHeight, int animationSpeedInFps, boolean animationForward)
   {
      this.canvasWidth = canvasWidth;
      this.canvasHeight = canvasHeight;
      this.drawableResourceCollection = drawableResourceCollection;
      this.animationSpeedInFpms = animationSpeedInFps / 1000.0;
      this.internalBitmap = ((BitmapDrawable) this.drawableResourceCollection.get(0).getCurrent()).getBitmap();
      this.startTimeInMillis = SystemClock.uptimeMillis();
      this.animationForward = animationForward;
   }

   public double getxPosition()
   {
      return xPosition;
   }

   public void draw(Canvas canvas)
   {
      drawableResourceCollection.get(currentFrame).setBounds((int) xPosition, (int) yPosition, (int) xPosition + getWidth(), (int) yPosition + getHeight());
      drawableResourceCollection.get(currentFrame).draw(canvas);
      currentFrame = GetNewFrame();
   }

   private int GetNewFrame()
   {
      int drawableResourceCollectionSize = drawableResourceCollection.size();
      long elapsedInMillis = SystemClock.uptimeMillis() - startTimeInMillis;
      int frame = (int) ((elapsedInMillis * animationSpeedInFpms) % drawableResourceCollectionSize);
      if (animationForward)
         return frame;
      else
         return drawableResourceCollectionSize - (frame + 1);
   }

   public void canvasChanges(int canvasWidth, int canvasHeight)
   {
      this.canvasWidth = canvasWidth;
      this.canvasHeight = canvasHeight;
   }

   public void setxPosition(int xPosition)
   {
      this.xPosition = xPosition;
   }

   public double getyPosition()
   {
      return yPosition;
   }

   public void setyPosition(int yPosition)
   {
      this.yPosition = yPosition;
   }

   public void reverseAnimation()
   {
      animationForward = !animationForward;
   }

   public int getWidth()
   {
      return drawableResourceCollection.get(currentFrame).getIntrinsicWidth();
   }

   public int getHeight()
   {
      return drawableResourceCollection.get(currentFrame).getIntrinsicHeight();
   }

   public void Move(double frameTime)
   {
      double newXPosition = velocity.getNewXPosition(xPosition, frameTime);
      if (newXPosition + getWidth() < (canvasWidth - 4) && newXPosition > 4)
         xPosition = newXPosition;

      yPosition = velocity.getNewYPosition(yPosition, frameTime);
   }

   public boolean CollidesWith(Sprite sprite, double frameTime)
   {
      int left1, left2;
      int right1, right2;
      int top1, top2;
      int bottom1, bottom2;

      left1 = (int) this.getNewXPosition(frameTime);
      left2 = (int) sprite.getNewXPosition(frameTime);
      right1 = left1 + this.getWidth();
      right2 = left2 + sprite.getWidth();
      top1 = (int) this.getNewYPosition(frameTime);
      top2 = (int) sprite.getNewYPosition(frameTime);
      bottom1 = top1 + this.getHeight();
      bottom2 = top2 + sprite.getHeight();

      if (bottom1 < top2)
         return false;
      if (top1 > bottom2)
         return false;
      if (right1 < left2)
         return false;
      if (left1 > right2)
         return false;

      int over_bottom;
      int over_top;
      int over_right;
      int over_left;

      if (bottom1 > bottom2)
         over_bottom = bottom2;
      else
         over_bottom = bottom1;

      if (top1 < top2)
         over_top = top2;
      else
         over_top = top1;

      if (right1 > right2)
         over_right = right2;
      else
         over_right = right1;

      if (left1 < left2)
         over_left = left2;
      else
         over_left = left1;

      for (int xPos = over_left; xPos < over_right; xPos++)
      {
         for (int yPos = over_top; yPos < over_bottom; yPos++)
         {
            int thisColor = this.getPixel(xPos, yPos);
            int spriteColor = sprite.getPixel(xPos, yPos);
            if (thisColor != 0 && spriteColor != 0)
            {
               return true;
            }
         }
      }

      return false;
   }

   public double getNewXPosition(double frameTime)
   {
      return velocity.getNewXPosition(xPosition, frameTime);
   }

   public double getNewYPosition(double frameTime)
   {
      return velocity.getNewYPosition(yPosition, frameTime);
   }

   private int getPixel(int x, int y)
   {
      int xPosInBitmap;
      if (x > getxPosition())
         xPosInBitmap = x - (int) getxPosition();
      else
         xPosInBitmap = (int) getxPosition() - x;

      int yPosInBitmap;
      if (y > getyPosition())
         yPosInBitmap = y - (int) getyPosition();
      else
         yPosInBitmap = (int) getyPosition() - y;

      if (x >= internalBitmap.getWidth() || y >= internalBitmap.getHeight())
         return 1;

      if (yPosInBitmap < this.internalBitmap.getHeight() && xPosInBitmap < internalBitmap.getWidth())
         return this.internalBitmap.getPixel(xPosInBitmap, yPosInBitmap);
      else
         return 0;
   }

   public void center()
   {
      centerHorizontal();
      centerVertical();
   }

   public void centerHorizontal()
   {
      setxPosition(canvasWidth / 2 - getWidth() / 2);
   }

   public void centerVertical()
   {
      setyPosition(canvasHeight / 2 - getHeight() / 2);
   }

   public boolean IsMaximumRight(int canvasWidth)
   {
      return getxPosition() >= (canvasWidth - getWidth());
   }

   public void SetMaximumRight(int canvasWidth)
   {
      setxPosition(canvasWidth - getWidth());
   }

   public void setVelocity(Velocity velocity)
   {
      this.velocity = velocity;
   }

   public void setPreviousLocation(double frameTime)
   {
      xPosition = velocity.getPreviousXPosition(xPosition, frameTime);
      yPosition = velocity.getPreviousYPosition(yPosition, frameTime);
   }

   public boolean IsOutOfXBounds(double frameTime)
   {
      return (getNewXPosition(frameTime) <= 7) || (getNewXPosition(frameTime) + getWidth() >= canvasWidth - 7);
   }

   public boolean IsOutOfLowerBounds(double frameTime)
   {
      return ((int) getNewYPosition(frameTime) + getHeight() >= canvasHeight - 10);
   }

   public boolean IsOutOfUpperBounds(double frameTime)
   {
      return getNewYPosition(frameTime) <= 10;
   }
}
