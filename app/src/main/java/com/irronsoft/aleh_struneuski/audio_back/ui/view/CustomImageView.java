package com.irronsoft.aleh_struneuski.audio_back.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.irronsoft.aleh_struneuski.audio_back.utils.ImageUtils;

public class CustomImageView extends AppCompatImageView {

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void transition(final Bitmap second) {
        if (second == null || second.getWidth() < 1 || second.getHeight() < 1) return;
        final int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        new TransitionTask().execute(second, size, size);
    }

    public void transition(Drawable second) {
        transition(ImageUtils.drawableToBitmap(second));
    }

    private class TransitionTask extends AsyncTask<Object, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Object... params) {
            try {
                return ThumbnailUtils.extractThumbnail((Bitmap) params[0], (Integer) params[1], (Integer) params[1]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            if (result == null) {
                setImageBitmap(result);
                return;
            }

            Animation exitAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
            exitAnim.setDuration(150);
            exitAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setImageBitmap(result);
                    Animation enterAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                    enterAnim.setDuration(150);
                    startAnimation(enterAnim);
                }
            });
            startAnimation(exitAnim);
        }
    }

}
