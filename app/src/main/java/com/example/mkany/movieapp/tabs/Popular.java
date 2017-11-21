package com.example.mkany.movieapp.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkany.movieapp.R;

import java.util.zip.Inflater;

/**
 * Created by Mery on 11/14/2017.
 */

public class Popular extends Fragment {
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View v = layoutInflater.inflate((R.layout.popular), container, false);
        TextView textView = (TextView) v.findViewById(R.id.pop);
//        textView.setText("Populaaaaaar");
        return v;
    }
}
