package edu.sdsmt.brunner_brian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import edu.sdsmt.brunner_brian.state.StateMachine;
import edu.sdsmt.brunner_brian.views.EndDialog;
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
        final GameView gameView = this.<GameView>findViewById(R.id.gameView);

        stateMachine.getState().observe(this, gameView);
        stateMachine.getState().observe(this, state -> {
        });

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
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
        gameView.setOnTouchListener((__, e) -> gestureDetector.onTouchEvent(e));

    }
}
