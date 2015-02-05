/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * DB use-case class.
 * Created by Marco Battisti on 31/01/2015.
 */
public class FavoriteDBMngr {

    public static final int ADD = 0;
    public static final int REMOVE = 1;
    public static final int CHECK = 2;
    //DB utility variable
    protected static final String DB_NAME = "FavoriteDB";
    protected static final int DB_VERSION = 1;
    protected static final String AUTHORITY = "it.zerocool.batmacaana";
    //Tables and columns
    protected static final String TABLE_FAVORITE = "Favorite";
    protected static final String ID_COLUMN = "ID";
    protected static final String TYPE_COLUMN = "TYPE";
    protected static final String JSON_COLUMN = "JSON";
    protected static final int TYPE_COLUMN_INDEX = 1;
    protected static final int ID_COLUMN_INDEX = 0;
    protected static final int JSON_COLUMN_INDEX = 2;

    /**
     * Add a place to favorite's list
     *
     * @param db    is the db
     * @param place is the place to add
     */
    public static boolean favoritePlace(SQLiteDatabase db, Place place) {
        if (place != null) {
            ContentValues values = new ContentValues();
            values.put(ID_COLUMN, place.getId());
            values.put(TYPE_COLUMN, place.getType());
            values.put(JSON_COLUMN, place.getJson());

            long done = db.insert(TABLE_FAVORITE, null, values);
            if (done >= 0)
                return true;
            else
                return false;
        }
        return false;
    }

    /**
     * Remove a place from favorite's list
     *
     * @param db    is the db
     * @param place is the place to remove
     */
    public static void unfavoritePlace(SQLiteDatabase db, Place place) {
        if (place != null) {
            String whereClause = ID_COLUMN + "= ?";
            String[] whereArgs = new String[1];
            whereArgs[0] = Integer.valueOf(place.getId()).toString();
            db.delete(TABLE_FAVORITE, whereClause, whereArgs);
        }
    }

    /**
     * List all the favorite on the DB
     *
     * @param db is the db
     * @return a List of favorite Places
     */
    public static ArrayList<Place> favoriteList(SQLiteDatabase db) {
        ArrayList<Place> result = new ArrayList<>();
        Cursor c = db.query(TABLE_FAVORITE, null, null, null, null, null, TYPE_COLUMN);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String json = c.getString(JSON_COLUMN_INDEX);
            if (json != null) {
                Place p = ParsingUtilities.parseSinglePlace(json);
                result.add(p);
                c.moveToNext();
            }

        }
        return result;
    }


    /**
     * Check if a place is in the favorite's list
     *
     * @param db    is the db
     * @param place is the place to check
     * @return true if place is in the database, false otherwise
     */
    public static boolean isFavorite(SQLiteDatabase db, Place place) {
        Place result = null;
        String whereClause = ID_COLUMN + "= ?";
        String[] whereArgs = new String[1];
        whereArgs[0] = Integer.valueOf(place.getId()).toString();
        Cursor c = db.query(TABLE_FAVORITE, null, whereClause, whereArgs, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String json = c.getString(JSON_COLUMN_INDEX);
            if (json != null) {
                result = ParsingUtilities.parseSinglePlace(json);
            }
            c.moveToNext();
        }
        return result != null;

    }


}
