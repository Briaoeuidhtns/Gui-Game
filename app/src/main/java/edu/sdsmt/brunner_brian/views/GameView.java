package edu.sdsmt.brunner_brian.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.LinkedList;
import java.util.Queue;

import edu.sdsmt.brunner_brian.state.State;

import static androidx.core.math.MathUtils.clamp;
import static java.lang.Math.abs;
import static java.lang.Math.copySign;
import static java.lang.Math.min;
import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

public class GameView extends AnimatedView implements Observer<State> {
    private static final String TAG = "GameView";
    private static final long animSpeed = 1;
    final float rate = 1f / 6f;
    final Paint paint = new Paint();
    float currentPosX = .5f;
    RectF player = new RectF(0, 0, 50, 50);

    private Queue<State> toTraverse = new LinkedList<>();
    private @Nullable
    State current;

    private final MutableLiveData<State> stateAnimated = new MutableLiveData<>();

    public LiveData<State> getStateAnimated() {
        return stateAnimated;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        player.bottom = w * .1f;
        player.right = h * .1f;
        Log.v(TAG, "Setting player to " + player.toString());
    }

    // I Don't know which of these is required or which needs to be here, but at the very least it
    // needed the second one
    public GameView(Context context) {
        super(context);
        Log.v(TAG, "GameView created 1");
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG, "GameView created 2");
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.v(TAG, "GameView created 3");
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.v(TAG, "GameView created 4");
    }

    private float minMag(float a, float b) {
        return abs(a) < abs(b) ? a : b;
    }

    @Override
    protected void onAnimate(Canvas c, float delta) {
        if (current == null) {
            current = requireNonNull(toTraverse.remove());

            stateAnimated.postValue(current);

            Log.v(TAG, format("No current, replacing with {0}", current));
        }

        setBackgroundColor(current.getColor());
        paint.setColor(Color.GREEN);
        final @Nullable State target = toTraverse.peek();
        float endPos;
        if (target == null || target.getRoomPosition() == current.getRoomPosition())
            endPos = .5f;
            // target is to the right => going to width
        else endPos = ((target.getRoomPosition() > current.getRoomPosition())) ? 1 : 0;

        float toMove = copySign((delta * rate), endPos - currentPosX);
        // Last move should only move to desired position
        toMove = minMag(toMove, endPos - currentPosX);
        currentPosX = clamp(currentPosX + toMove, 0, 1);

        player.offsetTo((currentPosX * getWidth()) - player.width() / 2, (getHeight() - player.height()) / 2f);
        c.drawRect(player, paint);

        // Should transition rooms?
        if (abs(currentPosX - endPos) < .1 && target != null) {
            Log.i(TAG, "Transitioning rooms");
            // came from the right => start on the left
            currentPosX = (target.getRoomPosition() > current.getRoomPosition()) ? 0 : 1;
            // reset next time
            current = null;
        }
        invalidate();
    }

    @Override
    public void onChanged(State state) {
        toTraverse.add(state);
        Log.v(TAG, format("Current state: {0}, Next state: {1}", current, toTraverse.peek()));
    }
}
