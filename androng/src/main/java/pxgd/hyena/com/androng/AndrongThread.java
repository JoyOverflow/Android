package pxgd.hyena.com.androng;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.view.SurfaceHolder;
import android.view.View;

class AndrongThread extends Thread{

    private final SurfaceHolder surfaceHolder;
    private Handler handler;
    private boolean isRunning;

    private Bitmap backgroundImage;
    private Ball ball;
    private Sprite battBottom;
    private Sprite battTop;

    private int canvasWidth;
    private int canvasHeight;

    private double delayTime = 0;
    public int currentState;

    public static final int STATE_PAUSE = 2;
    public static final int STATE_RUNNING_1P = 4;
    public static final int STATE_RUNNING_2P = 5;
    public static final int STATE_RUNNING_0P = 6;
    private Score score;
    private boolean shouldDiagnosticInformation = false;
    private double frameTime;
    private int previousState;

    public AndrongThread(SurfaceHolder surfaceHolder, Context context, Handler handler)
    {
        this.surfaceHolder = surfaceHolder;
        this.handler = handler;

        Resources resources = context.getResources();
        DrawableResourceCollection drawableCollection = CreateBallCollection(resources);

        ball = new Ball(drawableCollection, canvasWidth, canvasHeight, new VelocityGenerator());
        ball.setInitialVelocity();
        ball.center();

        DrawableResourceCollection battCollection = new DrawableResourceCollection();
        battCollection.add(resources.getDrawable(R.drawable.bat));

        battBottom = new Sprite(battCollection, canvasWidth, canvasHeight, 0, false);
        battTop = new Sprite(battCollection, canvasWidth, canvasHeight, 0, false);
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background2);
        SetInitialBattPosition();

        score = new Score(10);

        SoundManager.getInstance();
        SoundManager.initSounds(context);
        SoundManager.loadSounds();
    }

    private DrawableResourceCollection CreateBallCollection(Resources resources)
    {

        DrawableResourceCollection drawableCollection = new DrawableResourceCollection();
        /*drawableCollection.add(resources.getDrawable(R.drawable.ball1));
        drawableCollection.add(resources.getDrawable(R.drawable.ball2));
        drawableCollection.add(resources.getDrawable(R.drawable.ball3));
        drawableCollection.add(resources.getDrawable(R.drawable.ball4));
        drawableCollection.add(resources.getDrawable(R.drawable.ball5));
        drawableCollection.add(resources.getDrawable(R.drawable.ball6));
        drawableCollection.add(resources.getDrawable(R.drawable.ball7));
        drawableCollection.add(resources.getDrawable(R.drawable.ball8));
        drawableCollection.add(resources.getDrawable(R.drawable.ball9));
        drawableCollection.add(resources.getDrawable(R.drawable.ball10));
        drawableCollection.add(resources.getDrawable(R.drawable.ball11));
        drawableCollection.add(resources.getDrawable(R.drawable.ball12));
        */
        //Drawable t = ContextCompat.getDrawable(getContext(),R.drawable.ball1);
        //Drawable t=ResourcesCompat.getDrawable(resources, R.drawable.ball1, null);


        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball1, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball2, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball3, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball4, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball5, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball6, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball7, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball8, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball9, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball10, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball11, null));
        drawableCollection.add(ResourcesCompat.getDrawable(resources, R.drawable.ball12, null));

        return drawableCollection;
    }

    private void SetInitialBattPosition()
    {
        battBottom.centerHorizontal();
        battBottom.setyPosition(canvasHeight - (canvasHeight / 100 * 20));
        battTop.centerHorizontal();
        battTop.setyPosition((canvasHeight / 100) * 5);
    }

    @Override
    public void run()
    {
        long startTime = SystemClock.uptimeMillis();
        while (isRunning)
        {
            Canvas canvas = null;
            try
            {
                long currentTime = SystemClock.uptimeMillis();
                frameTime = (currentTime - startTime) / 1000.0;
                startTime = currentTime;

                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder)
                {
                    if ((currentState == STATE_RUNNING_1P || currentState == STATE_RUNNING_0P ||
                            currentState == STATE_RUNNING_2P) && delayTime <= 0)
                    {
                        CheckCollision();
                        CheckBallBounds();
                        AdjustBatts();
                        AdvanceBall(frameTime);
                    }

                    if (delayTime >= 0)
                        delayTime = delayTime - frameTime;

                    DrawScoreBoard();
                    doDraw(canvas);
                }
            }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void DrawScoreBoard()
    {
        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        String scoreBoard = score.CreateScoreBoard();
        if (shouldDiagnosticInformation)
            scoreBoard += " FPS: " + (int) (1 / frameTime);
        b.putString("text", scoreBoard);
        b.putInt("viz", View.VISIBLE);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    private void AddToastToQueue(String messageToShow)
    {
        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("toast", messageToShow);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    private void AdjustBatts()
    {
        if (currentState == STATE_RUNNING_0P || currentState == STATE_RUNNING_1P)
        {
            AdjustBattPosition(battTop);
        }

        if (currentState == STATE_RUNNING_0P)
        {
            AdjustBattPosition(battBottom);
        }
    }

    private void AdjustBattPosition(Sprite batt)
    {
        if (batt.getxPosition() < ball.getxPosition())
            batt.setVelocity(new Velocity(100, 0));
        if (batt.getxPosition() >= ball.getxPosition())
            batt.setVelocity(new Velocity(-130, 0));
    }

    private void doDraw(Canvas canvas)
    {
        canvas.drawBitmap(backgroundImage, 0, 0, null);
        ball.draw(canvas);
        battBottom.draw(canvas);
        battTop.draw(canvas);
    }

    private void CheckBallBounds()
    {
        if (ball.IsOutOfXBounds(frameTime))
        {
            ball.reverseXVelocity();
            SoundManager.playSound(1, 2);
        }

        if (ball.IsOutOfLowerBounds(frameTime))
        {
            score.Player2Scored();
            if (score.isGameFinished())
            {
                FinishGame();
            }
            else
            {
                Scored(new Velocity(133, -93));
            }
        }

        if (ball.IsOutOfUpperBounds(frameTime))
        {
            score.Player1Scored();
            if (score.isGameFinished())
            {
                FinishGame();
            }
            else
            {
                Scored(new Velocity(133, 93));
            }
        }
    }

    private void Scored(Velocity newBallVelocity)
    {
        ball.center();
        ball.setVelocity(newBallVelocity);
        SoundManager.playSound(2, 1);
        delayTime = 2;
    }

    private void FinishGame()
    {
        SoundManager.playSound(3, 1);
        setState(STATE_PAUSE);
        AddToastToQueue(score.CreateWinnerBoard());
        AddToastToQueue("Select Menu for a new game.");
        ResetGame();
    }

    private void AdvanceBall(double frameTime)
    {
        ball.Move(frameTime);
        battTop.Move(frameTime);
        battBottom.Move(frameTime);
    }

    private void CheckCollision()
    {
        if (battBottom.CollidesWith(ball, frameTime))
        {
            ball.setPreviousLocation(frameTime);
            ball.generateNewVelocityUp();
            battBottom.setPreviousLocation(frameTime);
            SoundManager.playSound(1, 1);
        }
        else if (battTop.CollidesWith(ball, frameTime))
        {
            ball.setPreviousLocation(frameTime);
            ball.generateNewVelocityDown();
            battTop.setPreviousLocation(frameTime);
            SoundManager.playSound(1, 1);
        }
    }

    public void setSurfaceSize(int width, int height)
    {
        synchronized (surfaceHolder)
        {
            canvasWidth = width;
            canvasHeight = height;

            backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, true);
            ball.canvasChanges(width, height);
            battBottom.canvasChanges(width, height);
            battTop.canvasChanges(width, height);
            ball.center();
            SetInitialBattPosition();
        }
    }

    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }

    public void setState(int state)
    {
        this.currentState = state;
    }

    public void doStart0p()
    {
        synchronized (surfaceHolder)
        {
            previousState = STATE_RUNNING_0P;
            setState(STATE_RUNNING_0P);
            ResetGame();
        }
    }

    private void ResetGame()
    {
        ball.center();
        battTop.centerHorizontal();
        battBottom.centerHorizontal();
        setRunning(true);
        ResetVelocities();
        score.Reset();
        delayTime = 2;
    }

    private void ResetVelocities()
    {
        battBottom.setVelocity(new Velocity(0, 0));
        battTop.setVelocity(new Velocity(0, 0));
    }

    public void doStart1p()
    {
        synchronized (surfaceHolder)
        {
            previousState = STATE_RUNNING_1P;
            setState(STATE_RUNNING_1P);
            ResetGame();
        }
    }

    public void doStart2p()
    {
        synchronized (surfaceHolder)
        {
            previousState = STATE_RUNNING_2P;
            setState(STATE_RUNNING_2P);
            ResetGame();
        }
    }

    public void pause()
    {
        synchronized (surfaceHolder)
        {
            setState(STATE_PAUSE);
        }
    }

    public void unpause()
    {
        synchronized (surfaceHolder)
        {
            if (currentState == STATE_PAUSE)
            {
                setState(previousState);
            }
        }
    }

    public void toggleDiagnosticInformation()
    {
        shouldDiagnosticInformation = !shouldDiagnosticInformation;
        resumeGame();
    }

    public void setBattPosition(float xPosition1, float yPosition1, float xPosition2, float yPosition2)
    {
        if (currentState == STATE_RUNNING_1P || currentState == STATE_RUNNING_2P)
        {
            if (currentState == STATE_RUNNING_1P)
            {
                int battXPos = (int) xPosition1;
                if (battXPos <= 0)
                    battXPos = 0;

                battBottom.setxPosition(battXPos);
                if (battBottom.IsMaximumRight(canvasWidth))
                    battBottom.SetMaximumRight(canvasWidth);
            }
            else if (currentState == STATE_RUNNING_2P)
            {
                if (yPosition1 < yPosition2)
                {
                    SetBattsPosition((int) xPosition1, (int) xPosition2);
                }
                else
                {
                    SetBattsPosition((int) xPosition2, (int) xPosition1);
                }
            }
        }
    }

    private void SetBattsPosition(int xPosition1, int xPosition2)
    {
        battTop.setxPosition((int) xPosition1);
        battBottom.setxPosition((int) xPosition2);
        if (battTop.IsMaximumRight(canvasWidth))
            battTop.SetMaximumRight(canvasWidth);
        if (battBottom.IsMaximumRight(canvasWidth))
            battBottom.SetMaximumRight(canvasWidth);
    }

    public void toggleSound()
    {
        SoundManager.togglePlaySound();
        resumeGame();
    }

    public void resumeGame()
    {
        setState(previousState);
    }




}
