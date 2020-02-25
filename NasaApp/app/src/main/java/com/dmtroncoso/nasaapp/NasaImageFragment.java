package com.dmtroncoso.nasaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NasaImageFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    MyNasaImageRecyclerViewAdapter adapter;
    List<NasaImage> listPicture;

    Boolean isScrolling = true;
    int currentItems, totalItems, scrollOutItems;
    int countScroll = 0;

    int countFirst = 0;
    int countSecond = 1;

    LocalDate date = LocalDate.now();
    LocalDate dateMinusMonth = date.minusMonths(1);

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NasaImageFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NasaImageFragment newInstance(int columnCount) {
        NasaImageFragment fragment = new NasaImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nasaimage_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            new NasadateIntervalAsync().execute();

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(NasaImage item);
    }

    private class NasadateIntervalAsync extends AsyncTask<List<NasaImage>, Void, List<NasaImage>>{

        NasaApi nasaApi = new NasaApi("NryfCSMeS824H1J2wQ8PLIsy9Uj8H0cE3ZrW1CQ4");

        @Override
        protected List<NasaImage> doInBackground(List<NasaImage>... lists) {

            listPicture = nasaApi.getPicOfDateInterval(dateMinusMonth.toString(), date.toString());
            Collections.reverse(listPicture);

            return listPicture;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Downloading Images ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(List<NasaImage> nasaImages) {

            adapter = new MyNasaImageRecyclerViewAdapter(nasaImages, mListener);
            recyclerView.setAdapter(adapter);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                        isScrolling = true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems = recyclerView.getLayoutManager().getChildCount();
                    totalItems = recyclerView.getLayoutManager().getItemCount();
                    scrollOutItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        isScrolling = false;
                        countFirst = countFirst++;
                        countSecond = countSecond++;

                        date = dateMinusMonth.minusMonths(countFirst).minusDays(1);
                        dateMinusMonth = dateMinusMonth.minusMonths(countSecond);
                        Toast.makeText(context, "Loading..", Toast.LENGTH_SHORT).show();

                        new NasadateLoadDateAsync().execute();

                    }
                }
                });
            Toast.makeText(context, "First page is loaded..", Toast.LENGTH_SHORT).show();
        }

    }

    private class NasadateLoadDateAsync extends AsyncTask<List<NasaImage>, Void, List<NasaImage>>{

        NasaApi nasaApi = new NasaApi("NryfCSMeS824H1J2wQ8PLIsy9Uj8H0cE3ZrW1CQ4");

        @Override
        protected List<NasaImage> doInBackground(List<NasaImage>... lists) {

            return nasaApi.getPicOfDateInterval(dateMinusMonth.toString(), date.toString());
        }


        @Override
        protected void onPostExecute(List<NasaImage> nasaImages) {

            Collections.reverse(nasaImages);

            listPicture.addAll(nasaImages);
            adapter.notifyDataSetChanged();
            Toast.makeText(context, "Loaded", Toast.LENGTH_SHORT).show();
        }

    }
}
