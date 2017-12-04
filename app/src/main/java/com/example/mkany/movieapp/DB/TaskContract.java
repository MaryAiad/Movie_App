package com.example.mkany.movieapp.DB;

import android.provider.BaseColumns;

/**
 * Created by Mery on 11/26/2017.
 */

public class TaskContract {

    public final static String DB_Name = "MovieApp";
    public final static int DB_Version = 1;

    public class TaskEntry implements BaseColumns{
        public static final String TABLE = "Favorites";

        public static final String COL_MOVIE_TITLE = "title";
        public static final String COL_MOVIE_Image = "imagePath";
        public static final String COL_MOVIE_RATING = "rating";
        public static final String COL_MOVIE_Release = "releaseDate";
        public static final String COL_MOVIE_Overview = "overview";
    }
}
