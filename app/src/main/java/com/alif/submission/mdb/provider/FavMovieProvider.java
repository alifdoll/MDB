package com.alif.submission.mdb.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.alif.submission.mdb.database.DatabaseFavorite;

public class FavMovieProvider extends ContentProvider {

    private DatabaseFavorite movieDatabase;

    public static final int MOVIE = 1;
    public static final int MOVIE_ID_FAVORITE = 2;

    public static final int SHOW = 3;
    public static final int SHOW_ID_FAVORITE = 4;

    private static final String DB_TABLE_MOVIE = "favorite_movies";
    private static final String DB_TABLE_SHOW = "favorite_shows";

    private static final String AUTHORITY = "com.alif.submission.mdb.provider";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sUriMatcher.addURI(AUTHORITY, DB_TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, DB_TABLE_MOVIE + "/#", MOVIE_ID_FAVORITE);

        sUriMatcher.addURI(AUTHORITY, DB_TABLE_SHOW, SHOW);
        sUriMatcher.addURI(AUTHORITY, DB_TABLE_SHOW + "/#", SHOW_ID_FAVORITE);
    }

    public FavMovieProvider() {
    }

    @Override
    public boolean onCreate() {
        movieDatabase = DatabaseFavorite.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){

            case MOVIE:
                cursor = movieDatabase.getFavMovie();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case MOVIE_ID_FAVORITE:
                cursor = movieDatabase.getFavMovie(uri.getLastPathSegment());
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case SHOW:
                cursor = movieDatabase.getFavShow();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case SHOW_ID_FAVORITE:
                cursor = movieDatabase.getFavShow(uri.getLastPathSegment());
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;

                default:
                    cursor = null;
                    break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
