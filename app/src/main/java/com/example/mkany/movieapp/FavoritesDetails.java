package com.example.mkany.movieapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mkany.movieapp.DB.TaskContract;
import com.example.mkany.movieapp.DB.TaskDBHelper;

/**
 * Created by Mery on 11/26/2017.
 */

public class FavoritesDetails extends AppCompatActivity{
    TextView plotSynopsis, releaseDate;
    ImageView imageView;
    RatingBar ratingBar;
    String thubnail, movieName, overview, rating, dateRelease;
    ImageButton imptyStar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_details);

        imageView  = (ImageView) findViewById(R.id.image_header);
        plotSynopsis = (TextView) findViewById(R.id.plotsyn);
        releaseDate = (TextView) findViewById(R.id.release_date);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        imptyStar = (ImageButton) findViewById(R.id.star);

        Intent intentTheStartedThisActivity = getIntent();
        if(intentTheStartedThisActivity.hasExtra("original_title")){
            thubnail = getIntent().getExtras().getString("poster_path");
            movieName = getIntent().getExtras().getString("original_title");
            overview = getIntent().getExtras().getString("overview");
            rating = getIntent().getExtras().getString("vote_average");
            dateRelease = getIntent().getExtras().getString("release_date");

            Glide.with(this).load(thubnail)
                    .into(imageView);
            plotSynopsis.setText(overview);
            releaseDate.setText(dateRelease);
            ratingBar.setRating(Float.parseFloat(rating)/2);
        }
        else
        {
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show();
        }
    }
}