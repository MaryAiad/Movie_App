package com.example.mkany.movieapp.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mkany.movieapp.DB.TaskContract;
import com.example.mkany.movieapp.DB.TaskDBHelper;
import com.example.mkany.movieapp.FavoritesDetails;
import com.example.mkany.movieapp.MainActivity;
import com.example.mkany.movieapp.R;
import com.example.mkany.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mery on 11/9/2017.
 */

public class FavoritesAdaptor extends RecyclerView.Adapter<FavoritesAdaptor.MovieViewHolder>{
    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    ImageButton imageButton;

    public FavoritesAdaptor(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public FavoritesAdaptor() {
        this.movies = new ArrayList<>();
    }

    public void add (Movie movie)
    {
        movies.add(movie);
        notifyItemInserted(movies.size()-1);
    }

    public int search(Movie movie){
        System.err.println("sssss "+movies.size()+ "name "+movie.getOriginalTitle());
        for(int i = 0; i< movies.size(); i++ ){
            if(movie.getOriginalTitle().equals(movies.get(i).getOriginalTitle())){
                System.err.println("yes "+i);
                return i;
            }
        }
        return 0;
    }
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView rating;
        ImageView movieImage;

        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieImage = (ImageView) v.findViewById(R.id.movie_image);
            movieTitle = (TextView) v.findViewById(R.id.title);
            rating = (TextView) v.findViewById(R.id.rating);
            imageButton = (ImageButton) v.findViewById(R.id.star);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION)
                    {
                        Movie movie = movies.get(pos);
                        Intent intent = new Intent(context, FavoritesDetails.class);
                        intent.putExtra("original_title", movie.getOriginalTitle());
                        intent.putExtra("poster_path", movie.getPosterPath());
                        intent.putExtra("overview", movie.getOverview());
                        intent.putExtra("vote_average", Double.toString(movie.getVoteAverage()));
                        intent.putExtra("release_date", movie.getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public FavoritesAdaptor.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FavoritesAdaptor.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoritesAdaptor.MovieViewHolder holder, final int position) {
        Glide.with(context).load(movies.get(position).getPosterPath())
                .placeholder(R.drawable.ic_loadingcurved)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.movieImage);
        holder.movieTitle.setText(movies.get(position).getOriginalTitle());
        holder.rating.setText(Double.toString(movies.get(position).getVoteAverage()));
        imageButton.setBackgroundResource(R.drawable.ic_star_black_24dp);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog builder = new AlertDialog.Builder(context)
                        .setTitle("Remove Favorite")
                        .setMessage("Are you sure remove this film from favorites??!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int i){

                                SQLiteDatabase database = MainActivity.taskDBHelper.getWritableDatabase();
                                if(movies.get(position).getOriginalTitle().contains("'")){
                                    database.execSQL("DELETE FROM " + TaskContract.TaskEntry.TABLE+ " WHERE "+
                                            TaskContract.TaskEntry.COL_MOVIE_Overview+"= '" +movies.get(position).getOverview()+"'");
                                }
                                else {
                                    database.execSQL("DELETE FROM " + TaskContract.TaskEntry.TABLE + " WHERE " +
                                            TaskContract.TaskEntry.COL_MOVIE_TITLE + "= '" + movies.get(position).getOriginalTitle() + "'");
                                }
                                    database.close();

                                TranslateAnimation animation = new TranslateAnimation(0, holder.itemView.getWidth(), 0, 0);
                                animation.setDuration(500);
                                animation.setFillAfter(true);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        imageButton.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        movies.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, movies.size());
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                holder.itemView.startAnimation(animation);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int i){
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movies!= null && !movies.isEmpty()){
            return movies.size();
        }
        else
            return 0;
    }
}
