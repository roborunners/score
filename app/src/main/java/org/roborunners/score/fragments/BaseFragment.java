package org.roborunners.score.fragments;

import android.support.v4.app.Fragment;

import butterknife.ButterKnife;


abstract class BaseFragment extends Fragment {
    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
