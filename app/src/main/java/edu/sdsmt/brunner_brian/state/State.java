package edu.sdsmt.brunner_brian.state;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public abstract class State implements Actions {
    private final boolean blueVisited;

    public State(boolean blueVisited) {
        this.blueVisited = blueVisited;
    }

    public abstract @ColorInt
    int
    getColor();

    /**
     * Get the position of this room state
     *
     * @return relative room position
     */
    public abstract int getRoomPosition();

    @Override
    public State doGoLeft() {
        throw new InvalidTaskException(this, "doGoLeft");
    }

    @Override
    public State doGoRight() {
        throw new InvalidTaskException(this, "doGoRight");
    }

    public final boolean getBlueVisited() {
        return blueVisited;
    }

    public @Nullable String getEntryMessage() {
        return null;
    }

    public boolean didWin() {
        return false;
    }
}
