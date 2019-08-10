package com.irronsoft.aleh_struneuski.audio_back.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.irronsoft.aleh_struneuski.audio_back.Background;
import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;


public class SplashActivity extends AppCompatActivity {

    private Background mBackground;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBackground = (Background) this.getApplicationContext();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        new ExtractPlayListTask().execute();
    }

    private class ExtractPlayListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            mBackground.extractPlayList(null);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Class nextActivity = null;
            if (prefs.getBoolean(ProjectConstants.FIRST_LAUNCH_FLAG, true)) {
                prefs.edit().putBoolean(ProjectConstants.FIRST_LAUNCH_FLAG, false).apply();
                nextActivity = IntroActivity.class;
            } else {
                nextActivity = MainActivity.class;
            }

            final Class finalNextActivity = nextActivity;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, finalNextActivity));
                    finish();
                }
            }, 3500);
        }
    }
}
