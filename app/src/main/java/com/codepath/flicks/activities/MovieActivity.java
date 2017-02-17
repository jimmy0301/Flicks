package com.codepath.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flicks.R;
import com.codepath.flicks.adapter.MovieAdapter;
import com.codepath.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

   private Handler handler;
   private final int HOT = 0;
   private final int NORMAL = 1;
   private final int REQUEST_SUCCESS = 200;
   private final int REQUEST_FAILED = 500;

   ArrayList<Movie> movieArrayList;
   MovieAdapter movieAdapter;
   @Nullable @BindView(R.id.lvMovies) ListView lvItems;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_movie);
      ButterKnife.bind(this);

      String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
      movieArrayList = new ArrayList<>();
      lvItems.setAdapter(movieAdapter);

      handler = new myHandler();
      Thread t = new NetworkThread(url);
      t.start();

      setupListViewListener();

      Log.d("movie object", "movie object: " + movieArrayList.size());
   }

   private void setupListViewListener() {
      /* one click listener */
      lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {
            Movie MovieItem = movieAdapter.getItem(pos);

            Log.d("item click", "click");

            Bundle args = new Bundle();
            args.putInt("id", MovieItem.getId());
            args.putDouble("rating", MovieItem.getVote_avg());
            args.putString("releaseDate", MovieItem.getReleaseDate());
            args.putString("overview", MovieItem.getOverview());
            args.putString("title", MovieItem.getTitle());

            Intent i = new Intent(MovieActivity.this, MovieDetailActivity.class);
            i.putExtra("movieDetail", args);
            startActivity(i);

         }
      });
   }

   /*private String getVideoKey(int id) throws IOException, JSONException {
      String videoKey = null;
      String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder()
                            .url(url)
                            .build();


      Response response = client.newCall(request).execute();
      String responseData = response.body().string();
      JSONObject jsonObj = new JSONObject(responseData);
      JSONArray jsonArray = jsonObj.getJSONArray("results");
      videoKey = jsonArray.getJSONObject(0).getString("key");

      return videoKey;
   }*/

   class myHandler extends Handler{

      @Override
      public void handleMessage(Message msg){
         switch (msg.what) {
            case REQUEST_SUCCESS:
               Bundle b = msg.getData();
               String responseData = b.getString("result");
               Log.d("request success", responseData);
               try {
                  JSONObject json = new JSONObject(responseData);
                  JSONArray movieResults = json.getJSONArray("results");
                  movieArrayList.addAll(Movie.fromJsonArray(movieResults));
                  movieAdapter = new MovieAdapter(MovieActivity.this, movieArrayList);
                  lvItems.setAdapter(movieAdapter);
               } catch (JSONException e) {
                  e.printStackTrace();
               }
               break;
            case REQUEST_FAILED:
               Log.d("handleMsg", "request failed");
               break;
            default:
               super.handleMessage(msg);
         }
      }
   }

   class NetworkThread extends Thread {

      int page;
      String url;

      public NetworkThread(String url) {
         this.url = url;
      }

      public NetworkThread(int page) {
         this.page = page;
      }

      @Override
      public void run(){

         Message msg = handler.obtainMessage();

         OkHttpClient client = new OkHttpClient();
         Request request = new Request.Builder().url(url).build();

         Call call = client.newCall(request);
         Response response = null;

         try {
            response = call.execute();
            if (!response.isSuccessful()) {
               msg.what = REQUEST_FAILED;
            } else {
               msg.what = REQUEST_SUCCESS;
            }
         } catch (IOException e) {
            msg.what = REQUEST_FAILED;
            e.printStackTrace();
         } finally {
            String result = null;
            try {
               result = response.body().string();
            } catch (IOException e) {
               e.printStackTrace();
            }
            Log.d("get request", result);
            Bundle b = new Bundle();
            b.putString("result", result);
            msg.setData(b);
            handler.sendMessage(msg);
         }
      }
   }

   /*private void getMovieList() {
      Log.d("getMovieList", "get movie");
      handler.post(new Runnable() {
         @Override
         public void run() {
            Message msg = handler.obtainMessage();

            String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            Call call = client.newCall(request);
            Response response = null;

            try {
               response = call.execute();
               if (!response.isSuccessful()) {
                  msg.what = REQUEST_FAILED;
               } else {
                  msg.what = REQUEST_SUCCESS;
               }
            } catch (IOException e) {
               msg.what = REQUEST_FAILED;
               e.printStackTrace();
            } finally {
               String result = null;
               try {
                  result = response.body().string();
               } catch (IOException e) {
                  e.printStackTrace();
               }
               Log.d("get request", result);
               Bundle b = new Bundle();
               b.putString("result", result);
               msg.setData(b);
               handler.sendMessage(msg);
            }
         }
      });
   }*/
}

