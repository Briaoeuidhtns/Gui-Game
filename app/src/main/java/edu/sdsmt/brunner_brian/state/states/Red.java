package edu.sdsmt.brunner_brian.state.states;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import edu.sdsmt.brunner_brian.state.InvalidTaskException;
import edu.sdsmt.brunner_brian.state.State;

import static android.graphics.Color.RED;

public class Red extends State {
    public static final int POSITION = Grey.POSITION + 1;
    Red(boolean blueVisited) {
        super(blueVisited);
    }

    @Override
    public @ColorInt
    int
    getColor() {
        return RED;
    }

    @Override
    public int getRoomPosition() {
        return 1;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Red{blueVisited: %s}", getBlueVisited());
    }

    // GRADING: TRANSITION
    @Override
    public State doGoLeft() {
                return new Grey(getBlueVisited());
    }

    @Override
    public State doGoRight() {
        if (getBlueVisited())
            return new Won();
        // GRADING: GUARD
        throw new InvalidTaskException(this, "doGoRight");
    }
}
