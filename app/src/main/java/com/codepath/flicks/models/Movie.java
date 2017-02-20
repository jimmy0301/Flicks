package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by keyulun on 2017/2/9.
 */

public class Movie {
   Long id;
   String posterPath;
   String backdropPath;
   String overview;
   String title;
   String releaseDate;
   Double vote_avg;

   public Movie(Long id, String posterPath, String overview,
                String title, String releaseDate, Double vote_avg) {
      setId(id);
      setTitle(title);
      setVote_avg(vote_avg);
      setReleaseDate(releaseDate);
      setPosterPath(posterPath);
      setOverview(overview);
   }

   public Movie(JSONObject jsonObject) throws JSONException {
      this.id = jsonObject.getLong("id");
      this.releaseDate = jsonObject.getString("release_date");
      this.posterPath = jsonObject.getString("poster_path");
      this.overview = jsonObject.getString("overview");
      this.title = jsonObject.getString("title");
      this.backdropPath = jsonObject.getString("backdrop_path");
      this.vote_avg = jsonObject.getDouble("vote_average");
   }

   public static ArrayList<Movie> fromJsonArray(JSONArray array) {
      ArrayList<Movie> results = new ArrayList<>();

      for (int i = 0; i < array.length(); i++) {
         try {
            results.add(new Movie(array.getJSONObject(i)));
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }
      return results;
   }

   public String getImagePath() {
      return posterPath;
   }

   public String getPosterPath() {
      return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
   }

   public String getBackdropPath() {
      return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
   }

   public String getOverview() {
      return overview;
   }

   public String getTitle() {
      return title;
   }

   public Long getId() { return id; }

   public void setId(Long id) {
      this.id = id;
   }

   public void setPosterPath(String posterPath) {
      this.posterPath = posterPath;
   }

   public void setBackdropPath(String backdropPath) {
      this.backdropPath = backdropPath;
   }

   public void setOverview(String overview) {
      this.overview = overview;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setReleaseDate(String releaseDate) {
      this.releaseDate = releaseDate;
   }

   public void setVote_avg(Double vote_avg) {
      this.vote_avg = vote_avg;
   }

   public String getReleaseDate() {
      return releaseDate;
   }

   public Double getVote_avg() { return vote_avg; }
}
