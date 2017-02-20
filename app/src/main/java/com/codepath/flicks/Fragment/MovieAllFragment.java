package com.codepath.flicks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.activities.MovieActivity;
import com.codepath.flicks.activities.MovieDetailActivity;
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

import static android.R.attr.id;

/**
 * Created by keyulun on 2017/2/18.
 */

public class MovieAllFragment extends Fragment {

   private Handler handler;
   private final int REQUEST_SUCCESS = 200;
   private final int REQUEST_FAILED = 500;

   ArrayList<Movie> movieArrayList;
   MovieAdapter movieAdapter;
   @Nullable @BindView(R.id.lvMovies) ListView lvItems;

   public static MovieAllFragment newInstance() {
      MovieAllFragment fragment = new MovieAllFragment();
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

      initList();

      handler = new myHandler();
      Thread t = new NetworkThread(url);
      t.start();
   }

   // Inflate the fragment layout we defined above for this fragment
   // Set the associated text for the title
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

      ButterKnife.bind(this, view);
      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

      setupListViewListener();
   }

   private void initList() {
      movieArrayList = new ArrayList<>();
   }

   private void setupListViewListener() {
      /* one click listener */
      lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {
            Movie MovieItem = movieAdapter.getItem(pos);

            Bundle args = new Bundle();
            args.putLong("id", MovieItem.getId());
            args.putDouble("rating", MovieItem.getVote_avg());
            args.putString("releaseDate", MovieItem.getReleaseDate());
            args.putString("overview", MovieItem.getOverview());
            args.putString("title", MovieItem.getTitle());

            Intent i = new Intent(getActivity(), MovieDetailActivity.class);
            i.putExtra("movieDetail", args);
            startActivity(i);

         }
      });

      /* long click listener */
      lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

         @Override
         public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
            showAlertDiaolog(movieAdapter.getItem(pos));
            return true;
         }

         private void showAlertDiaolog(Movie movieItem) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AddFavoriteFragment alertDialog = AddFavoriteFragment.newInstance(movieItem);
            alertDialog.show(fm, "fragment_alert");
         }
      });
   }

   class myHandler extends Handler{

      @Override
      public void handleMessage(Message msg){
         switch (msg.what) {
            case REQUEST_SUCCESS:
               Bundle b = msg.getData();
               String responseData = b.getString("result");

               try {
                  JSONObject json = new JSONObject(responseData);
                  JSONArray movieResults = json.getJSONArray("results");
                  movieArrayList.addAll(movieArrayList.size(), Movie.fromJsonArray(movieResults));
                  movieAdapter = new MovieAdapter(getContext(), movieArrayList);
                  lvItems.setAdapter(movieAdapter);
                  movieAdapter.notifyDataSetChanged();
               } catch (JSONException e) {
                  e.printStackTrace();
               }
               break;
            case REQUEST_FAILED:
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
            Bundle b = new Bundle();
            b.putString("result", result);
            msg.setData(b);
            handler.sendMessage(msg);
         }
      }
   }
}
