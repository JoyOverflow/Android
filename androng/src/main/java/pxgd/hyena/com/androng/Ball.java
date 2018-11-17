package pxgd.hyena.com.androng;

public class Ball extends Sprite
{
   private VelocityGenerator velocityGenerator;

   public Ball(DrawableResourceCollection drawableResourceCollection, int canvasWidth, int canvasHeight, VelocityGenerator velocityGenerator)
   {
      super(drawableResourceCollection, canvasWidth, canvasHeight, 12, true);
      this.drawableResourceCollection = drawableResourceCollection;
      this.velocityGenerator = velocityGenerator;
   }

   public void reverseXVelocity()
   {
      this.velocity.ReverseX();
      this.reverseAnimation();
   }

   public void setInitialVelocity()
   {
      this.velocity = velocityGenerator.GenerateInitialVelocity();
   }

   public void generateNewVelocityDown()
   {
      this.velocity = this.velocityGenerator.GenerateNewReverseDown(velocity);
   }

   public void generateNewVelocityUp()
   {
      this.velocity = this.velocityGenerator.GenerateNewReverseUp(velocity);
   }

}
