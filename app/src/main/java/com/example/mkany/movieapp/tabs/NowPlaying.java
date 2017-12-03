package com.example.mkany.movieapp.tabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkany.movieapp.MainActivity;
import com.example.mkany.movieapp.R;
import com.example.mkany.movieapp.adaptor.MovieAaptor;
import com.example.mkany.movieapp.api.Client;
import com.example.mkany.movieapp.api.Service;
import com.example.mkany.movieapp.model.Movie;
import com.example.mkany.movieapp.model.MovieResponse;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mery on 11/14/2017.
 */

public class NowPlaying extends Fragment {
    private static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;
    private MovieAaptor movieAaptor;

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String API_KEY = "62e640366d4a28a538995764bd7d1401";
    public View view;
    private TextView textView;
    private int countPlaying = 2;
    public ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    public NowPlaying(){

    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        view = layoutInflater.inflate((R.layout.top_rated),container,false);
        movieAaptor = new MovieAaptor();

        textView = (TextView) view.findViewById(R.id.show_more);
        textView.setVisibility(View.INVISIBLE);

        intiViews();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_content);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                intiViews();
                Toast.makeText(getActivity(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private Activity getActivitya() {
        Context context= getActivity();
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetchig movies...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        recyclerView = (RecyclerView) view.findViewById(R.id.tab_one_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(getActivitya().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        connectAndGetApiData();
    }

    public void connectAndGetApiData(){
        retrofit = Client.getClient();
        final Service movieApiService = retrofit.create(Service.class);

        Call<MovieResponse> call = movieApiService.getNowPlaying(API_KEY, 1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                movieAaptor = new MovieAaptor(movies, R.layout.movie_card, getActivity());

                recyclerView.setAdapter(movieAaptor);
                Log.d(TAG, "Number of movies received: " + movies.size());
                recyclerView.smoothScrollToPosition(0);

                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressDialog.dismiss();

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0) {
                            // Recycle view scrolling down...
                            if(!recyclerView.canScrollVertically(dy)){
                                textView.setVisibility(View.VISIBLE);
                                textView.getBackground().setAlpha(160);
                            }
                        }
                        else if(dy < 0){
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setVisibility(View.INVISIBLE);
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Fetchig movies...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();

                        Call<MovieResponse> call2 = movieApiService.getNowPlaying(API_KEY, countPlaying);
                        call2.enqueue(new Callback<MovieResponse>() {
                            @Override
                            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                                for(int i =0; i< response.body().getResults().size(); i++)
                                {
                                    movieAaptor.add(response.body().getResults().get(i));
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<MovieResponse> call, Throwable t) {

                            }
                        });
                        countPlaying += 1;
                    }
                });
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }
}
