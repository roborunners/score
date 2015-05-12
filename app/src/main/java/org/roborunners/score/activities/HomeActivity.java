package org.roborunners.score.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.roborunners.score.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity {
    @InjectView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        // Set toolbar as action bar.
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about: return true;
            case R.id.action_license: return true;
            case R.id.action_settings: return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.button_new, R.id.button_old})
    public void onTouchClick(Button button) {
        switch (button.getId()) {
            case R.id.button_new:
                startActivity(new Intent(this, SeasonActivity.class));
        }
    }
}
