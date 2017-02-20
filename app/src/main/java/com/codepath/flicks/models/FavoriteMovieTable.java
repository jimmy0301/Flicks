package com.codepath.flicks.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by keyulun on 2017/2/19.
 */

@Table(database = MovieDatabase.class)
public class FavoriteMovieTable extends BaseModel {
   @Column
   @PrimaryKey
   Long id;

   @Column
   String imagePath;

   public void setVote_avg(Double vote_avg) {
      this.vote_avg = vote_avg;
   }

   public void setReleaseDate(String releaseDate) {
      this.releaseDate = releaseDate;
   }

   public Double getVote_avg() {
      return vote_avg;
   }

   public String getReleaseDate() {
      return releaseDate;
   }

   @Column
   Double vote_avg;

   @Column
   String releaseDate;

   @Column
   String title;

   @Column
   String overview;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setOverview(String overview) {
      this.overview = overview;
   }

   public String getImagePath() {
      return imagePath;
   }

   public void setImagePath(String imagePath) {
      this.imagePath = imagePath;
   }

   public String getTitle() {

      return title;
   }

   public String getOverview() {
      return overview;
   }
}
