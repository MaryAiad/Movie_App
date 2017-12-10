package com.example.mkany.movieapp.tabs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkany.movieapp.DB.TaskContract;
import com.example.mkany.movieapp.MainActivity;
import com.example.mkany.movieapp.R;
import com.example.mkany.movieapp.adaptor.FavoritesAdaptor;
import com.example.mkany.movieapp.model.Movie;

import java.util.ArrayList;

/**
 * Created by Mery on 11/26/2017.
 */
public class Favorites extends Fragment {
    public static FavoritesAdaptor movieAaptor;
    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList;
    private TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.top_rated),container,false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_content);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        movieList = readDB();

        recyclerView = (RecyclerView) view.findViewById(R.id.tab_one_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        textView = (TextView) view.findViewById(R.id.show_more);
        textView.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        movieAaptor = new FavoritesAdaptor(movieList, R.layout.favorite_card, getActivity());
        recyclerView.setAdapter(movieAaptor);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDB();
                movieAaptor.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public ArrayList<Movie> readDB()
    {
        ArrayList<Movie> movies = new ArrayList<>();
        if(getActivity() instanceof MainActivity){
            SQLiteDatabase database = ((MainActivity) getActivity()).taskDBHelper.getReadableDatabase();
            final String [] coloumns = {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_MOVIE_TITLE,
                    TaskContract.TaskEntry.COL_MOVIE_Image, TaskContract.TaskEntry.COL_MOVIE_RATING,
                    TaskContract.TaskEntry.COL_MOVIE_Release, TaskContract.TaskEntry.COL_MOVIE_Overview};

            Cursor cursor = database.query(TaskContract.TaskEntry.TABLE, coloumns,
                    null, null, null, null, null);

            cursor.moveToFirst();
            if(cursor.moveToFirst())
            {
                Movie movie = new Movie();
                movie.setOriginalTitle(cursor.getString(1));
                movie.setPosterPath(cursor.getString(2));
                movie.setVoteAverage(cursor.getDouble(3));
                movie.setReleaseDate(cursor.getString(4));
                movie.setOverview(cursor.getString(5));
                movies.add(movie);
            }
            while(cursor.moveToNext())
            {
                Movie movie = new Movie();
                movie.setOriginalTitle(cursor.getString(1));
                movie.setPosterPath(cursor.getString(2));
                movie.setVoteAverage(cursor.getDouble(3));
                movie.setReleaseDate(cursor.getString(4));
                movie.setOverview(cursor.getString(5));
                movies.add(movie);
            }
            cursor.close();
            database.close();
        }
        else{
            Toast.makeText(getActivity(), "not instance ", Toast.LENGTH_SHORT).show();
        }

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        return movies;
    }

    public FavoritesAdaptor adaptor() {
        if(movieAaptor!= null) {
            return movieAaptor;
        }
        else{
            return null;
        }
    }
}
