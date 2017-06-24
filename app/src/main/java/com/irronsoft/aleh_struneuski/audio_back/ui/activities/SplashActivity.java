package com.irronsoft.aleh_struneuski.audio_back.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.async.Action;
import com.afollestad.async.Async;
import com.afollestad.async.Done;
import com.afollestad.async.Result;
import com.irronsoft.aleh_struneuski.audio_back.R;

/**
 * Created by alehstruneuski on 6/23/17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        openRequest();
    }

    private void openRequest() {
            Async.parallel(
                    new Action() {

                        @NonNull
                        @Override
                        public String id() {
                            return "fetch_artists";
                        }

                        @Nullable
                        @Override
                        protected Object run() throws InterruptedException {
                            return "tut";
                        }
                    }
            ).done(new Done() {
                @Override
                public void result(@NonNull Result result) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }, 1500);
                }
            });

        }

}
