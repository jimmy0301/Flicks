package com.codepath.flicks.activities;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

public class PlayVideoActivity extends YouTubeBaseActivity {

   @Nullable
   @BindView(R.id.player) YouTubePlayerView youTubePlayerView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_play_video);

      ButterKnife.bind(this);

      Bundle b = getIntent().getBundleExtra("movieDetail");

      int id = getIntent().getIntExtra("id", 0);

      String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

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
               Log.d("youtube key", getResources().getString(R.string.youtube_key));
               youTubePlayerView.initialize(getResources().getString(R.string.youtube_key),
                       new YouTubePlayer.OnInitializedListener() {
                          @Override
                          public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                              YouTubePlayer youTubePlayer, boolean b) {

                             // do any work here to cue video, play video, etc.
                             youTubePlayer.loadVideo(s);
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
