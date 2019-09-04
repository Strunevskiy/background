package com.aleh.struneuski.background.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alexandrepiveteau.library.tutorial.TutorialActivity;
import com.alexandrepiveteau.library.tutorial.TutorialFragment;
import com.aleh.struneuski.background.R;
import com.aleh.struneuski.background.utils.ImageUtils;

public class IntroActivity extends TutorialActivity {

    private int[] BACKGROUND_COLORS = {Color.parseColor("#303030"), Color.parseColor("#0D47A1"), Color.parseColor("#F44336"), Color.parseColor("#2196F3")};

    private int currentPage;

    @Override
    public String getIgnoreText() {
        return "Skip";
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getBackgroundColor(int position) {
        return BACKGROUND_COLORS[position];
    }

    @Override
    public int getNavigationBarColor(int position) {
        return ImageUtils.darkColor(BACKGROUND_COLORS[position]);
    }

    @Override
    public int getStatusBarColor(int position) {
        return ImageUtils.darkColor(BACKGROUND_COLORS[position]);
    }

    @Override
    public Fragment getTutorialFragmentFor(int position) {
        currentPage = position;
        switch (position) {
            case 0:
                return new TutorialFragment.Builder()
                        .setTitle(getResources().getString(R.string.app_name))
                        .setDescription(getResources().getString(R.string.app_des))
                        .setImageResource(R.mipmap.splash)
                        .build();

            case 1:
                return new TutorialFragment.Builder()
                        .setTitle(getResources().getString(R.string.playlist_mode))
                        .setDescription(getResources().getString(R.string.playlists_des))
                        .setImageResource(R.drawable.playlist_offline)
                        .build();
            case 2:
                return new TutorialFragment.Builder()
                        .setTitle(getResources().getString(R.string.search_mode))
                        .setDescription(getResources().getString(R.string.search_des))
                        .setImageResource(R.drawable.playlist_search)
                        .build();
            case 3:
                return new TutorialFragment.Builder()
                        .setTitle(getResources().getString(R.string.feedback_mode))
                        .setDescription(getResources().getString(R.string.feedback_des))
                        .setImageResourceForeground(R.drawable.rate)
                        .setImageResourceBackground(R.drawable.rate_bg)
                        .build();
            default:
                return new TutorialFragment.Builder().build();
        }
    }

    @Override
    public boolean isNavigationBarColored() {
        return true;
    }

    @Override
    public boolean isStatusBarColored() {
        return true;
    }

    @Override
    public ViewPager.PageTransformer getPageTransformer() {
        return TutorialFragment.getParallaxPageTransformer(2.5f);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        currentPage = position;
    }

    public void onClick(View v) {
        if ((currentPage == 0 && v.getId() == com.alexandrepiveteau.library.tutorial.R.id.tutorial_button_left) ||
                (currentPage == 3 && v.getId() == com.alexandrepiveteau.library.tutorial.R.id.tutorial_button_image_right)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            super.onClick(v);
        }
    }

}
