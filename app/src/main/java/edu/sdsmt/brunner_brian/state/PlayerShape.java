package edu.sdsmt.brunner_brian.state;

import android.util.SparseArray;

import androidx.annotation.Nullable;

import edu.sdsmt.brunner_brian.R;

public enum PlayerShape {
    SQUARE(R.id.playerSquare), ROUND(R.id.playerRound);

    public final int id;

    private static final SparseArray<PlayerShape> LOOKUP = new SparseArray<>();

    static {
        for (PlayerShape s : values())
            LOOKUP.append(s.id, s);
    }

    PlayerShape(int id) {
        this.id = id;
    }

    @Nullable
    public static PlayerShape factory(int id) {
        return LOOKUP.get(id);
    }
}
