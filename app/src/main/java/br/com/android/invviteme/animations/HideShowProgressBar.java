package br.com.android.invviteme.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

public class HideShowProgressBar {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean isShowProgress, final View progressBar, final View ui, Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            ui.setVisibility(isShowProgress ? View.GONE : View.VISIBLE);
            ui.animate().setDuration(shortAnimTime).alpha(isShowProgress ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ui.setVisibility(isShowProgress ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(isShowProgress ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
            ui.setVisibility(isShowProgress ? View.GONE : View.VISIBLE);
        }
    }
}
