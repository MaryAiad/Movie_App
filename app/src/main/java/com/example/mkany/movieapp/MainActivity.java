package com.example.mkany.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mkany.movieapp.DB.TaskDBHelper;
import com.example.mkany.movieapp.tabs.Favorites;
import com.example.mkany.movieapp.tabs.NowPlaying;
import com.example.mkany.movieapp.tabs.Popular;
import com.example.mkany.movieapp.tabs.TopRated;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static TaskDBHelper taskDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        MainActivity.taskDBHelper = new TaskDBHelper(this);

        intiViews();
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdaptor adaptor = new ViewPagerAdaptor(getSupportFragmentManager());
        adaptor.addFragment(new Popular(), "Popular");
        adaptor.addFragment(new TopRated(), "Top Rated");
        adaptor.addFragment(new NowPlaying(), "Now Playing");
        adaptor.addFragment(new Favorites(), "Favorites");
        viewPager.setAdapter(adaptor);
    }

    class ViewPagerAdaptor extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        public void addFragment(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void intiViews() {

        if(isNetworkConnected())
        {
            setUpViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
        else {
            final AlertDialog builder = new AlertDialog.Builder(this)
                    .setTitle("Connection failed")
                    .setMessage("This application requires network access, Please enable mobile network or Wi-Fi.")
                    .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int i){
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int i){
                        }
                    })
                    .show();
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
