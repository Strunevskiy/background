package com.irronsoft.aleh_struneuski.audio_back.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.async.Action;
import com.afollestad.async.Async;
import com.afollestad.async.Done;
import com.afollestad.async.Result;
import com.irronsoft.aleh_struneuski.audio_back.Background;
import com.irronsoft.aleh_struneuski.audio_back.R;
import com.irronsoft.aleh_struneuski.audio_back.constants.ProjectConstants;

/**
 * Created by alehstruneuski on 6/23/17.
 */

public class SplashActivity extends AppCompatActivity {

    private Background mBackground;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBackground = (Background) this.getApplicationContext();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        openRequest();
    }

    private void openRequest() {
            Async.parallel(
                    new Action() {

                        @NonNull
                        @Override
                        public String id() {
                            return "fetch_playlist";
                        }

                        @Nullable
                        @Override
                        protected Object run() throws InterruptedException {
                            mBackground.extractPlayList(null);
                            return null;
                        }
                    }
            ).done(new Done() {
                @Override
                public void result(@NonNull Result result) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (prefs.getBoolean(ProjectConstants.FIRST_LAUNCH_FLAG, true)) {
                                prefs.edit().putBoolean(ProjectConstants.FIRST_LAUNCH_FLAG, false).apply();
                                startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }, 1500);
                }
            });
        }
}
