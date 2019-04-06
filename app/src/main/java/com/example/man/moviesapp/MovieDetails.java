package com.example.man.moviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieDetails extends AppCompatActivity {


    ProgressBar spinner;
    SQLiteDatabase myDatabase;
    ToggleButton buttonFavorite;
    ScaleAnimation scaleAnimation;
    BounceInterpolator bounceInterpolator;

    ArrayList<String> characterName = new ArrayList<>();
    ArrayList<String> actorName = new ArrayList<>();
    ArrayList<String> profilepic = new ArrayList<>();

    ArrayList<String> keywords = new ArrayList<>();

    ArrayList<String> similarMovie = new ArrayList<>();
    ArrayList<String> similarPoster = new ArrayList<>();
    ArrayList<Float> similarRating = new ArrayList<>();

    int movieID;
    String moviePic;
    String movieName;
    float movieRating;
    String movieDate;
    ArrayList<String> genres;
    String overview;

    ImageView movieDetailImage;
    TextView MovieDetailMovieNameTextView;
    TextView MovieDetailRating;
    TextView MovieDetailReleaseDate;
    TextView MovieDetailGenre;
    TextView MovieDetailKeywords;
    TextView MovieDetailOverview;

    ImageView MovieDetailCast1Image;
    TextView MovieDetailCast1Character;
    TextView MovieDetailCast1Name;

    ImageView MovieDetailCast2Image;
    TextView MovieDetailCast2Character;
    TextView MovieDetailCast2Name;

    ImageView MovieDetailCast3Image;
    TextView MovieDetailCast3Character;
    TextView MovieDetailCast3Name;

    ImageView MovieDetailCast4Image;
    TextView MovieDetailCast4Character;
    TextView MovieDetailCast4Name;

    ImageView MovieDetailsimilar1Image;
    TextView MovieDetailsimilar1Movie;
    TextView MovieDetailsimilar1Rating;

    ImageView MovieDetailsimilar2Image;
    TextView MovieDetailsimilar2Movie;
    TextView MovieDetailsimilar2Rating;

    ImageView MovieDetailsimilar3Image;
    TextView MovieDetailsimilar3Movie;
    TextView MovieDetailsimilar3Rating;

    ImageView MovieDetailsimilar4Image;
    TextView MovieDetailsimilar4Movie;
    TextView MovieDetailsimilar4Rating;


    LinearLayout MovieDetailsLinearLayout;
    LinearLayout overViewLinearLayout;
    LinearLayout castWordLinearLayout;
    LinearLayout castLinearLayout;
    LinearLayout similarWorldLinearLayout;
    LinearLayout similarLinearLayout;
    View firstView;
    View secondView;
    View thirdView;


    @Override
    protected void onResume() {

        Intent intent = getIntent();
        movieID = intent.getIntExtra("movieID", 0);

        DownloadTask task = new DownloadTask();
        String result = null;

        task.execute("https://api.themoviedb.org/3/movie/" + movieID + "/credits?api_key=3334807660ce77b344b26c355513e53d");
        //        Log.i("Contents Of URL", result);

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spinner = findViewById(R.id.progressBarMovieDetails);

        spinner.setVisibility(View.VISIBLE);
    }

    //back button
    @Override
    public boolean onSupportNavigateUp() {
//        startActivity( new Intent(getApplicationContext(),MainActivity.class));
        onBackPressed();



        return true;
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

            buttonFavorite = findViewById(R.id.button_favorite);
            spinner.setVisibility(View.VISIBLE);

            MovieDetailsLinearLayout = findViewById(R.id.MovieDetailsLinearLayout);
            overViewLinearLayout = findViewById(R.id.overViewLinearLayout);
            castWordLinearLayout = findViewById(R.id.castWordLinearLayout);
            castLinearLayout = findViewById(R.id.castLinearLayout);
            similarWorldLinearLayout = findViewById(R.id.similarWorldLinearLayout);
            similarLinearLayout = findViewById(R.id.similarLinearLayout);
            firstView = findViewById(R.id.firstView);
            secondView = findViewById(R.id.secondView);
            thirdView = findViewById(R.id.thirdView);

            MovieDetailsLinearLayout.setVisibility(View.INVISIBLE);
            overViewLinearLayout.setVisibility(View.INVISIBLE);
            castWordLinearLayout.setVisibility(View.INVISIBLE);
            castLinearLayout.setVisibility(View.INVISIBLE);
            similarWorldLinearLayout.setVisibility(View.INVISIBLE);
            similarLinearLayout.setVisibility(View.INVISIBLE);
            firstView.setVisibility(View.INVISIBLE);
            secondView.setVisibility(View.INVISIBLE);
            thirdView.setVisibility(View.INVISIBLE);


            myDatabase = openOrCreateDatabase("Movies", MODE_PRIVATE, null);
//        myDatabase.execSQL("delete from "+ "movies");

            Cursor c = myDatabase.rawQuery("SELECT movie_id FROM moviesdatabase", null);
            ArrayList<Integer> likedMovies = new ArrayList<>();
            int movie_id = c.getColumnIndex("movie_id");
            if (c.moveToFirst()) {
                do {
                    likedMovies.add(c.getInt(movie_id));
                }
                while (c.moveToNext());
            }

            if (likedMovies.contains(movieID)) {
                buttonFavorite.setChecked(true);
            }

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            movieDetailImage = findViewById(R.id.movieDetailImage);
            MovieDetailMovieNameTextView = findViewById(R.id.MovieDetailMovieNameTextView);
            MovieDetailRating = findViewById(R.id.MovieDetailRating);
            MovieDetailReleaseDate = findViewById(R.id.MovieDetailReleaseDate);
            MovieDetailGenre = findViewById(R.id.MovieDetailGenre);
            MovieDetailKeywords = findViewById(R.id.MovieDetailKeywords);
            MovieDetailOverview = findViewById(R.id.MovieDetailOverview);
            MovieDetailCast1Image = findViewById(R.id.MovieDetailCast1Image);
            MovieDetailCast1Character = findViewById(R.id.MovieDetailCast1Character);
            MovieDetailCast1Name = findViewById(R.id.MovieDetailCast1Name);
            MovieDetailCast2Image = findViewById(R.id.MovieDetailCast2Image);
            MovieDetailCast2Character = findViewById(R.id.MovieDetailCast2Character);
            MovieDetailCast2Name = findViewById(R.id.MovieDetailCast2Name);
            MovieDetailCast3Image = findViewById(R.id.MovieDetailCast3Image);
            MovieDetailCast3Character = findViewById(R.id.MovieDetailCast3Character);
            MovieDetailCast3Name = findViewById(R.id.MovieDetailCast3Name);
            MovieDetailCast4Image = findViewById(R.id.MovieDetailCast4Image);
            MovieDetailCast4Character = findViewById(R.id.MovieDetailCast4Character);
            MovieDetailCast4Name = findViewById(R.id.MovieDetailCast4Name);
            MovieDetailsimilar1Image = findViewById(R.id.MovieDetailsimilar1Image);
            MovieDetailsimilar1Movie = findViewById(R.id.MovieDetailsimilar1Movie);
            MovieDetailsimilar1Rating = findViewById(R.id.MovieDetailsimilar1Rating);
            MovieDetailsimilar2Image = findViewById(R.id.MovieDetailsimilar2Image);
            MovieDetailsimilar2Movie = findViewById(R.id.MovieDetailsimilar2Movie);
            MovieDetailsimilar2Rating = findViewById(R.id.MovieDetailsimilar2Rating);
            MovieDetailsimilar3Image = findViewById(R.id.MovieDetailsimilar3Image);
            MovieDetailsimilar3Movie = findViewById(R.id.MovieDetailsimilar3Movie);
            MovieDetailsimilar3Rating = findViewById(R.id.MovieDetailsimilar3Rating);
            MovieDetailsimilar4Image = findViewById(R.id.MovieDetailsimilar4Image);
            MovieDetailsimilar4Movie = findViewById(R.id.MovieDetailsimilar4Movie);
            MovieDetailsimilar4Rating = findViewById(R.id.MovieDetailsimilar4Rating);


            Intent intent = getIntent();
            movieID = intent.getIntExtra("movieID", 0);
            moviePic = intent.getStringExtra("moviePoster");
            movieName = intent.getStringExtra("movieTitle");
            movieRating = intent.getFloatExtra("movieRating", 0);
            movieDate = intent.getStringExtra("date");
            overview = intent.getStringExtra("overview");
            genres = intent.getStringArrayListExtra("genre");

            // display image.
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
            ImageLoader.getInstance().init(config); // Do it on Application start


            Log.i("before reading" , "before readingfrom the web");

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                //cast

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }


                Log.i("Read the data" , "Successfully");
                JSONObject parentObject = new JSONObject(result);
                JSONArray parentArray = parentObject.getJSONArray("cast");

                for (int i = 0; i < 4; i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    characterName.add(finalObject.getString("character"));
                    actorName.add(finalObject.getString("name"));
                    profilepic.add(finalObject.getString("profile_path"));
                }


                Log.i("finished setting data" , "data is set to the arrayLists");
//                Log.i("character name" , characterName.toString());
//                Log.i("actor name" , actorName.toString());
//                Log.i("profile picture" , profilepic.toString());


                // keywords.

                url = new URL("https://api.themoviedb.org/3/movie/"+movieID+"/keywords?api_key=3334807660ce77b344b26c355513e53d");
                urlConnection = (HttpURLConnection) url.openConnection();
                in = urlConnection.getInputStream();
                reader = new InputStreamReader(in);
                data = reader.read();
                String keywordInfo = "";

                while (data != -1 ) {
                    char current = (char) data;
                    keywordInfo += current;
                    data = reader.read();
                }

                JSONObject parentObjectKeyword = new JSONObject(keywordInfo);
                JSONArray parentArrayKeyword = parentObjectKeyword.getJSONArray("keywords");
                for (int i =0; i< parentArrayKeyword.length();i++){
                    JSONObject finalObject = parentArrayKeyword.getJSONObject(i);
                    keywords.add(finalObject.getString("name"));
                }

//                Log.i("Keywords" , keywords.toString());


                // similar movies.

                url = new URL("https://api.themoviedb.org/3/movie/"+ movieID +"/similar?api_key=3334807660ce77b344b26c355513e53d&language=en-US&page=1");
                urlConnection = (HttpURLConnection) url.openConnection();
                in = urlConnection.getInputStream();
                reader = new InputStreamReader(in);
                data = reader.read();
                String similarMovieInfo = "";

                while (data != -1 ) {
                    char current = (char) data;
                    similarMovieInfo += current;
                    data = reader.read();
                }

                JSONObject parentObjectSimilar = new JSONObject(similarMovieInfo);
                JSONArray parentArraysimilar = parentObjectSimilar.getJSONArray("results");
                for (int i =0; i< 4;i++){
                    JSONObject finalObject = parentArraysimilar.getJSONObject(i);
                    similarMovie.add(finalObject.getString("title"));
                    similarPoster.add(finalObject.getString("poster_path"));
                    similarRating.add((float) finalObject.getDouble("vote_average"));
                }

//                Log.i("similarMovie" , similarMovie.toString());
//                Log.i("similarPoster" , similarPoster.toString());
//                Log.i("similarRating" , similarRating.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // set all the data to the views.
            super.onPostExecute(s);

            spinner.setVisibility(View.GONE);
            MovieDetailsLinearLayout.setVisibility(View.VISIBLE);
            overViewLinearLayout.setVisibility(View.VISIBLE);
            castWordLinearLayout.setVisibility(View.VISIBLE);
            castLinearLayout.setVisibility(View.VISIBLE);
            similarWorldLinearLayout.setVisibility(View.VISIBLE);
            similarLinearLayout.setVisibility(View.VISIBLE);
            firstView.setVisibility(View.VISIBLE);
            secondView.setVisibility(View.VISIBLE);
            thirdView.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + moviePic, movieDetailImage);
            MovieDetailMovieNameTextView.setText(movieName);
            MovieDetailRating.setText(movieRating + "/10");
            MovieDetailReleaseDate.setText(movieDate);
            MovieDetailGenre.setText(genres.toString());
            MovieDetailKeywords.setText("Keywords: " + keywords.toString());
            MovieDetailOverview.setText(overview);

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + profilepic.get(0), MovieDetailCast1Image);
            MovieDetailCast1Character.setText(characterName.get(0));
            MovieDetailCast1Name.setText(actorName.get(0));

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + profilepic.get(1), MovieDetailCast2Image);
            MovieDetailCast2Character.setText(characterName.get(1));
            MovieDetailCast2Name.setText(actorName.get(1));

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + profilepic.get(2), MovieDetailCast3Image);
            MovieDetailCast3Character.setText(characterName.get(2));
            MovieDetailCast3Name.setText(actorName.get(2));

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + profilepic.get(3), MovieDetailCast4Image);
            MovieDetailCast4Character.setText(characterName.get(3));
            MovieDetailCast4Name.setText(actorName.get(3));


            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + similarPoster.get(0), MovieDetailsimilar1Image);
            MovieDetailsimilar1Movie.setText(similarMovie.get(0));
            MovieDetailsimilar1Rating.setText(similarRating.get(0) + "");

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + similarPoster.get(1), MovieDetailsimilar2Image);
            MovieDetailsimilar2Movie.setText(similarMovie.get(1));
            MovieDetailsimilar2Rating.setText(similarRating.get(1) + "");

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + similarPoster.get(2), MovieDetailsimilar3Image);
            MovieDetailsimilar3Movie.setText(similarMovie.get(2));
            MovieDetailsimilar3Rating.setText(similarRating.get(2) + "");

            ImageLoader.getInstance().displayImage("http://image.tmdb.org/t/p/w185" + similarPoster.get(3), MovieDetailsimilar4Image);
            MovieDetailsimilar4Movie.setText(similarMovie.get(3));
            MovieDetailsimilar4Rating.setText(similarRating.get(3) + "");



            scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(500);
            bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);
            buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    //animation
                    compoundButton.startAnimation(scaleAnimation);

                    String movieNameFixed = movieName.replace("'","");
                    String overviewFixed = overview.replace("'", "");

                    if (isChecked){
                        myDatabase.execSQL("INSERT INTO moviesdatabase (movie_id,movie_name,rating,movie_pic,movie_date,overview,genres) VALUES ( " + movieID  + ", '" + movieNameFixed + "'" + ", '" + movieRating  + "'" + ", '" + moviePic  + "'" + ", '" + movieDate  + "'" + ", '" + overviewFixed  + "'" + ", '" +genres.toString() +"' )");
                        Log.i("database" , "suppose to be inserted successfully");
                    }else {
                        myDatabase.execSQL("DELETE FROM moviesdatabase WHERE movie_id = " + movieID );
                        Log.i("database" , "suppose to be deleted successfully");
                    }

                }});



        }

    }

}

