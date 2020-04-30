package edu.sdsmt.brunner_brian.state;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.MessageFormat;

import edu.sdsmt.brunner_brian.ThreadedTimer;
import edu.sdsmt.brunner_brian.state.states.Grey;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

public class StateMachine extends ViewModel implements Actions {
    private static final String TAG = "StateMachine";
    private final MutableLiveData<State> state;

    public final MutableLiveData<State> getState() {
        return state;
    }

    private final ThreadedTimer timer;

    public ThreadedTimer getTimer() {
        return timer;
    }

    @NonNull
    @Override
    public String toString() {
        return MessageFormat.format("StateMachine'{'state={0}, timer={1}'}'", state.getValue(), timer.getValue());
    }

    public StateMachine() {
        this.state = new MutableLiveData<>();
        this.timer = new ThreadedTimer(1, SECONDS);
        this.state.setValue(new Grey());
        Log.v(TAG, "initialized");
    }

    /**
     * Call the action on current state and update state to returned state.
     * Same interface to ensure nothing missed
     * On invalid call nothing changes, does not throw, returns null
     */
    @Override
    public State doGoLeft() {
        Log.i(TAG, "doGoLeft called");
        try {
            State newState = requireNonNull(this.state.getValue()).doGoLeft();
            state.setValue(newState);
            return newState;
        } catch (InvalidTaskException e) {
            Log.i(TAG, "Invalid action, skipping");
            return null;
        }
    }

    /**
     * Call the action on current state and update state to returned state.
     * Same interface to ensure nothing missed
     * On invalid call nothing changes, does not throw, returns null
     */
    @Override
    public State doGoRight() {
        Log.i(TAG, "doGoRight called");
        try {
            State newState = requireNonNull(this.state.getValue()).doGoRight();
            state.setValue(newState);
            return newState;
        } catch (InvalidTaskException e) {
            Log.i(TAG, "Invalid action, skipping");
            return null;
        }
    }
}
