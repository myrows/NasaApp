package com.dmtroncoso.nasaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imagePreviewDetail;
    TextView txtTitle;
    TextView txtExplanation;
    String urlSelected;
    String [] urlArray;
    int urlArrayCount;
    String videoId;
    String videoIdFormated;
    String miniatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imagePreviewDetail = findViewById(R.id.imageViewPreviewDetails);
        txtTitle = findViewById(R.id.textViewTitleDetails);
        txtExplanation = findViewById(R.id.textViewExplanationDetails);

        imagePreviewDetail.setOnClickListener(this);

        //Get id video with split to get a character array
        urlSelected = getIntent().getExtras().getString("imgNasaSelected");
        urlArray = urlSelected.split("/");
        urlArrayCount = urlArray.length;
        videoId = urlArray[urlArrayCount-1];
        videoIdFormated = videoId.replaceAll("rel=0", "");
        miniatura = videoId.split("\\?")[0];


        if(urlSelected.contains("https://www.youtube.com/")) {
            Glide
                    .with(this)
                    .load("http://img.youtube.com/vi/"+miniatura+"/hqdefault.jpg")
                    .into(imagePreviewDetail);
        }else {

            Glide
                    .with(this)
                    .load(getIntent().getExtras().getString("imgNasaSelected"))
                    .into(imagePreviewDetail);
        }

        Toast.makeText(this, "Click on image to scroll", Toast.LENGTH_LONG).show();

        txtTitle.setText(getIntent().getExtras().getString("titleNasaSelected"));
        txtExplanation.setText(getIntent().getExtras().getString("explanationNasaSelected"));

    }

    @Override
    public void onClick(View v) {
        txtTitle.setVisibility(View.VISIBLE);
        txtExplanation.setVisibility(View.VISIBLE);
    }
}
