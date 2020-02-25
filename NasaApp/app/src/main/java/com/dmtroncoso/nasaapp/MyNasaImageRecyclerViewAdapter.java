package com.dmtroncoso.nasaapp;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmtroncoso.nasaapp.NasaImageFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyNasaImageRecyclerViewAdapter extends RecyclerView.Adapter<MyNasaImageRecyclerViewAdapter.ViewHolder>{

    private final List<NasaImage> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context ctx;

    public MyNasaImageRecyclerViewAdapter(List<NasaImage> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_nasaimage, parent, false);

        ctx = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if(holder.mItem.getUrl().contains("https://www.youtube.com/")){
            Glide
                    .with(ctx)
                    .load(holder.mItem.getUrl())
                    .error(R.drawable.ic_youtube_logo_video)
                    .into(holder.imageHistory);
        }else{
            Glide
                    .with(ctx)
                    .load(holder.mItem.getUrl())
                    .thumbnail(Glide.with(ctx).load(R.drawable.ic_spinner))
                    .into(holder.imageHistory);
        }



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    //Pasamos los datos al DetailsActivity

                    Intent intent = new Intent(ctx, DetailsActivity.class);
                    intent.putExtra("imgNasaSelected", holder.mItem.getUrl());
                    intent.putExtra("titleNasaSelected", holder.mItem.getTitle());
                    intent.putExtra("explanationNasaSelected", holder.mItem.getExplanation());
                    ctx.startActivity(intent);

                    if(holder.mItem.getUrl().contains("https://www.youtube.com/")){
                        Intent youtubeWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.mItem.getUrl()));
                        ctx.startActivity(youtubeWebIntent);

                    }
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageHistory;
        public final ProgressBar progressBar;
        //public final TextView date;

        public NasaImage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageHistory = view.findViewById(R.id.imageViewPictureFragment);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
