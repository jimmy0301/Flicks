package com.codepath.flicks.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.flicks.R;
import com.codepath.flicks.activities.MovieActivity;
import com.codepath.flicks.activities.PlayVideoActivity;
import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


/**
 * Created by keyulun on 2017/2/9.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

   private int orientation;
   private Context context;
   private final int HOT = 0;
   private final int NORMAL = 1;
   private ViewHolderHot viewHolderHot;
   private ViewHolderNormal viewHolder;

   static class ViewHolderNormal {

      @Nullable @BindView(R.id.ivmovieimage) ImageView ivImage;
      @Nullable @BindView(R.id.tvTitle) TextView tvTitle;
      @Nullable @BindView(R.id.tvOverview) TextView tvOverview;

      public ViewHolderNormal(View view) {

         ButterKnife.bind(this, view);
      }
   }

   static class ViewHolderHot {
      @Nullable @BindView(R.id.ivmovieimage) ImageView ivImage;
      @Nullable @BindView(R.id.VideoPreviewPlayButton) ImageView ivButton;

      public ViewHolderHot(View view) {
         ButterKnife.bind(this, view);
      }
   }

   public MovieAdapter(@NonNull Context context, List<Movie> movies) {
      super(context, android.R.layout.simple_list_item_1, movies);
      orientation = context.getResources().getConfiguration().orientation;
      this.context = context;
   }

   @Override
   public int getViewTypeCount() {
      return 2;
   }

   @Override
   public int getItemViewType(int position) {
      Double vote_average = getItem(position).getVote_avg();

      if (vote_average > 5.0f) {
         return HOT;
      }
      else {
         return NORMAL;
      }
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

      Movie movie = getItem(position);
      int type = getItemViewType(position);

      switch (type) {
         case HOT:

            if (convertView == null) {

               convertView = getInflatedLayoutForType(type);
               viewHolderHot = new ViewHolderHot(convertView);

               convertView.setTag(viewHolderHot);
            }
            else {
               viewHolderHot = (ViewHolderHot) convertView.getTag();
            }

            viewHolderHot.ivImage.setImageResource(0);


            viewHolderHot.ivButton.setOnClickListener(new IvButtonListener(position));

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
               Picasso.with(getContext()).load(movie.getBackdropPath())
                       .transform(new RoundedCornersTransformation(10, 10)).fit().centerInside()
                       .placeholder(R.drawable.placeholder).error(R.drawable.error48).into(viewHolderHot.ivImage);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
               Picasso.with(getContext()).load(movie.getBackdropPath())
                       .transform(new RoundedCornersTransformation(10, 10)).resize(300, 500).centerInside()
                       .placeholder(R.drawable.placeholder).error(R.drawable.error48).into(viewHolderHot.ivImage);
            }

            viewHolderHot.ivButton.setVisibility(View.VISIBLE);

            return convertView;
         case NORMAL:

            if (convertView == null) {

               convertView = getInflatedLayoutForType(type);
               viewHolder = new ViewHolderNormal(convertView);

               convertView.setTag(viewHolder);
            }
            else {
               viewHolder = (ViewHolderNormal) convertView.getTag();
            }

            viewHolder.ivImage.setImageResource(0);

            viewHolder.tvTitle.setText(movie.getTitle());
            viewHolder.tvOverview.setText(movie.getOverview());

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
               Picasso.with(getContext()).load(movie.getPosterPath())
                       .transform(new RoundedCornersTransformation(10, 10)).resize(350, 500).centerCrop()
                       .placeholder(R.drawable.placeholder).error(R.drawable.error48).into(viewHolder.ivImage);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
               Picasso.with(getContext()).load(movie.getBackdropPath())
                       .transform(new RoundedCornersTransformation(10, 10)).resize(500, 350).centerCrop()
                       .placeholder(R.drawable.placeholder).error(R.drawable.error48).into(viewHolder.ivImage);
            }

            return convertView;
         default:
            return null;
      }
   }

   class IvButtonListener implements View.OnClickListener {
      private int position;

      IvButtonListener(int pos) {
         this.position = pos;
      }

      @Override
      public void onClick(View v) {
         int vid = v.getId();

         if (vid == viewHolderHot.ivButton.getId()) {
            Movie movieItem = getItem(position);
            Log.d("image", "click the image");

            Intent i = new Intent(context, PlayVideoActivity.class);
            i.putExtra("id", movieItem.getId());
            context.startActivity(i);
         }
      }
   }

   private View getInflatedLayoutForType(int type) {
      Log.d("View type", "type: " + type);
      if (type == HOT) {
         return LayoutInflater.from(getContext()).inflate(R.layout.item_hot_movie, null);

      }
      else if (type == NORMAL){
         return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
      }
      else {
         return null;
      }
   }

}
