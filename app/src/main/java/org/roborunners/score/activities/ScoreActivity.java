package org.roborunners.score.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.parceler.Parcels;
import org.roborunners.score.R;
import org.roborunners.score.datatypes.ScoreElement;
import org.roborunners.score.fragments.ScoreFragment;
import org.roborunners.score.widgets.SlidingTabStrip;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ScoreActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private final int[] titles = {
            R.string.title_scoring_auto,
            R.string.title_scoring_teleop,
            R.string.title_scoring_endgame,
    };
    private final int[] colors = {
            R.color.background_auto,
            R.color.background_teleop,
            R.color.background_endgame,
    };

    private ArrayList<ScoreElement> scoreElementsAuto = null;
    private ArrayList<ScoreElement> scoreElementsTeleop = null;
    private ArrayList<ScoreElement> scoreElementsEndgame = null;

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.pager) ViewPager pager;
    @InjectView(R.id.tabs) SlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_cascade_effect);
        ButterKnife.inject(this);

        // Get arguments.
        Bundle args = getIntent().getExtras();
        // Unwrap game data.
        Bundle gameData = args.getBundle("gameData");
        scoreElementsAuto = Parcels.unwrap(gameData.getParcelable("scoreElementsAuto"));
        scoreElementsTeleop = Parcels.unwrap(gameData.getParcelable("scoreElementsTeleop"));
        scoreElementsEndgame = Parcels.unwrap(gameData.getParcelable("scoreElementsEndgame"));

        // Set toolbar as action bar.
        setSupportActionBar(toolbar);
        // Enable navigation button.
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load pages into pager.
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        // Connect the tab bar to the pager.
        tabs.setViewPager(pager);
        // Connect the page change listener to the tab bar.
        tabs.setOnPageChangeListener(this);

        // Set the initial colors.
        int position = pager.getCurrentItem();
        pager.getRootView().setBackgroundColor(getResources().getColor(colors[position]));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_score, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save: return true;
            case R.id.action_reset: return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onPageScrollStateChanged(int state) {
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        View root = pager.getRootView();

        // Fade background when we switch pages.
        ObjectAnimator bgFade = ObjectAnimator.ofObject(root, "backgroundColor",
                new ArgbEvaluator(),
                ((ColorDrawable) root.getBackground()).getColor(),
                getResources().getColor(colors[position]));
        bgFade.setDuration(getResources().getInteger(R.integer.duration_score_mix));
        bgFade.start();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() { return 3; }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ScoreFragment();
            Bundle args = new Bundle();

            switch (position) {
                case 0:
                    args.putParcelable("scoreElements", Parcels.wrap(scoreElementsAuto)); break;
                case 1:
                    args.putParcelable("scoreElements", Parcels.wrap(scoreElementsTeleop)); break;
                case 2:
                    args.putParcelable("scoreElements", Parcels.wrap(scoreElementsEndgame)); break;
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titles[position]).toUpperCase();
        }
    }
}