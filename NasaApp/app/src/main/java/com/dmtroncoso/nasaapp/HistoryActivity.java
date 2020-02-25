package com.dmtroncoso.nasaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class HistoryActivity extends AppCompatActivity implements NasaImageFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


    }

    @Override
    public void onListFragmentInteraction(NasaImage item) {

    }
}
