package com.example.man.moviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class LikedMoviesFragment extends Fragment {

    SQLiteDatabase myDatabase;
    ListView listView;
    FrameLayout frameLayout;

    ArrayList<Integer> movieID = new ArrayList<>();
    ArrayList<String> movieTitle = new ArrayList<>();
    ArrayList<Float> voteAverage = new ArrayList<>();
    ArrayList<String> overviewArrayList = new ArrayList<>();
    ArrayList<String> releaseDateArrayList = new ArrayList<>();
    ArrayList<String> movieImage = new ArrayList<>();
    ArrayList<ArrayList<String>> genre = new ArrayList<ArrayList<String>>();

    // check if there is network connection.
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // check if there is Internet connection.
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
        Log.i("visible to user" , isVisibleToUser+ "");
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onResume() {

        movieID.clear();
        movieTitle.clear();
        voteAverage.clear();
        releaseDateArrayList.clear();
        overviewArrayList.clear();
        genre.clear();
        movieImage.clear();

        Cursor c = myDatabase.rawQuery("SELECT * FROM moviesdatabase", null);

        int movie_name = c.getColumnIndex("movie_name");
        int rating = c.getColumnIndex("rating");
        int movie_id = c.getColumnIndex("movie_id");
        int overview = c.getColumnIndex("overview");
        int releaseDate = c.getColumnIndex("movie_date");
        int movie_pic = c.getColumnIndex("movie_pic");
        int genres = c.getColumnIndex("genres");

        if (c.moveToFirst()) {
            do {
                movieID.add(c.getInt(movie_id));
                movieTitle.add(c.getString(movie_name));
                voteAverage.add(c.getFloat(rating));
                overviewArrayList.add(c.getString(overview));
                releaseDateArrayList.add(c.getString(releaseDate));
                movieImage.add(c.getString(movie_pic));

                String retrievedGenres = c.getString(genres);
                String removeBracket1 = retrievedGenres.replace("[", "");
                String removeBracket2 = removeBracket1.replace("]", "");
                String[] genresArray = removeBracket2.split(",");

                Log.i("stringsSplitted", genresArray.toString());
                ArrayList<String> splittedGenres = new ArrayList<>();
                splittedGenres.addAll(Arrays.asList(genresArray));
                Log.i("ArrayList", splittedGenres.toString());
                genre.add(splittedGenres);
            }
            while (c.moveToNext());

            CustomAdapter adapter = new CustomAdapter();
            listView.setAdapter(adapter);
        }
        Log.i("Resume" , "Entered the resume method in the likedMovieFragment Class");
        Log.i("ResumeAgain" , movieTitle.toString());
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.liked_movie_fragment, container, false);

        movieID.clear();
        movieTitle.clear();
        voteAverage.clear();
        releaseDateArrayList.clear();
        overviewArrayList.clear();
        genre.clear();
        movieImage.clear();

        // display image.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        listView = rootView.findViewById(R.id.likedMoviesListView);

        myDatabase = getContext().openOrCreateDatabase("Movies", MODE_PRIVATE, null);

        Cursor c = myDatabase.rawQuery("SELECT * FROM moviesdatabase", null);

        int movie_name = c.getColumnIndex("movie_name");
        int rating = c.getColumnIndex("rating");
        int movie_id = c.getColumnIndex("movie_id");
        int overview = c.getColumnIndex("overview");
        int releaseDate = c.getColumnIndex("movie_date");
        int movie_pic = c.getColumnIndex("movie_pic");
        int genres = c.getColumnIndex("genres");

        if (c.moveToFirst()) {
            do {
                movieID.add(c.getInt(movie_id));
                movieTitle.add(c.getString(movie_name));
                voteAverage.add(c.getFloat(rating));
                overviewArrayList.add(c.getString(overview));
                releaseDateArrayList.add(c.getString(releaseDate));
                movieImage.add(c.getString(movie_pic));

                String retrievedGenres = c.getString(genres);
                String removeBracket1 = retrievedGenres.replace("[","");
                String removeBracket2 = removeBracket1.replace("]" , "");
                String[] genresArray = removeBracket2.split(",");

                Log.i("stringsSplitted" ,genresArray.toString());
                ArrayList<String> splittedGenres = new ArrayList<>();
                splittedGenres.addAll(Arrays.asList(genresArray));
                Log.i("ArrayList" ,splittedGenres.toString());
                genre.add(splittedGenres);
            }
            while (c.moveToNext());


            CustomAdapter adapter = new CustomAdapter();
            listView.setAdapter(adapter);
        }

        Log.i("OnCreate" , "Entered on create method in the likeMovieFragment Class");
        Log.i("OnCreateAgain" , movieTitle.toString());



        Log.i("network connectivity",isNetworkConnected() + "");
        Log.i("Internet connectivity",isInternetOn() + "");

        //check this issue it always says that there is no internet connection.
        if (isInternetOn()) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getContext(), MovieDetails.class);
                    intent.putExtra("movieID", movieID.get(i));
                    intent.putExtra("movieTitle", movieTitle.get(i));
                    intent.putExtra("movieRating", voteAverage.get(i));
                    intent.putExtra("overview", overviewArrayList.get(i));
                    intent.putExtra("date", releaseDateArrayList.get(i));
                    intent.putExtra("moviePoster", movieImage.get(i));
                    intent.putExtra("genre", genre.get(i));
                    intent.putExtra("Activity" , "LikeMoviesFragment");
                    startActivity(intent);
                }
            });
        }
        return rootView;
    }


    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return movieTitle.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.liked_movie_row,null);

            TextView movieName = view.findViewById(R.id.likedMovieNameTextView);
            TextView rating = view.findViewById(R.id.likedMovieRating);
            TextView releaseDate = view.findViewById(R.id.likedMovieReleaseDate);
            TextView genreTextView = view.findViewById(R.id.likedMovieGenre);
            ImageView img = view.findViewById(R.id.likedMovieImage);

            movieName.setText(movieTitle.get(i));
            rating.setText(voteAverage.get(i) + "/10");
            releaseDate.setText(releaseDateArrayList.get(i));
            genreTextView.setText(genre.get(i).toString());

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + movieImage.get(i), img);

            notifyDataSetChanged();

            return view;
        }
    }

}



