package edu.sdsmt.brunner_brian.views;

import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;


import static java.util.Objects.requireNonNull;

public class FabManager implements Button.OnClickListener {
    private final FabManagerState state;

    public static class FabManagerState extends ViewModel {
        private MutableLiveData<Integer> selected = new MutableLiveData<>();

        public LiveData<Integer> getSelected() {
            return selected;
        }

        private MutableLiveData<Boolean> open = new MutableLiveData<>(false);

        public LiveData<Boolean> getOpen() {
            return open;
        }
    }

    public LiveData<Integer> getSelected() {
        return state.getSelected();
    }


    public <O extends ViewModelStoreOwner & LifecycleOwner> FabManager(O owner, Button toggle, Button... others) {
        state = new ViewModelProvider(owner).get(FabManagerState.class);
        state.getOpen().observe(owner, open -> {
            for (Button b : others) {
                b.setVisibility(open ? View.VISIBLE : View.GONE);
            }
        });
        for (Button b : others)
            b.setOnClickListener(e -> state.selected.setValue(e.getId()));

        toggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        state.open.setValue(!requireNonNull(state.open.getValue()));
    }
}
