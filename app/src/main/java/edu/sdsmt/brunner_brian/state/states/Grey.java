package edu.sdsmt.brunner_brian.state.states;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import edu.sdsmt.brunner_brian.state.State;

import static android.graphics.Color.GRAY;

public class Grey extends State {
    public static final int POSITION = 0;
    public Grey() {
        this(false);
    }

    public Grey(boolean blueVisited) {
        super(blueVisited);
    }

    @Override
    public @ColorInt
    int
    getColor() {
        return GRAY;
    }

    @Override
    public int getRoomPosition() {
        return POSITION;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Grey{blueVisited: %s}", getBlueVisited());
    }

    @Override
    public State doGoLeft() {
        return new Blue();
    }

    @Override
    public State doGoRight() {
        return new Red(getBlueVisited());
    }
}
