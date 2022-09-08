package com.offpen.sp_pen;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.offpen.sp_pen.Admin.InviteAdmin;
import com.offpen.sp_pen.Tag.TagPenActivity;
import com.offpen.sp_pen.sideDrawer.My_org;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    NonSwipeViewPager pager;
    TabLayout mTabLayout;
    TabItem penTab, studentTab, adminTab;
    PagerAdapter pagerAdapter;
    BottomNavigationItemView navigation_add;
    BottomNavigationView bottomNavigationView;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Bundle bundle = getIntent().getExtras();

        String stuff = bundle.getString("fname");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(stuff);

        setSupportActionBar(toolbar);
        pager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tab_layout);

        penTab = findViewById(R.id.penTab);
        studentTab = findViewById(R.id.studentTab);
        adminTab = findViewById(R.id.adminTab);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);

        navigation_add = findViewById(R.id.navigation_add);

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);// enables hamburger sign
        toggle.syncState();

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);

        //for linking tab layout with the view pager
        // mTabLayout.setupWithViewPager(pager);

        // tagpenModel tagpenModel = new tagpenModel();
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.transparent));
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_add:
                        item.setCheckable(true);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (pos == 0) {
                                    Intent i = new Intent(HomeActivity.this, AddPenActivity.class);
                                    startActivity(i);
                                }
                                if (pos == 1) {
                                    Intent i = new Intent(HomeActivity.this, AddStudentActivity.class);
                                    startActivity(i);
                                }
                                if (pos == 2) {

                                    Intent i = new Intent(HomeActivity.this, InviteAdmin.class);
                                    startActivity(i);
                                }
                            }
                        }, 0050);
                        break;
                    case R.id.navigation_delete:
                        Intent i = new Intent(HomeActivity.this, PenDeleteActivity.class);
                        startActivity(i);
                        // viewpenAdapter.DeletePen deletePen = new viewpenAdapter.DeletePen();
                        // deletePen.execute();
                        break;
                    case R.id.create_tag:

                        Toast.makeText(HomeActivity.this, "You are at position " + mTabLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).

                                show();

                        if (pos == 0) {
                            Intent intent = new Intent(HomeActivity.this, TagPenActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("TabPosition", mTabLayout.getSelectedTabPosition());
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                        if (pos == 1) {
                         /*   Intent intent = new Intent(HomeActivity.this, TagStudentActivity.class);
                            startActivity(intent);*/

                            Intent intent = new Intent(HomeActivity.this, TagPenActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("TabPosition", mTabLayout.getSelectedTabPosition());
                            intent.putExtras(b);
                            startActivity(intent);
                        }


//todo create tag here btm nav
                        break;

                }
                return true;
            }
        });
       /* private BottomNavigationView.OnNavigationItemSelectedListener bottomnav= new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        } 29 aug 2020
       */

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // Toast.makeText(HomeActivity.this,"scrolling",Toast.LENGTH_SHORT).show();
            }

            // SharedPreferences sharedpreferences = getSharedPreferences(, Context.MODE_PRIVATE);
            @Override
            public void onPageSelected(int position) {
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
                switch (position) {

                    case 0:
                        Toast.makeText(HomeActivity.this, "frag 1 selected", Toast.LENGTH_SHORT).show();
                        pos = 0;
                        bottomNavigationView.getMenu().getItem(0).setTitle("Add");
                        break;
                    case 1:
                        Toast.makeText(HomeActivity.this, "frag 2 selected", Toast.LENGTH_SHORT).show();
                        pos = 1;
                        bottomNavigationView.getMenu().getItem(0).setTitle("Add");
                        break;
                    case 2:
                        // bottomNavigationView.setVisibility(View.GONE);
                        //   findViewById(R.id.navigation_add).setVisibility(View.GONE);
                        //   findViewById(R.id.navigation_invite).setVisibility(View.VISIBLE);
                        Toast.makeText(HomeActivity.this, "frag 3 selected", Toast.LENGTH_SHORT).show();
                        pos = 2;
                        bottomNavigationView.getMenu().getItem(0).setTitle("Invite");
                        break;

                }

              /*  PenFragment pf = new PenFragment();
                pf.onResume();*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // Side Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.my_acc_s_menu) {
            Toast.makeText(HomeActivity.this, "First item selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.org_s_menu) {
            Intent i = new Intent(getApplicationContext(), My_org.class);
            startActivity(i);
            Toast.makeText(HomeActivity.this, "Second item selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.set_s_menu) {
            Toast.makeText(HomeActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.war_s_menu) {
            Toast.makeText(HomeActivity.this, "Warranties selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.report_s_menu) {
            Toast.makeText(HomeActivity.this, "Reports selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.arc_s_menu) {
            Toast.makeText(HomeActivity.this, "View Archive selected", Toast.LENGTH_SHORT).show();
        }


        return false;
    }
}