package edu.sdsmt.brunner_brian.state.states;

import edu.sdsmt.brunner_brian.state.State;

import static android.graphics.Color.WHITE;

public class Won extends State {
    public static final int POSITION = Red.POSITION + 1;

    public Won() {
        super(true);
    }

    @Override
    public int getColor() {
        return WHITE;
    }

    @Override
    public boolean didWin() {
        return true;
    }

    @Override
    public int getRoomPosition() {
        return POSITION;
    }
}
