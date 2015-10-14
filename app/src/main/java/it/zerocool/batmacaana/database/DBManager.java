/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * DB use-case class.
 * Created by Marco Battisti on 31/01/2015.
 */
public class DBManager {

    public static final int ADD = 0;
    public static final int REMOVE = 1;
    public static final int CHECK = 2;
    //DB utility variable
    static final String DB_NAME = "ExploraDB";
    static final int DB_VERSION = 2;

    //Tables and columns

    //Favorite table
    private static final String TABLE_FAVORITE = "Favorite";
    private static final String ID_COLUMN = "ID";
    private static final String TYPE_COLUMN = "TYPE";
    private static final String JSON_COLUMN = "JSON";
    private static final int JSON_COLUMN_INDEX = 2;

    //Customers table
    private static final String TABLE_CUSTOMERS = "Customers";
    private static final String UID_COLUMN = "UID";
    private static final String CITY_NAME_COLUMN = "NAME";
    private static final String AVATAR_COLUMN = "AVATAR";
    private static final String PREMIUM_COLUMN = "PREMIUM";
    private static final int ID_COLUMN_INDEX = 0;
    private static final int UID_COLUMN_INDEX = 1;
    private static final int NAME_COLUMN_INDEX = 2;
    private static final int AVATAR_COLUMN_INDEX = 3;
    private static final int PREMIUM_COLUMN_INDEX = 4;

    /**
     * Add a place to favorite's list
     *
     * @param db    is the db
     * @param place is the place to add
     */
    public static boolean favoritePlace(@NonNull SQLiteDatabase db, @Nullable Place place) {
        if (place != null) {
            ContentValues values = new ContentValues();
            values.put(ID_COLUMN, place.getId());
            values.put(TYPE_COLUMN, place.getType());
            values.put(JSON_COLUMN, place.getJson());

            long done = db.insert(TABLE_FAVORITE, null, values);
            return done >= 0;
        }
        return false;
    }

    /**
     * Remove a place from favorite's list
     *
     * @param db    is the db
     * @param place is the place to remove
     */
    public static void unfavoritePlace(@NonNull SQLiteDatabase db, @Nullable Place place) {
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
    @NonNull
    public static ArrayList<Place> favoriteList(@NonNull SQLiteDatabase db) {
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
        c.close();
        return result;
    }


    /**
     * Check if a place is in the favorite's list
     *
     * @param db    is the db
     * @param place is the place to check
     * @return true if place is in the database, false otherwise
     */
    public static boolean isFavorite(@NonNull SQLiteDatabase db, @NonNull Place place) {
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
        c.close();
        return result != null;

    }

    /**
     * Clear the favorite list
     *
     * @param db is the db
     */
    public static void clearFavorite(@NonNull SQLiteDatabase db) {
        db.delete(TABLE_FAVORITE, null, null);
    }


    /**
     * Add Customer Cities to local DB
     *
     * @param db        is the db
     * @param customers is the list of Customer Cities
     */
    public static boolean addCustomers(@NonNull SQLiteDatabase db, @Nullable ArrayList<City> customers) {
        boolean done = false;
        if (customers != null && !customers.isEmpty()) {
            int check = customers.size();
            db.beginTransaction();
            try {
                for (City c : customers) {
                    ContentValues values = new ContentValues();
                    values.put(ID_COLUMN, c.getId());
                    values.put(UID_COLUMN, c.getUserID());
                    values.put(CITY_NAME_COLUMN, c.getName());
                    values.put(AVATAR_COLUMN, c.getAvatar());
                    values.put(PREMIUM_COLUMN, c.isPremium());
                    check += db.insert(TABLE_CUSTOMERS, null, values);
                }
                db.setTransactionSuccessful();
                done = check > 0;
            } finally {
                db.endTransaction();
            }
        }
        return done;
    }

    /**
     * Clear the customers table
     *
     * @param db is the db
     */
    public static void clearCustomers(@NonNull SQLiteDatabase db) {
        db.delete(TABLE_CUSTOMERS, null, null);
    }

// --Commented out by Inspection START (14/04/2015 11:06):
//    public static boolean hasCustomers(SQLiteDatabase db) {
//        ArrayList<City> result = getCustomers(db);
//        return !result.isEmpty();
//    }
// --Commented out by Inspection STOP (14/04/2015 11:06)

    /**
     * @param db is the db
     * @return an ArrayList of City that have an Explora account
     */
    @NonNull
    public static ArrayList<City> getCustomers(@NonNull SQLiteDatabase db) {
        ArrayList<City> result = new ArrayList<>();
        Cursor c = db.query(TABLE_CUSTOMERS, null, null, null, null, null, "NAME");
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int id = c.getInt(ID_COLUMN_INDEX);
            City city = new City(id);
            city.setUserID(c.getInt(UID_COLUMN_INDEX));
            city.setName(c.getString(NAME_COLUMN_INDEX));
            city.setAvatar(c.getString(AVATAR_COLUMN_INDEX));
            city.setPremium(c.getInt(PREMIUM_COLUMN_INDEX));
            result.add(city);
            c.moveToNext();
        }
        c.close();
        return result;
    }
}
