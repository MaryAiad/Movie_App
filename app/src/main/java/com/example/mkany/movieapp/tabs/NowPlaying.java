package com.example.mkany.movieapp.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mkany.movieapp.R;

/**
 * Created by Mery on 11/14/2017.
 */

public class NowPlaying extends Fragment{
        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
            return layoutInflater.inflate(R.layout.now_playing, container, false);
        }
}
