package br.com.android.invviteme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;

import br.com.android.invviteme.InvviteMe;
import br.com.android.invviteme.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

        InvviteMe invviteMe = new InvviteMe();
        invviteMe.setFont(getApplicationContext());

        new Handler().postDelayed(this, 2100);
    }

    @Override
    public void run() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

}
