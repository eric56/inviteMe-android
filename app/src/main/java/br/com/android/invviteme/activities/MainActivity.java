package br.com.android.invviteme.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pkmmte.view.CircularImageView;

import br.com.android.invviteme.R;
import br.com.android.invviteme.enums.KeysSharedPreference;
import br.com.android.invviteme.fragments.FragmentEvent;
import br.com.android.invviteme.fragments.FragmentEventsNearMe;
import br.com.android.invviteme.fragments.FragmentProfile;
import br.com.android.invviteme.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager manager;
    private boolean isDrawerOpen = false;

    private DrawerLayout drawer;
    private FrameLayout contentPage;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private TextView nameUserDrawer, emailUserDrawer;
    private CircularImageView photoUserDrawer;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        manager = getSupportFragmentManager();

        if((int) getResources().getDimension(R.dimen.margin_start_drawer) == (int) getResources().getDimension(R.dimen.drawer_width)) {
            isDrawerOpen = true;
        }

        referenceUI();
        referenceUIDrawer();
        configureDrawer();

        if(savedInstanceState == null){
            FragmentEvent fragmentEvent = new FragmentEvent();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.fragmentMain,fragmentEvent,"fragmentEvent");
            fragmentTransaction.commit();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    if(AccessToken.getCurrentAccessToken() != null){
                        LoginManager.getInstance().logOut();
                    }
                    SharedPreferencesUtils.clearSharedPreferenceUsers(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

    }

    private void configureDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        if(isDrawerOpen){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if(mAuth.getCurrentUser() != null) {
            nameUserDrawer.setText(SharedPreferencesUtils.selectByKeyFromSPUsers(MainActivity.this, KeysSharedPreference.NAMEUSER.getKey()));
            emailUserDrawer.setText(SharedPreferencesUtils.selectByKeyFromSPUsers(MainActivity.this, KeysSharedPreference.EMAILUSER.getKey()));
        }
    }

    private void referenceUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        contentPage = (FrameLayout) findViewById(R.id.fragmentMain);
    }

    private void referenceUIDrawer(){
        View headerLayout = navigationView.getHeaderView(0);
        nameUserDrawer = (TextView) headerLayout.findViewById(R.id.nameUserDrawer);
        emailUserDrawer = (TextView) headerLayout.findViewById(R.id.emailUserDrawer);
        photoUserDrawer = (CircularImageView) headerLayout.findViewById(R.id.photoUserDrawer);
    }

    private DialogInterface.OnClickListener buttonDialog() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switch (id) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ProgressDialog dialogLogout = new ProgressDialog(MainActivity.this);
                        dialogLogout.setIndeterminate(true);
                        dialogLogout.setCancelable(false);
                        dialogLogout.setMessage(getString(R.string.logout_message_dialog));
                        dialogLogout.show();
                        mAuth.signOut();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            if(!isDrawerOpen){
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Dar o fragmentReplace no fragment em cada if corresponde
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            FragmentEvent fragmentEvent = new FragmentEvent();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentMain,fragmentEvent,"fragmentEvent");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profile) {
            FragmentProfile fragmentProfile = new FragmentProfile();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentMain,fragmentProfile,"fragmentProfile");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_event_near_me) {
            FragmentEventsNearMe fragmentEventsNearMe = new FragmentEventsNearMe();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentMain,fragmentEventsNearMe,"fragmentEventsNearMe");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setIcon(android.R.drawable.ic_menu_info_details);
            dialog.setTitle(R.string.title_alert_logout);
            dialog.setPositiveButton(R.string.positive_button, buttonDialog());
            dialog.setNegativeButton(R.string.negative_button, buttonDialog());
            dialog.setCancelable(false);
            dialog.show();
        }

        if(!isDrawerOpen){
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
