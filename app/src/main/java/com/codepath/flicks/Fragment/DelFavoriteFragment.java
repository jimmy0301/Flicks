package com.codepath.flicks.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.Toast;

import com.codepath.flicks.R;
import com.codepath.flicks.models.FavoriteMovieTable;
import com.codepath.flicks.models.Movie;

/**
 * Created by keyulun on 2017/2/19.
 */

public class DelFavoriteFragment extends DialogFragment {
   public static DelFavoriteFragment newInstance(Movie movieItem) {
      DelFavoriteFragment frag = new DelFavoriteFragment();
      Bundle args = new Bundle();
      args.putLong("id", movieItem.getId());
      args.putString("releaseData", movieItem.getReleaseDate());
      args.putDouble("rating", movieItem.getVote_avg());
      args.putString("title", movieItem.getTitle());
      args.putString("overview", movieItem.getOverview());
      args.putString("image", movieItem.getPosterPath());

      frag.setArguments(args);
      return frag;
   }

   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      Dialog dialog = super.onCreateDialog(savedInstanceState);
      final String imagePath = getArguments().getString("image");
      final String movieTitle = getArguments().getString("title");
      final Double rating = getArguments().getDouble("rating");
      final String releaseDate = getArguments().getString("releaseDate");
      final String movieOverview = getArguments().getString("overview");
      final Long id = getArguments().getLong("id", 0);

      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
      dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

      String message = "Are you sure to delete the movie " + "\"" + movieTitle + "\" to favorite" + "?";

      alertDialogBuilder.setMessage(message);
      alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            // on success
            FavoriteMovieTable favoriteMovie = new FavoriteMovieTable();
            favoriteMovie.setId(id);
            favoriteMovie.setVote_avg(rating);
            favoriteMovie.setReleaseDate(releaseDate);
            favoriteMovie.setTitle(movieTitle);
            favoriteMovie.setOverview(movieOverview);
            favoriteMovie.setImagePath(imagePath);
            favoriteMovie.delete();

            //dialog.dismiss();
            Toast toast = Toast.makeText(getContext(), "Delete \"" + movieTitle + "\"" + "successfully", Toast.LENGTH_LONG);
            toast.show();

         }
      });
      alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
         }
      });
      return alertDialogBuilder.create();
   }
}
