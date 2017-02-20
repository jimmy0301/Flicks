package com.codepath.flicks.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.R.id.list;

/**
 * Created by keyulun on 2017/2/19.
 */

public class MovieFavoriteAdapter extends ArrayAdapter<Movie> {
   private int orientation;
   private Context context;
   private ViewHolder viewHolder;

   static class ViewHolder {

      @Nullable
      @BindView(R.id.ivmovieimage) ImageView ivImage;
      @Nullable @BindView(R.id.tvTitle) TextView tvTitle;
      @Nullable @BindView(R.id.tvOverview) TextView tvOverview;

      public ViewHolder(View view) {

         ButterKnife.bind(this, view);
      }
   }

   public MovieFavoriteAdapter(@NonNull Context context, List<Movie> movies) {
      super(context, android.R.layout.simple_list_item_1, movies);
      orientation = context.getResources().getConfiguration().orientation;
      this.context = context;
   }


   @Override
   public int getCount() {
      return super.getCount();
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      Movie movie = getItem(position);

      if (convertView == null) {

         convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
         viewHolder = new ViewHolder(convertView);

         convertView.setTag(viewHolder);
      }
      else {
         viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.ivImage.setImageResource(0);

      viewHolder.tvTitle.setText(movie.getTitle());
      viewHolder.tvOverview.setText(movie.getOverview());

      if (orientation == Configuration.ORIENTATION_PORTRAIT) {
         Picasso.with(getContext()).load(movie.getImagePath())
                 .transform(new RoundedCornersTransformation(10, 10)).resize(350, 500).centerCrop()
                 .placeholder(R.drawable.placeholder).error(R.drawable.error48).into(viewHolder.ivImage);
      }
      else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
         Picasso.with(getContext()).load(movie.getImagePath())
                 .transform(new RoundedCornersTransformation(10, 10)).resize(350, 500).centerCrop()
                 .placeholder(R.drawable.placeholder).error(R.drawable.error48).into(viewHolder.ivImage);
      }

      return convertView;
   }
}
