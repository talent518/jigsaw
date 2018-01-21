package net.yeah.talent518.jigsaw;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private static final String TAG = "MainActivity";
    private static final int[][] blockResourceIds = {
            {R.id.block_0x0, R.id.block_0x1, R.id.block_0x2, R.id.block_0x3},
            {R.id.block_1x0, R.id.block_1x1, R.id.block_1x2, R.id.block_1x3},
            {R.id.block_2x0, R.id.block_2x1, R.id.block_2x2, R.id.block_2x3},
            {R.id.block_3x0, R.id.block_3x1, R.id.block_3x2, R.id.block_3x3}
    };
    Random rnd = new Random();
    GestureDetector mGestureDetector;
    private TextView[][] mBlockViews = new TextView[4][4];
    private int[][] mBgResourceIds = {
            {R.drawable.bg_block_01, R.drawable.bg_block_02, R.drawable.bg_block_03, R.drawable.bg_block_04},
            {R.drawable.bg_block_05, R.drawable.bg_block_06, R.drawable.bg_block_07, R.drawable.bg_block_08},
            {R.drawable.bg_block_09, R.drawable.bg_block_10, R.drawable.bg_block_11, R.drawable.bg_block_12},
            {R.drawable.bg_block_13, R.drawable.bg_block_14, R.drawable.bg_block_15, R.drawable.bg_block_16}
    };
    private int[][] mInts = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
    };
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int y, x;
        for (y = 0; y < 4; y++) {
            for (x = 0; x < 4; x++) {
                mBlockViews[y][x] = (TextView) findViewById(blockResourceIds[y][x]);
            }
        }

        initGame(false);

        mGestureDetector = new GestureDetector(this, this);
        mGestureDetector.setOnDoubleTapListener(this);

        view = findViewById(R.id.activity_main);
        view.setOnTouchListener(this);
    }

    private void initGame(boolean isInitVariable) {
        int y, x, n = 1;

        if (isInitVariable) {
            for (y = 0; y < 4; y++) {
                for (x = 0; x < 4; x++) {
                    mInts[y][x] = n++;
                }
            }
            mInts[3][3] = 0;
        }
    }

    private void playAnimation(int y, int x) {
        Animation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        mBlockViews[y][x].clearAnimation();
        mBlockViews[y][x].startAnimation(animation);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float posX = e2.getX() - e1.getX();
        float posY = e2.getY() - e1.getY();
        float lenX = Math.abs(posX);
        float lenY = Math.abs(posY);
        int x, y;
        boolean flag;
        boolean isMovable = true;

        Log.i(TAG, "posX = " + posX + ", posY = " + posY);
        if (posX > 0 && lenX > lenY) {
            Log.e(TAG, "onFling-" + "向右滑动: " + mIntsToString());

        } else if (posX < 0 && lenX > lenY) {
            Log.e(TAG, "onFling-" + "向左滑动: " + mIntsToString());
        } else if (posY > 0 && lenY > lenX) {
            Log.e(TAG, "onFling-" + "向下滑动: " + mIntsToString());
        } else if (posY < 0 && lenY > lenX) {
            Log.e(TAG, "onFling-" + "向上滑动: " + mIntsToString());
        } else {
            isMovable = false;
        }

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    private String mIntsToString() {
        StringBuffer sb = new StringBuffer();
        int y, x;
        for (y = 0; y < 4; y++) {
            sb.append("\t[");
            for (x = 0; x < 4; x++) {
                if (x > 0) {
                    sb.append(", ");
                }
                sb.append(mInts[y][x]);
            }
            sb.append("]" + (y == 3 ? "" : ",") + "\n");
        }
        return "[\n" + sb.toString() + "]";
    }
}