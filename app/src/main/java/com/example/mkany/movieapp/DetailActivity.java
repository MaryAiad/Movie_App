package com.example.mkany.movieapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mkany.movieapp.DB.TaskContract;
import com.example.mkany.movieapp.DB.TaskDBHelper;
import com.example.mkany.movieapp.model.Movie;
import com.example.mkany.movieapp.tabs.Favorites;

/**
 * Created by Mery on 11/7/2017.
 */
public class DetailActivity extends AppCompatActivity {
    TextView nameOfMovie, plotSynopsis,releaseDate;
    ImageView imageView, star, bottomStar;
    RatingBar ratingBar;
    RelativeLayout bottomView;
    String thubnail, movieName, overview, dateRelease, halfPath;
    Double rating;
    Movie movie;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView  = (ImageView) findViewById(R.id.image_header);
        nameOfMovie = (TextView) findViewById(R.id.movie_title1);
        plotSynopsis = (TextView) findViewById(R.id.plotsyn);
        releaseDate = (TextView) findViewById(R.id.release_date);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        star = (ImageView) findViewById(R.id.imptyStar);
        bottomStar = (ImageView) findViewById(R.id.star);
        bottomView = (RelativeLayout) findViewById(R.id.bottomView);

        bottomView.getBackground().setAlpha(150);

        Intent intentTheStartedThisActivity = getIntent();
        if(intentTheStartedThisActivity.hasExtra("original_title")){
            thubnail = getIntent().getExtras().getString("poster_path");
            movieName = getIntent().getExtras().getString("original_title");
            overview = getIntent().getExtras().getString("overview");
            rating = Double.parseDouble(getIntent().getExtras().getString("vote_average"));
            dateRelease = getIntent().getExtras().getString("release_date");
            halfPath = getIntent().getExtras().getString("halfPosterPath");

            Glide.with(this).load(thubnail)
                    .into(imageView);
            nameOfMovie.setText(movieName);
            plotSynopsis.setText(overview);
            releaseDate.setText(dateRelease);
            ratingBar.setRating((float) (rating/2));

            movie = new Movie();
            movie.setOriginalTitle(movieName);
            movie.setPosterPath(halfPath);
            movie.setOverview(overview);
            movie.setReleaseDate(dateRelease);
            movie.setVoteAverage(rating);

            if (search(movie)){
                star.setBackgroundResource(R.drawable.ic_star_black_24dp);
                bottomStar.setBackgroundResource(R.drawable.ic_star_black_24dp);

                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Added already to favourites", Toast.LENGTH_SHORT).show();
                    }
                });

                bottomStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Added already to favourites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                star.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                bottomStar.setBackgroundResource(R.drawable.ic_star_border_black_24dp);

                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Add it to favourite", Toast.LENGTH_SHORT).show();
                        star.setBackgroundResource(R.drawable.ic_star_black_24dp);
                        bottomStar.setBackgroundResource(R.drawable.ic_star_black_24dp);
                        addDB();
                    }
                });

                bottomStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Add it to favourite", Toast.LENGTH_SHORT).show();
                        bottomStar.setBackgroundResource(R.drawable.ic_star_black_24dp);
                        star.setBackgroundResource(R.drawable.ic_star_black_24dp);
                        addDB();
                    }
                });
            }
        }
        else
        {
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show();
        }
        intialCollapsingToolbar();
    }

    public boolean search(Movie movie)
    {
        SQLiteDatabase database = MainActivity.taskDBHelper.getReadableDatabase();
        String query = "select "+TaskContract.TaskEntry.COL_MOVIE_TITLE + " from "+TaskContract.TaskEntry.TABLE +" where "+
                TaskContract.TaskEntry.COL_MOVIE_TITLE + " = '" + movie.getOriginalTitle() + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            System.err.println("not found ");
            cursor.close();
            return false;
        }
        else{
            System.err.println("found "+ cursor.getCount());
            cursor.close();
            return true;
        }
    }

    public void addDB()
    {
        SQLiteDatabase database = MainActivity.taskDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_MOVIE_TITLE, movieName);
        values.put(TaskContract.TaskEntry.COL_MOVIE_Image, halfPath);
        values.put(TaskContract.TaskEntry.COL_MOVIE_RATING, rating);
        values.put(TaskContract.TaskEntry.COL_MOVIE_Release, dateRelease);
        values.put(TaskContract.TaskEntry.COL_MOVIE_Overview, overview);

        database.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();
        Favorites.movieAaptor.add(movie);
    }

    private void intialCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1)
                {
                    scrollRange= appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0)
                {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                }
                else if(isShow)
                {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
