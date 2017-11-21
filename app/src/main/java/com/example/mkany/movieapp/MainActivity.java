package com.example.mkany.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.opengl.EGLExt;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkany.movieapp.adaptor.MovieAaptor;
import com.example.mkany.movieapp.api.Client;
import com.example.mkany.movieapp.api.Service;
import com.example.mkany.movieapp.model.Movie;
import com.example.mkany.movieapp.model.MovieResponse;
import com.example.mkany.movieapp.tabs.NowPlaying;
import com.example.mkany.movieapp.tabs.Popular;
import com.example.mkany.movieapp.tabs.TopRated;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    public static ViewPager viewPager;

    private final static String API_KEY = "62e640366d4a28a538995764bd7d1401";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//         viewPager = (ViewPager) findViewById(R.id.view);
//        setupViewPager(viewPager);
//        viewPager.setAdapter(new CustomAdaptor(getSupportFragmentManager(), getApplicationContext()));

//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.addTab(tabLayout.newTab().setText("Popular"));
//        tabLayout.addTab(tabLayout.newTab().setText("Top Rated"));
//        tabLayout.addTab(tabLayout.newTab().setText("Now Playing"));
//
//  tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

//        final CustomAdaptor adapter = new CustomAdaptor(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//        });

//        intiViews();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.background_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                intiViews();
            }
        });
    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new Popular(), "Popular");
//        adapter.addFragment(new TopRated(), "Top Rated");
//        adapter.addFragment(new NowPlaying(), "Now Playing");
//        viewPager.setAdapter(adapter);
//    }

//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
////            return mFragmentTitleList.get(position);
//
////             return null to display only the icon
//            return null;
//        }
//    }

    private Activity getActivity() {
        Context context= this;
        while(context instanceof ContextWrapper)
        {
            if(context instanceof Activity)
            {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void intiViews() {
        if(isNetworkConnected())
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetchig movies...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            }
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            connectAndGetApiData();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // This method create an instance of Retrofit
    public void connectAndGetApiData(){

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        Service movieApiService = retrofit.create(Service.class);

        Call<MovieResponse> call = movieApiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MovieAaptor(movies, R.layout.movie_card, getApplicationContext()));
                Log.d(TAG, "Number of movies received: " + movies.size());
                recyclerView.smoothScrollToPosition(0);
                    if(swipeRefreshLayout.isRefreshing())
                    {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }

//    private RecyclerView recyclerView;
//    private MovieAaptor movieAaptor;
//    private List<Movie> movieList;
//    ProgressDialog progressDialog;
//    private SwipeRefreshLayout swipeRefreshLayout;
////    public static final String Log_tag = MovieAaptor.class.getName();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        intiViews();
//
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_content);
//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                intiViews();
//                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



//    private void loadJson() {
//
//        try{
//            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
//                Toast.makeText(getApplicationContext(), "Please obtain API Key Firstly", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                return;
//            }
//
//            Client client = new Client();
//            Service apiService = client.getClient().create(Service.class);
//            Call<MovieResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
//
//            call.enqueue(new Callback<MovieResponse>() {
//                @Override
//                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                    List<Movie> movies = response.body().getResults();
//                    Toast.makeText(getApplicationContext(), "Moviess "+movies.get(0).getOriginalTitle(), Toast.LENGTH_SHORT).show();
//                    recyclerView.setAdapter(new MovieAaptor(getApplicationContext(), movies));
//                    recyclerView.smoothScrollToPosition(0);
//                    if(swipeRefreshLayout.isRefreshing())
//                    {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(Call<MovieResponse> call, Throwable t) {
//                    Log.d("Error", t.getMessage());
//                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }catch (Exception e)
//        {
//            Log.d("Error`", e.getMessage());
//            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private class CustomAdaptor extends FragmentStatePagerAdapter {
////        private String names []= {"Popular", "Top Rated", "Now Playing"};
//        int tabCount;
//        public CustomAdaptor(FragmentManager supportFragmentManager, int numOfTabs) {
//            super(supportFragmentManager);
//            tabCount = numOfTabs;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return new Popular();
//                case 1:
//                    return new TopRated();
//                case 2:
//                    return new NowPlaying();
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return tabCount;
//        }
//
////        @Override
////        public CharSequence getPageTitle(int i)
////        {
////            return names[i];
////        }
//    }
}
