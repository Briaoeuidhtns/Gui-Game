package edu.sdsmt.brunner_brian.state.states;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.sdsmt.brunner_brian.state.State;

import static android.graphics.Color.BLUE;

public class Blue extends State {
    public static final int POSITION = Grey.POSITION - 1;

    Blue() {
        super(true);
    }

    @Override
    public @ColorInt
    int
    getColor() {
        return BLUE;
    }

    @Override
    public int getRoomPosition() {
        return POSITION;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Blue{blueVisited: %s}", getBlueVisited());
    }

    @Override
    public State doGoRight() {
        return new Grey(getBlueVisited());
    }

    @Override
    public String getEntryMessage() {
        return "Blue room entered";
    }
}
