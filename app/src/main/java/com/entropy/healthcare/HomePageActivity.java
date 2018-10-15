package com.entropy.healthcare;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfAlreadyLoggedIn();
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar=findViewById(R.id.toolbar_homepage_activity);
        setSupportActionBar(toolbar);
        DrawerLayout drawer=findViewById(R.id.drawer_layout_homepage_activity);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
    }

    public void checkIfAlreadyLoggedIn(){
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if (user==null) startActivity(new Intent(HomePageActivity.this,LogInActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_homepage,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_homepage:{
                auth.signOut();
                startActivity(new Intent(HomePageActivity.this,LogInActivity.class));
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
