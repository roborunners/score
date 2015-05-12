package org.roborunners.score.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import org.parceler.Parcels;
import org.roborunners.score.R;
import org.roborunners.score.datatypes.ScoreElement;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Optional;


public class ScoreFragment extends BaseFragment {
    @InjectView(R.id.recycler) RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        ButterKnife.inject(this, view);

        // Get arguments.
        Bundle args = getArguments();
        // Unwrap score elements.
        ArrayList<ScoreElement> scoreElements = Parcels.unwrap(args.getParcelable("scoreElements"));

        // Set up new recycler.
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // Connect recycler to adapter.
        recycler.setAdapter(new ScoreElementAdapter(scoreElements));

        // Return the view.
        return view;
    }

    private class ScoreElementAdapter extends RecyclerView.Adapter<ScoreElementViewHolder> {
        private final ArrayList<ScoreElement> scoreElements;

        public ScoreElementAdapter(ArrayList<ScoreElement> scoreElements) {
            this.scoreElements = new ArrayList<>();
            this.scoreElements.addAll(scoreElements);
        }

        @Override
        public ScoreElementViewHolder onCreateViewHolder(ViewGroup parent, int layout) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new ScoreElementViewHolder(view);
        }

        public void onBindViewHolder(ScoreElementViewHolder viewHolder, int position) {
            viewHolder.label.setText(
                    getResources().getText(scoreElements.get(position).getDescription()));
        }

        @Override
        public int getItemViewType(int position) {
            return this.scoreElements.get(position).getLayout();
        }

        @Override
        public int getItemCount() {
            return scoreElements.size();
        }
    }

    protected class ScoreElementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View view;
        @InjectView(R.id.label) TextView label;
        @Optional @InjectView(R.id.toggle) SwitchCompat toggle;

        public ScoreElementViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.inject(this, view);

            if (toggle != null) {
                toggle.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            Log.w("SENPAI", "NOTICE ME");

            if (view.getId() == R.id.toggle) {
                Log.w("SENPAI", "NOTICED");
            }
        }
    }
}
