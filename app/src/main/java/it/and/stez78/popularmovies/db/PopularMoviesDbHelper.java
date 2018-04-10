package it.and.stez78.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.and.stez78.popularmovies.db.MovieContract.MovieEntry;

/**
 * Created by stefano on 18/03/18.
 */

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmoviesDb.db";

    private static final int VERSION = 6;

    PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
