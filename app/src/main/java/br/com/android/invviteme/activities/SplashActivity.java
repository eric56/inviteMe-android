package br.com.android.invviteme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import br.com.android.invviteme.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(this, 2100);
    }

    @Override
    public void run() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

}
