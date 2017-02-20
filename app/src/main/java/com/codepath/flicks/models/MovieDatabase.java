package com.codepath.flicks.models;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by keyulun on 2017/2/19.
 */

@Database(name=MovieDatabase.NAME, version = MovieDatabase.VERSION)
public class MovieDatabase {
   public static final String NAME = "MovieDatabase";
   public static final int VERSION = 1;
}
