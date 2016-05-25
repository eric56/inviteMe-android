package br.com.android.invviteme.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import br.com.android.invviteme.R;
import br.com.android.invviteme.fragments.FragmentEvent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager manager;
    boolean isDrawerOpen = false;

    DrawerLayout drawer;
    FrameLayout contentPage;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        referenceUI();
        if((int) getResources().getDimension(R.dimen.margin_start_drawer) == (int) getResources().getDimension(R.dimen.drawer_width)) {
            isDrawerOpen = true;
        }
        configureDrawer();
        if(savedInstanceState == null){
            FragmentEvent fragmentEvent = new FragmentEvent();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.fragmentMain,fragmentEvent,"fragmentEvent");
            fragmentTransaction.commit();
        }
    }

    private void configureDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        if(isDrawerOpen){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void referenceUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        contentPage = (FrameLayout) findViewById(R.id.fragmentMain);
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_event_near_me) {

        } else if (id == R.id.nav_logout) {

        }

        if(!isDrawerOpen){
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
