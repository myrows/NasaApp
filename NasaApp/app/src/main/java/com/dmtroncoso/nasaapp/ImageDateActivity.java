package com.dmtroncoso.nasaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ImageDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_date);

        new NasaDateAsyncTask(this).execute();
    }

    private class NasaDateAsyncTask extends AsyncTask<NasaImage, Void, NasaImage> implements View.OnClickListener {

        ImageDateActivity imageDateActivity;
        ImageView imageViewDate;
        TextView txtTitle;
        TextView txtExplanation;
        String date;
        String urlSelected;


        public NasaDateAsyncTask(ImageDateActivity imageDateActivity) {
            this.imageDateActivity = imageDateActivity;
        }

        NasaApi nasaApi = new NasaApi("NryfCSMeS824H1J2wQ8PLIsy9Uj8H0cE3ZrW1CQ4");

        @Override
        protected NasaImage doInBackground(NasaImage... nasaImages) {
            date = getIntent().getExtras().getString("dateSelected");
            return nasaApi.getPicOfAnyDate(date);
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(imageDateActivity, "Downloading images ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(NasaImage nasaImage) {
            Toast.makeText(imageDateActivity, "Images downloaded", Toast.LENGTH_SHORT).show();

            imageViewDate = findViewById(R.id.imageViewDateSelected);
            txtTitle = findViewById(R.id.textViewTitleDate);
            txtExplanation = findViewById(R.id.textViewExplanationDate);

            imageViewDate.setOnClickListener(this);

            urlSelected = nasaImage.getUrl();

            if(urlSelected.contains("https://www.youtube.com/")){
                Glide
                        .with(imageDateActivity)
                        .load(urlSelected)
                        .error(R.drawable.ic_youtube_logo_video)
                        .into(imageViewDate);
            }else {

                Glide
                        .with(imageDateActivity)
                        .load(urlSelected)
                        .error(R.drawable.ic_youtube_logo_video)
                        .thumbnail(Glide.with(imageDateActivity).load(R.drawable.ic_spinner))
                        .into(imageViewDate);
            }

            txtTitle.setText(nasaImage.getTitle());
            txtExplanation.setText(nasaImage.getExplanation());

            Toast.makeText(imageDateActivity, "Click on image to scroll", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onClick(View v) {
            txtTitle.setVisibility(View.VISIBLE);
            txtExplanation.setVisibility(View.VISIBLE);

            if(urlSelected.contains("https://www.youtube.com/")){
                Intent youtubeWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlSelected));
                imageDateActivity.startActivity(youtubeWebIntent);

            }
        }
    }
}
