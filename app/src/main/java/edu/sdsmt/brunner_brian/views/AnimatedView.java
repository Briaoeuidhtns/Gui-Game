package edu.sdsmt.brunner_brian.views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import edu.sdsmt.brunner_brian.BuildConfig;

import static java.text.MessageFormat.format;

/**
 * Handle deltas for animation
 */
public abstract class AnimatedView extends View {
    private static final String TAG = "AnimatedView";
    private long lastDraw = -1;

    public AnimatedView(Context context) {
        super(context);
    }

    public AnimatedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimatedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (lastDraw < 0) {
            Log.v(TAG, "Initializing last draw time");
            lastDraw = SystemClock.uptimeMillis();
        }
        long now = SystemClock.uptimeMillis();
        float delta = (now - lastDraw) / 1000f;
        lastDraw = now;
        if (BuildConfig.DEBUG && delta > .5f) {
            Log.e(TAG, format("In debug mode, clamping long animation time from {0} to 0.5", delta));
            delta = .5f;
        }

        onAnimate(c, delta);
    }

    abstract protected void onAnimate(Canvas c, float delta);
}
