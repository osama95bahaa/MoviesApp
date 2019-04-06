package com.example.man.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NowPlayingFragment extends Fragment {

    ListView listView;
    ArrayList<Integer> movieID = new ArrayList<>();
    ArrayList<String> movieTitle = new ArrayList<>();
    ArrayList<Float> voteAverage = new ArrayList<>();
    ArrayList<String> overviewArrayList = new ArrayList<>();
    ArrayList<String> releaseDateArrayList = new ArrayList<>();
    ArrayList<String> movieImage = new ArrayList<>();
    ArrayList<ArrayList<String>> genre = new ArrayList<>();
    ProgressBar spinner;


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            // display image.
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
            ImageLoader.getInstance().init(config); // Do it on Application start


            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                JSONObject parentObject = new JSONObject(result);
                JSONArray parentArray = parentObject.getJSONArray("results");

                for (int i =0; i< parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    movieID.add(finalObject.getInt("id"));
                    movieTitle.add(finalObject.getString("title"));
                    voteAverage.add((float) finalObject.getDouble("vote_average"));
                    overviewArrayList.add(finalObject.getString("overview"));
                    releaseDateArrayList.add(finalObject.getString("release_date"));
                    movieImage.add(finalObject.getString("poster_path"));

                    ArrayList<String> genreObject = new ArrayList<>();
                    for (int j = 0; j< finalObject.getJSONArray("genre_ids").length();j++){
                        int x = (int) finalObject.getJSONArray("genre_ids").get(j);
                        genreObject.add(MainActivity.genreHashMap.get(x));
                    }
                    genre.add(genreObject);
                }
                return result;
            }
            catch(Exception e) {
                e.printStackTrace();
                return "Failed";

            }
        }

        @Override
        protected void onPostExecute(String s) {

            spinner.setVisibility(View.GONE);

            CustomAdapter customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);

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
                    intent.putExtra("Activity" , "NowPlayingFragment");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            super.onPostExecute(s);
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

        DownloadTask task = new DownloadTask();
        task.execute("https://api.themoviedb.org/3/movie/now_playing?api_key=3334807660ce77b344b26c355513e53d&language=en-US&page=1");

        super.onResume();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.now_playing_fragment, container, false);

        spinner = rootView.findViewById(R.id.progressBarNowPlaying);
        listView = rootView.findViewById(R.id.nowplayingListView);

        return rootView;
    }


    class CustomAdapter extends BaseAdapter{

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
            view = getLayoutInflater().inflate(R.layout.now_playing_row,null);

            TextView movieName = view.findViewById(R.id.nowPlayingMovieNameTextView);
            TextView rating = view.findViewById(R.id.nowPlayingRating);
            TextView releaseDate = view.findViewById(R.id.nowPlayingReleaseDate);
            TextView genreTextView = view.findViewById(R.id.nowPlayingGenre);
            ImageView img = view.findViewById(R.id.nowPlayingImage);

            movieName.setText(movieTitle.get(i));
            rating.setText(voteAverage.get(i) + "/10");
            releaseDate.setText(releaseDateArrayList.get(i));
            genreTextView.setText(genre.get(i).toString());

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + movieImage.get(i), img);

            return view;
        }
    }
}
