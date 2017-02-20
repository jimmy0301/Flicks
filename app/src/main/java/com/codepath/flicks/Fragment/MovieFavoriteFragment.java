package com.codepath.flicks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flicks.R;
import com.codepath.flicks.activities.MovieDetailActivity;
import com.codepath.flicks.adapter.MovieFavoriteAdapter;
import com.codepath.flicks.models.FavoriteMovieTable;
import com.codepath.flicks.models.Movie;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;

/**
 * Created by keyulun on 2017/2/19.
 */

public class MovieFavoriteFragment extends Fragment {
   MovieFavoriteAdapter movieAdapter;
   ArrayList<Movie> movieLists;
   List<FavoriteMovieTable> movieListFromDB;
   @Nullable @BindView(R.id.lvMovies) ListView lvItems;

   public static MovieFavoriteFragment newInstance() {
      MovieFavoriteFragment fragment = new MovieFavoriteFragment();
      return fragment;
   }

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_movie_favorite, container, false);

      ButterKnife.bind(this, view);
      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
      initList();
   }

   private void initList() {
      readMovieFromDB();
      movieAdapter = new MovieFavoriteAdapter(getContext(), movieLists);
      lvItems.setAdapter(movieAdapter);

      //movieAdapter.addAll(readMovieFromDB());
      setupListViewListener();
   }

   /*public reload(){
      movieAdapter.clear();
      movieAdapter.addAll(readMovieFromDB());
   }*/

   private void setupListViewListener() {
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
            DelFavoriteFragment alertDialog = DelFavoriteFragment.newInstance(movieItem);
            alertDialog.show(fm, "fragment_alert");
         }
      });
   }

   private void readMovieFromDB() {
      int i;
      movieListFromDB = new Select().from(FavoriteMovieTable.class).queryList();
      movieLists = new ArrayList<Movie>();

      for (i = 0; i < movieListFromDB.size(); i++) {
         //add item to movieLists to make adapter show the list

         Long id = movieListFromDB.get(i).getId();
         String posterPath = movieListFromDB.get(i).getImagePath();
         String title = movieListFromDB.get(i).getTitle();
         String overview = movieListFromDB.get(i).getOverview();
         String releaseDate = movieListFromDB.get(i).getReleaseDate();
         Double rating = movieListFromDB.get(i).getVote_avg();


         Movie movieItem = new Movie(id, posterPath, overview,
                                     title, releaseDate, rating);

         movieLists.add(movieItem);

      }
   }

   public void update(Context context) {
      Log.d("update", "update fragment");
      readMovieFromDB();
      //movieAdapter.addAll(movieLists);
      //movieAdapter.addAll(movieLists);
      //movieAdapter.notifyDataSetChanged();
   }
}
