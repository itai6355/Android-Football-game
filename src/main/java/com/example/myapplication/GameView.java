package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {



    private final Bitmap playerImage;
    private Bitmap ballImage;
    private Bitmap fieldImage;
    private Bitmap trophyImage;
    private float playerX, playerY;
    private float targetX, targetY;
    private static final float MOVEMENT_SPEED = 10f;
    private boolean isBallAttached = false;
    private float ballX, ballY;
    private float ballSpeedX, ballSpeedY;
    private boolean gameWon = false;
    private long touchDownTime = 0;
    private static final long LONG_PRESS_THRESHOLD = 1000;

    public GameView(Context context) {
        super(context);

        playerImage = BitmapFactory.decodeResource(getResources(), R.drawable.walk);

        targetX = playerX;
        targetY = playerY;

        fieldImage = BitmapFactory.decodeResource(getResources(), R.drawable.img);

        trophyImage = BitmapFactory.decodeResource(getResources(), R.drawable.win);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameWon) {
                    updatePlayerPosition();
                    updateBallPosition();
                }
                invalidate();
                handler.postDelayed(this, 16); 
            }
        }, 16);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        ballImage = BitmapFactory.decodeResource(getResources(), R.drawable.football);

        int ballWidth = 100;
        int ballHeight = 100;
        ballImage = Bitmap.createScaledBitmap(ballImage, ballWidth, ballHeight, false);

        playerX = w / 2 - playerImage.getWidth() / 2;
        playerY = h / 2 - playerImage.getHeight() / 2;

        ballX = w / 2 - ballImage.getWidth() / 2;
        ballY = h / 2 - ballImage.getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect fieldRect = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(fieldImage, null, fieldRect, new Paint());

        if (gameWon) {
            Rect trophyRect = new Rect(getWidth() / 2 - trophyImage.getWidth() / 2, getHeight() / 4 - trophyImage.getHeight() / 2,
                    getWidth() / 2 + trophyImage.getWidth() / 2, getHeight() / 4 + trophyImage.getHeight() / 2);
            canvas.drawBitmap(trophyImage, null, trophyRect, new Paint());

            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(0xFFFFFFFF);
            canvas.drawText("You Win!", getWidth() / 2 - 250, getHeight() / 2, paint);
            return;
        }

        Rect playerRect = new Rect((int) playerX, (int) playerY, (int) playerX + playerImage.getWidth(), (int) playerY + playerImage.getHeight());
        canvas.drawBitmap(playerImage, null, playerRect, new Paint());

        Rect ballRect = new Rect((int) ballX, (int) ballY, (int) ballX + ballImage.getWidth(), (int) ballY + ballImage.getHeight());
        canvas.drawBitmap(ballImage, null, ballRect, new Paint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (isBallTouched(touchX, touchY)) {
            isBallAttached = true;
            touchDownTime = System.currentTimeMillis();
        } else {
            targetX = touchX - (float) playerImage.getWidth() / 2;
            targetY = touchY - (float) playerImage.getHeight() / 2;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_UP:
                long touchDuration = System.currentTimeMillis() - touchDownTime;
                if (touchDuration >= LONG_PRESS_THRESHOLD) {
                    shootBallUpward();
                }
                break;
        }

        return true;
    }

    private void updatePlayerPosition() {
        if (playerX < targetX) {
            playerX += MOVEMENT_SPEED;
        } else if (playerX > targetX) {
            playerX -= MOVEMENT_SPEED;
        }

        if (playerY < targetY) {
            playerY += MOVEMENT_SPEED;
        } else if (playerY > targetY) {
            playerY -= MOVEMENT_SPEED;
        }

        if (Math.abs(playerX - targetX) < MOVEMENT_SPEED) {
            playerX = targetX;
        }
        if (Math.abs(playerY - targetY) < MOVEMENT_SPEED) {
            playerY = targetY;
        }

        if (isBallAttached) {
            ballX = playerX + playerImage.getWidth();
            ballY = playerY + playerImage.getHeight() / 2 - ballImage.getHeight() / 2;
        }
    }

    private void updateBallPosition() {
        if (!isBallAttached) {
            ballX += ballSpeedX;
            ballY += ballSpeedY;

            if (ballX < 0) ballX = 0;
            if (ballY < 0) ballY = 0;
            if (ballX + ballImage.getWidth() > getWidth()) ballX = getWidth() - ballImage.getWidth();
            if (ballY + ballImage.getHeight() > getHeight()) ballY = getHeight() - ballImage.getHeight();
        }

        if (ballY <= 0 && !gameWon) {
            gameWon = true;
        }
    }

    private boolean isBallTouched(float touchX, float touchY) {
        Rect ballRect = new Rect((int) ballX, (int) ballY, (int) ballX + ballImage.getWidth(), (int) ballY + ballImage.getHeight());
        return ballRect.contains((int) touchX, (int) touchY);
    }

    private void shootBallUpward() {
        if (isBallAttached) {

            ballSpeedX = 0;
            ballSpeedY = -15;

            isBallAttached = false;
        }
    }
}
