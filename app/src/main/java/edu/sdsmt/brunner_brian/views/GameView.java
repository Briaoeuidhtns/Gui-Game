package edu.sdsmt.brunner_brian.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.Queue;

import edu.sdsmt.brunner_brian.state.PlayerShape;
import edu.sdsmt.brunner_brian.state.State;

import static androidx.core.math.MathUtils.clamp;
import static java.lang.Math.abs;
import static java.lang.Math.copySign;
import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

public class GameView extends AnimatedView implements Observer<State> {
    private static final String TAG = "GameView";
    final float rate = 1f / 2f;
    final Paint paint = new Paint();
    float currentPosX = .5f;
    private PlayerShape player = PlayerShape.SQUARE;

    private final Queue<State> toTraverse = new LinkedList<>();
    private @Nullable
    State current;

    private final MutableLiveData<State> stateAnimated = new MutableLiveData<>();

    public LiveData<State> getStateAnimated() {
        return stateAnimated;
    }

    // I Don't know which of these is required or which needs to be here, but at the very least it
    // needed the second one
    public GameView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        Log.v(TAG, "GameView created 1");
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
        Log.v(TAG, "GameView created 2");
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setColor(Color.BLACK);
        Log.v(TAG, "GameView created 3");
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint.setColor(Color.BLACK);
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

        // GRADING: ROOM
        setBackgroundColor(current.getColor());
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

        float radius = .1f * getWidth(),
                x = (currentPosX * getWidth()),
                y = getHeight() / 2f;

        switch (player) {
            case SQUARE:
                c.drawRect(x - radius, y - radius, x + radius, y + radius, paint);
                break;
            case ROUND:
                c.drawCircle(x, y, radius, paint);
                break;
        }

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

    private static final String TO_TRAVERSE = "GameView.toTraverse";
    private static final String LOCATION = "GameView.location";
    private static final String SUPER = "GameView.super";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloat(LOCATION, currentPosX);
        bundle.putParcelable(SUPER, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            currentPosX = bundle.getFloat(LOCATION);
            state = bundle.getParcelable(SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onChanged(State state) {
        // GRADING: TRANSITION
        toTraverse.add(state);
        Log.v(TAG, format("Current state: {0}, Next state: {1}", current, toTraverse.peek()));
    }

    public void setPlayerShape(PlayerShape shape) {
        this.player = shape;
    }
}
