package org.roborunners.score.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.parceler.Parcels;
import org.roborunners.score.R;
import org.roborunners.score.data.CascadeEffectGameData;
import org.roborunners.score.datatypes.ScoreElement;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class SeasonActivity extends BaseActivity {
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.list) ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_chooser);
        ButterKnife.inject(this);

        // Set toolbar as action bar.
        setSupportActionBar(toolbar);
        // Enable navigation button.
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get list of seasons.
        Resources res = getResources();
        String[] seasons = res.getStringArray(R.array.seasons);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, seasons);
        // Connect adapter to list.
        list.setAdapter(adapter);
    }

    @OnItemClick(R.id.list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(parent.getContext(), ScoreActivity.class);
        Bundle args = new Bundle();

        ArrayList<ScoreElement> scoreElementsAuto = null;
        ArrayList<ScoreElement> scoreElementsTeleop = null;
        ArrayList<ScoreElement> scoreElementsEndgame = null;

        switch (position) {
            case 0:
                return;
            case 1:
                scoreElementsAuto = CascadeEffectGameData.getAuto();
                scoreElementsTeleop = CascadeEffectGameData.getTeleop();
                scoreElementsEndgame = CascadeEffectGameData.getEndgame();
                break;
            case 2:
                return;
        }

        // Pack game data.
        args.putParcelable("scoreElementsAuto", Parcels.wrap(scoreElementsAuto));
        args.putParcelable("scoreElementsTeleop", Parcels.wrap(scoreElementsTeleop));
        args.putParcelable("scoreElementsEndgame", Parcels.wrap(scoreElementsEndgame));

        // Start activity.
        intent.putExtra("gameData", args);
        startActivity(intent);
    }
}