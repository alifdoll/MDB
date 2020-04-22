package com.alif.submission.mdb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.alif.submission.mdb.ui.show.item.ShowItem;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class DatabaseFavorite extends SQLiteOpenHelper {

    private static DatabaseFavorite instance;

    private static final String DATABASE_NAME = "mdb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITE_MOVIES = "favorite_movies";
    private static final String COLUMN_FAVORITE_MOVIES_ID = "id";
    private static final String COLUMN_FAVORITE_MOVIES_TITLE = "title";
    private static final String COLUMN_FAVORITE_MOVIES_OVERVIEW = "overview";
    private static final String COLUMN_FAVORITE_MOVIES_POSTER_URL = "poster_url";

    private static final String TABLE_FAVORITE_SHOWS = "favorite_shows";
    private static final String COLUMN_FAVORITE_SHOW_ID = "id";
    private static final String COLUMN_FAVORITE_SHOW_TITLE = "title";
    private static final String COLUMN_FAVORITE_SHOW_OVERVIEW = "overview";
    private static final String COLUMN_FAVORITE_SHOW_POSTER_URL = "poster_url";

    private static final String SQL_CREATE_TABLE_FAVORITE_SHOWS = "CREATE TABLE " + TABLE_FAVORITE_SHOWS +
            " (" + COLUMN_FAVORITE_SHOW_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_FAVORITE_SHOW_TITLE + " TEXT," +
            COLUMN_FAVORITE_SHOW_OVERVIEW + " TEXT, " +
            COLUMN_FAVORITE_SHOW_POSTER_URL + " TEXT);";

    private static final String SQL_CREATE_TABLE_FAVORITE_MOVIES = "CREATE TABLE " + TABLE_FAVORITE_MOVIES +
            " (" + COLUMN_FAVORITE_MOVIES_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_FAVORITE_MOVIES_TITLE + " TEXT, " +
            COLUMN_FAVORITE_MOVIES_OVERVIEW + " TEXT, " +
            COLUMN_FAVORITE_MOVIES_POSTER_URL + " TEXT);";

    private static  SQLiteDatabase database;

    private static final String SQL_DROP_TABLE_FAVORITE_MOVIES = "DROP TABLE IF EXISTS " + TABLE_FAVORITE_MOVIES + ";";
    private static final String SQL_DROP_TABLE_FAVORITE_SHOWS = "DROP TABLE IF EXISTS " + TABLE_FAVORITE_SHOWS + ";";

    private DatabaseFavorite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseFavorite getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseFavorite(context);
        }
        return instance;
    }


    public static final String AUTHORITY = "com.alif.submission.mdb.provider";
    private static final String SCHEME = "content";

    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE_MOVIES)
            .build();

    public static final Uri CONTENT_URI_SHOW = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE_SHOWS)
            .build();

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIES);
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_SHOWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_FAVORITE_MOVIES);
        db.execSQL(SQL_DROP_TABLE_FAVORITE_SHOWS);
        onCreate(db);
    }

//    FOR MOVIE DATA
    public void addFavoriteMovie(MovieItem movieItem) {
//        Log.d("MDB_DEBUG", "addFavoriteMovie(movieItem) movieItem: " + movieItem);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE_MOVIES_ID, movieItem.getId());
        contentValues.put(COLUMN_FAVORITE_MOVIES_TITLE, movieItem.getTitle());
        contentValues.put(COLUMN_FAVORITE_MOVIES_OVERVIEW, movieItem.getOverview());
        contentValues.put(COLUMN_FAVORITE_MOVIES_POSTER_URL, movieItem.getPosterPath());
        getWritableDatabase().insert(TABLE_FAVORITE_MOVIES, null, contentValues);
    }

    public void deleteFavoriteMovie(int movieId) {
//        Log.d("MDB_DEBUG", "deleteFavoriteMovie(movieId) movieId: " + movieId);
        getWritableDatabase().delete(TABLE_FAVORITE_MOVIES, "id = ?", new String[]{String.valueOf(movieId)});
    }

    public boolean movieIsFavorite(int movieId) {
//        Log.d("MDB_DEBUG", "movieIsFavorite(movieId) movieId: " + movieId);
        Cursor cursor = getReadableDatabase().query(
                TABLE_FAVORITE_MOVIES,
                new String[]{COLUMN_FAVORITE_MOVIES_ID},
                "id = ?",
                new String[]{String.valueOf(movieId)},
                null,
                null,
                null
        );
        boolean favorite = cursor.getCount() == 1;
        cursor.close();
        return favorite;
    }

    public Cursor getFavMovie(){

        Cursor cursor = getReadableDatabase().query(
                TABLE_FAVORITE_MOVIES,
                new String[]{
                        COLUMN_FAVORITE_MOVIES_ID,
                        COLUMN_FAVORITE_MOVIES_TITLE,
                        COLUMN_FAVORITE_MOVIES_OVERVIEW,
                        COLUMN_FAVORITE_MOVIES_POSTER_URL
                },
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    public Cursor getFavMovie(String id){
        Cursor cursor;
        return getReadableDatabase().query(TABLE_FAVORITE_MOVIES,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public ArrayList<MovieItem> getAllFavoriteMovies() {
        Cursor cursor = getReadableDatabase().query(
                TABLE_FAVORITE_MOVIES,
                new String[]{
                        COLUMN_FAVORITE_MOVIES_ID,
                        COLUMN_FAVORITE_MOVIES_TITLE,
                        COLUMN_FAVORITE_MOVIES_OVERVIEW,
                        COLUMN_FAVORITE_MOVIES_POSTER_URL
                },
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<MovieItem> movieItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            MovieItem movieItem = new MovieItem();
            movieItem.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE_MOVIES_ID)));
            movieItem.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_MOVIES_TITLE)));
            movieItem.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_MOVIES_OVERVIEW)));
            movieItem.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_MOVIES_POSTER_URL)));
            movieItems.add(movieItem);
        }
        cursor.close();
        return movieItems;
    }

    public void open() throws SQLiteException{
//        database = database.getwriteabledatabase
    }

    public Cursor queryAll(String id){
        return database.query(TABLE_FAVORITE_MOVIES,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC ");
    }

//    FOR SHOW DATA ========================================================================================================

    public void addFavoriteShow(ShowItem showItem) {
//        Log.d("MDB_DEBUG", "addFavoriteMovie(movieItem) movieItem: " + movieItem);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE_MOVIES_ID, showItem.getId());
        contentValues.put(COLUMN_FAVORITE_MOVIES_TITLE, showItem.getTitle());
        contentValues.put(COLUMN_FAVORITE_MOVIES_OVERVIEW, showItem.getOverview());
        contentValues.put(COLUMN_FAVORITE_MOVIES_POSTER_URL, showItem.getPosterPath());
        getWritableDatabase().insert(TABLE_FAVORITE_SHOWS, null, contentValues);
    }

    public void deleteFavoriteShow(int showId) {
//        Log.d("MDB_DEBUG", "deleteFavoriteMovie(movieId) movieId: " + movieId);
        getWritableDatabase().delete(TABLE_FAVORITE_SHOWS, "id = ?", new String[]{String.valueOf(showId)});
    }

    public boolean showIsFavorite(int showId) {
//        Log.d("MDB_DEBUG", "movieIsFavorite(movieId) movieId: " + movieId);
        Cursor cursor = getReadableDatabase().query(
                TABLE_FAVORITE_SHOWS,
                new String[]{COLUMN_FAVORITE_SHOW_ID},
                "id = ?",
                new String[]{String.valueOf(showId)},
                null,
                null,
                null
        );
        boolean favorite = cursor.getCount() == 1;
        cursor.close();
        return favorite;
    }

    public ArrayList<ShowItem> getAllFavoriteShows() {
        Cursor cursor = getReadableDatabase().query(
                TABLE_FAVORITE_SHOWS,
                new String[]{
                        COLUMN_FAVORITE_SHOW_ID,
                        COLUMN_FAVORITE_SHOW_TITLE,
                        COLUMN_FAVORITE_SHOW_OVERVIEW,
                        COLUMN_FAVORITE_SHOW_POSTER_URL
                },
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<ShowItem> showItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            ShowItem showItem = new ShowItem();
            showItem.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE_SHOW_ID)));
            showItem.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_SHOW_TITLE)));
            showItem.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_SHOW_OVERVIEW)));
            showItem.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_SHOW_POSTER_URL)));
            showItems.add(showItem);
        }
        cursor.close();
        return showItems;
    }

    public Cursor getFavShow(){
        Cursor cursor = getReadableDatabase().query(
                TABLE_FAVORITE_SHOWS,
                new String[]{
                        COLUMN_FAVORITE_SHOW_ID,
                        COLUMN_FAVORITE_SHOW_TITLE,
                        COLUMN_FAVORITE_SHOW_OVERVIEW,
                        COLUMN_FAVORITE_SHOW_POSTER_URL
                },
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor getFavShow(String id){
        Cursor cursor = getReadableDatabase().query(TABLE_FAVORITE_MOVIES,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
        return cursor;
    }
}
