package com.entropy.healthcare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawer;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfAlreadyLoggedIn();
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar=findViewById(R.id.toolbar_homepage_activity);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout_homepage_activity);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        toggle.syncState();
        drawer.addDrawerListener(toggle);


        navigationView=findViewById(R.id.nav_view_homepage_activity);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                changeToFragment(menuItem);
                return false;
            }
        });

        changeToFragment(navigationView.getMenu().getItem(0));


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

    public void changeToFragment(MenuItem item){
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment frag;
        switch(item.getItemId()){
            case R.id.profile_nav_drawer:{
                frag=new ProfileFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout_area_homepage_activity,frag).commit();
                break;
            }
            case R.id.doctor_list_nav_drawer:{
                break;
            }
            case R.id.archives_nav_drawer:{
                break;
            }
            case R.id.query_nav_drawer:{
                break;
            }
            case R.id.order_meds_nav_drawer:{
                break;
            }
            case R.id.doctor_portal_nav_drawer:{
                break;
            }
            case R.id.help_nav_drawer:{
                break;
            }
            case R.id.about_nav_drawer:{
                break;
            }
            default:{
                frag=null;
            }
        }

        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
    }







}
