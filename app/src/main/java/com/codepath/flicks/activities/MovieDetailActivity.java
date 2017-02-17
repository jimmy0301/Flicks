package com.codepath.flicks.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MovieDetailActivity extends YouTubeBaseActivity {

   @Nullable @BindView(R.id.tvDetailTitle) TextView tvTitle;
   @Nullable @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
   @Nullable @BindView(R.id.tvDetailOverview) TextView tvOverview;
   @Nullable @BindView(R.id.ratingBar) RatingBar ratingBar;
   @Nullable @BindView(R.id.player) YouTubePlayerView youTubePlayerView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_movie_detail);
      ButterKnife.bind(this);

      Bundle b = getIntent().getBundleExtra("movieDetail");

      int id = b.getInt("id");
      Double rating = b.getDouble("rating");
      String releaseDate = b.getString("releaseDate");
      String overview = b.getString("overview");
      String title = b.getString("title");
      String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

      tvReleaseDate.setText("Release Date: " + releaseDate);
      tvTitle.setText(title);
      tvOverview.setText(overview);
      Log.d("detail", "the rating: " + rating);
      ratingBar.setNumStars(((int) Math.ceil(rating)));
      ratingBar.setRating(rating.floatValue());

      final OkHttpClient client = new OkHttpClient();

      final Request request = new Request.Builder()
              .url(url)
              .build();

      AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
         @Override
         protected String doInBackground(Void... params) {

            String videoKey = null;
            try {
               Response response = client.newCall(request).execute();
               if (!response.isSuccessful()) {
                  return null;
               }
               String responseData = response.body().string();
               JSONObject jsonObj = new JSONObject(responseData);
               JSONArray jsonArray = jsonObj.getJSONArray("results");
               videoKey = jsonArray.getJSONObject(0).getString("key");

               return videoKey;
            } catch (Exception e) {
               e.printStackTrace();
               return null;
            }
         }

         @Override
         protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            if (s != null) {
               youTubePlayerView.initialize(getResources().getString(R.string.youtube_key),
                       new YouTubePlayer.OnInitializedListener() {
                          @Override
                          public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                              YouTubePlayer youTubePlayer, boolean b) {

                             // do any work here to cue video, play video, etc.
                             youTubePlayer.cueVideo(s);
                          }
                          @Override
                          public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                              YouTubeInitializationResult youTubeInitializationResult) {

                          }
                       });

            }
         }
      };

      asyncTask.execute();
   }
}
