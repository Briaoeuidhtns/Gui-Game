package edu.sdsmt.brunner_brian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import edu.sdsmt.brunner_brian.state.PlayerShape;
import edu.sdsmt.brunner_brian.state.StateMachine;
import edu.sdsmt.brunner_brian.views.EndDialog;
import edu.sdsmt.brunner_brian.views.FabManager;
import edu.sdsmt.brunner_brian.views.GameView;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Main activity created");

        setContentView(R.layout.activity_main);

        final StateMachine stateMachine = new ViewModelProvider(this).get(StateMachine.class);
        Log.v(TAG, stateMachine.toString());
        final GameView gameView = this.findViewById(R.id.gameView);

        stateMachine.getState().observe(this, gameView);
        stateMachine.getState().observe(this, state -> {
        });
        TextView score = this.findViewById(R.id.liveScore);
        stateMachine.getTimer().observe(this, time->{score.setText(String.format(Locale.getDefault(), "%d", time));});

        gameView.getStateAnimated().observe(this, state -> {
            final String msg = state.getEntryMessage();
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }

            if (state.didWin()) {
                Log.i(TAG, "Game won, showing dialog");
                final DialogFragment dialog = new EndDialog();
                Bundle args = new Bundle();
                args.putLong(EndDialog.SCORE_ARG, stateMachine.getTimer().getValue());
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), null);
                stateMachine.getTimer().cancel();
            }
        });

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent __, MotionEvent ___, float velocityX, float velocityY) {
                Log.i(TAG, String.format("Fling detected, %s, %s", velocityX, velocityY));
                // Horizontal fling only
                if (abs(velocityX) > abs(velocityY)) {
                    if (velocityX < 0)
                        stateMachine.doGoLeft();
                    else stateMachine.doGoRight();
                    return true;
                }
                return false;
            }

            /**
             * I have no idea why this is needed, but magically makes it work, soooo
             * https://stackoverflow.com/a/938657/2384326
             */
            @Override
            public boolean onDown(MotionEvent __) {
                return true;
            }
        });
        gameView.setOnTouchListener((__, e) -> gestureDetector.onTouchEvent(e));

        final FabManager fabManager = new FabManager(this, findViewById(R.id.openPlayers), findViewById(R.id.playerRound), findViewById(R.id.playerSquare));

        Transformations.map(fabManager.getSelected(), PlayerShape::factory).observe(this, gameView::setPlayerShape);
    }
}
