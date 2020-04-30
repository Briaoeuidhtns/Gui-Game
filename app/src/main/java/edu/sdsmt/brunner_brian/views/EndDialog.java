package edu.sdsmt.brunner_brian.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.sdsmt.brunner_brian.R;

import static java.text.MessageFormat.format;
import static java.util.Objects.*;

public class EndDialog extends DialogFragment {
    public static final String SCORE_ARG = "EndDialog.score";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        final TextView end = view.findViewById(R.id.scoreField);
        final long score = requireNonNull(getArguments()).getLong(SCORE_ARG);
        end.setText(format("Score: {0}", score));

        return view;
    }
}
