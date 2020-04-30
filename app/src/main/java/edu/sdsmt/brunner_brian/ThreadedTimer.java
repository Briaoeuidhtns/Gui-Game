package edu.sdsmt.brunner_brian;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * An observable timer. Only changes when there is an active observer and it is not canceled
 * GRADING: EXTENSION 2c
 */
public class ThreadedTimer extends LiveData<Long> {
    private static final String TAG = "ThreadedTimer";
    private final ScheduledThreadPoolExecutor exe = new ScheduledThreadPoolExecutor(1);
    private final long period;
    private final TimeUnit unit;

    private ScheduledFuture<?> future;
    private boolean canceled = false;

    public ThreadedTimer(long rate, TimeUnit unit) {
        super(0L);
        Log.v(TAG, "Timer created");
        this.period = rate;
        this.unit = unit;
    }

    @NonNull
    @Override
    public Long getValue() {
        // Always set, so can't be null. Just to fix warnings
        return requireNonNull(super.getValue());
    }

    public void start() {
        Log.v(TAG, "Timer started");
        canceled = false;
        if (future == null && hasActiveObservers())
            future = exe.scheduleAtFixedRate(() ->
                            // initialized so never null
                            postValue(requireNonNull(getValue()) + 1),
                    period, period, unit);
    }

    private boolean stop() {
        Log.v(TAG, "Timer stopped");
        boolean success = false;
        if (future != null) {
            success = future.cancel(true);
        }
        future = null;
        return success;
    }

    /**
     * Cancel the running timer. Will not resume until {@link #start() start} is called
     *
     * @return true if successfully cancelled
     */
    public boolean cancel() {
        canceled = true;
        return stop();
    }

    @Override
    protected void onActive() {
        if (!canceled)
            start();
    }

    @Override
    protected void onInactive() {
        stop();
    }

    /**
     * Reset the timer to 0 and restart
     */
    public void reset() {
        // restart so next update is a whole second
        stop();
        setValue(0L);
        start();
    }
}
