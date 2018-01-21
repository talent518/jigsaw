package net.yeah.talent518.jigsaw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Date;
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
    SharedPreferences pref;
    GestureDetector mGestureDetector;
    private TextView[][] mBlockViews = new TextView[4][4];
    private Drawable[] mBgs = new Drawable[16];
    private int[][] mInts = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
    };
    private int mMoves = 0;
    private TextView tvMoves;
    private View view;
    private TextView tvMin;
    private int mMin = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int y, x, i = 0;
        for (y = 0; y < 4; y++) {
            for (x = 0; x < 4; x++) {
                mBlockViews[y][x] = (TextView) findViewById(blockResourceIds[y][x]);
                mBgs[i++] = mBlockViews[y][x].getBackground();
            }
        }

        mGestureDetector = new GestureDetector(this, this);
        mGestureDetector.setOnDoubleTapListener(this);

        view = findViewById(R.id.activity_main);
        view.setOnTouchListener(this);

        pref = getSharedPreferences("score", MODE_MULTI_PROCESS);
        mMin = pref.getInt("min", 1000);

        tvMin = (TextView) findViewById(R.id.min);
        tvMin.setText(Integer.toString(mMin));

        tvMoves = (TextView) findViewById(R.id.moves);

        initGame();
    }

    private void initGame() {
        Random random = new Random();
        int y, x, n;

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (n = 1; n <= 16; n++) {
            list.add(n);
        }

        random.setSeed(new Date().getTime());
        for (y = 0; y < 4; y++) {
            for (x = 0; x < 4; x++) {
                mInts[y][x] = list.remove(random.nextInt(list.size()));
                setBlock(x, y);
            }
        }

        mMoves = 0;
        tvMoves.setText("0");
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
        int x, y, n = 1, _x = 3, _y = 3;
        boolean flag = true;

        for_y:
        for (y = 0; y < 4; y++) {
            for (x = 0; x < 4; x++) {
                if (mInts[y][x] == 16) {
                    Log.i(TAG, "posX = " + posX + ", posY = " + posY);
                    if (posX > 0 && lenX > lenY) {
                        if (x > 0) {
                            Log.e(TAG, "onFling-向右滑动-before: " + mIntsToString());

                            swapBlock(x, y, x - 1, y);

                            Log.e(TAG, "onFling-向右滑动-after: " + mIntsToString());

                            flag = false;
                        }
                    } else if (posX < 0 && lenX > lenY) {
                        if (x < 3) {
                            Log.e(TAG, "onFling-向左滑动-before: " + mIntsToString());

                            swapBlock(x, y, x + 1, y);

                            Log.e(TAG, "onFling-向左滑动-after: " + mIntsToString());

                            flag = false;
                        }
                    } else if (posY > 0 && lenY > lenX) {
                        if (y > 0) {
                            Log.e(TAG, "onFling-向下滑动-before: " + mIntsToString());

                            swapBlock(x, y - 1, x, y);

                            Log.e(TAG, "onFling-向下滑动-after: " + mIntsToString());

                            flag = false;
                        }
                    } else if (posY < 0 && lenY > lenX) {
                        if (y < 3) {
                            Log.e(TAG, "onFling-向上滑动-before: " + mIntsToString());

                            swapBlock(x, y + 1, x, y);

                            Log.e(TAG, "onFling-向上滑动-after: " + mIntsToString());

                            flag = false;
                        }
                    }

                    break for_y;
                }
            }
        }

        if(flag) { // 不可移动
            return true;
        }

        mMoves++;
        tvMoves.setText(Integer.toString(mMoves));
        Log.e(TAG, "score = " + mMoves);

        // 可移动
        flag = true;
        n = 1;
        for (y = 0; y < 4; y++) {
            for (x = 0; x < 4; x++) {
                if ((n++) != mInts[y][x]) {
                    flag = false;
                }
            }
        }

        if (flag) {
            Log.v(TAG, "Game Over");
            view.setOnTouchListener(null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle(R.string.game_over);
            builder.setMessage(getString(R.string.your_moves) + mMoves + (mMin > mMoves ? "\n" + getString(R.string.break_a_record) + mMin : ""));
            builder.setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    view.setOnTouchListener(MainActivity.this);
                    initGame();
                }
            });
            builder.setPositiveButton(R.string.end_game, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();

            if (mMoves < mMin) {
                mMin = mMoves;
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt("min", mMin);
                edit.commit();

                tvMin.setText(Integer.toString(mMin));
            }
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

    private void swapBlock(int x1, int y1, int x2, int y2) {
        int i = mInts[y1][x1];
        mInts[y1][x1] = mInts[y2][x2];
        mInts[y2][x2] = i;

        setBlock(x1, y1);
        setBlock(x2, y2);
    }

    private void setBlock(int x, int y) {
        mBlockViews[y][x].setText(mInts[y][x] == 16 ? "" : Integer.toString(mInts[y][x]));
        mBlockViews[y][x].setBackground(mBgs[mInts[y][x] - 1]);
        mBlockViews[y][x].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(mInts[y][x] >= 10 ? R.dimen.text_size_2font : R.dimen.text_size_1font));
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