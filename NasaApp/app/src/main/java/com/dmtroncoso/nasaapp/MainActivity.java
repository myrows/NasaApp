package com.dmtroncoso.nasaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;

import org.joda.time.LocalDate;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ISelectDateListener {

    FloatingActionButton fabDate;
    FloatingActionButton fabHistoric;
    ImageView imagePreview;
    TextView txtExplanation;
    TextView txtTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NasaAsyncTask(this).execute();

        imagePreview = findViewById(R.id.imageViewPictureMain);
        txtTitle = findViewById(R.id.textViewTitle);
        txtExplanation = findViewById(R.id.textViewExplanation);
        fabHistoric = findViewById(R.id.floatingActionItemHistorical);
        fabDate = findViewById(R.id.floatingActionItemDate);


        txtTitle.setMovementMethod(new ScrollingMovementMethod());
        txtExplanation.setMovementMethod(new ScrollingMovementMethod());
        fabDate.setOnClickListener(this);
        fabHistoric.setOnClickListener(this);
        fabDate.setOnClickListener(this);
        imagePreview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.floatingActionItemHistorical){
            Intent intent = new Intent(this, HistoryActivity.class);
            this.startActivity(intent);
        }else if(v.getId() == R.id.imageViewPictureMain){
            txtTitle.setVisibility(v.VISIBLE);
            txtExplanation.setVisibility(v.VISIBLE);
        }else if(v.getId() == R.id.floatingActionItemDate){
            DialogFragment dialogFragment = DialogFechaFragment.newInstance(this);
            dialogFragment.show(getSupportFragmentManager(), "datePicker");
        }

    }

    @Override
    public void onDateSelected(int year, int month, int day) {

        LocalDate date = new LocalDate(year, month+(1), day);
        LocalDate dateNow = LocalDate.now();

        if(date.compareTo(dateNow) == 1){
            Toast.makeText(this, "Wrong date", Toast.LENGTH_SHORT).show();
        }else{

            Intent intent = new Intent(this, ImageDateActivity.class);
            intent.putExtra("dateSelected", date.toString());
            Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show();
            startActivity(intent);

        }



    }


    private class NasaAsyncTask extends AsyncTask<NasaImage, Void, NasaImage> {

        MainActivity mainActivity;

        public NasaAsyncTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        NasaApi nasaApi = new NasaApi("NryfCSMeS824H1J2wQ8PLIsy9Uj8H0cE3ZrW1CQ4");

        TextView title;
        TextView explanation;
        ImageView imagePreview;
        String urlSelected;


        @Override
        protected NasaImage doInBackground(NasaImage... nasaImages) {
            return nasaApi.getPicOfToday();
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "Downloading imagenes ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(NasaImage nasaImage) {
            Toast.makeText(MainActivity.this, "Images downloaded", Toast.LENGTH_SHORT).show();

            title = findViewById(R.id.textViewTitle);
            explanation = findViewById(R.id.textViewExplanation);
            imagePreview = findViewById(R.id.imageViewPictureMain);

            title.setText(nasaImage.getTitle());
            explanation.setText(nasaImage.getExplanation());

            urlSelected = nasaImage.getUrl();

            if(urlSelected.contains("https://www.youtube.com/")){
                Glide
                        .with(mainActivity)
                        .load(urlSelected)
                        .error(R.drawable.ic_youtube_logo_video)
                        .into(imagePreview);
            }else {

                Glide
                        .with(mainActivity)
                        .load(urlSelected)
                        .error(R.drawable.ic_youtube_logo_video)
                        .thumbnail(Glide.with(mainActivity).load(R.drawable.ic_spinner))
                        .into(imagePreview);
            }

            Toast.makeText(mainActivity, "Click on image to scroll", Toast.LENGTH_LONG).show();

        }
    }

}

