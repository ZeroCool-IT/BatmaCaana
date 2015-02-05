/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.zerocool.batmacaana.R;

/**
 * Favorite Database Open Helper Class. Singleton design pattern.
 *
 * @author Marco Battisti
 */
public class FavoriteDBHelper extends SQLiteOpenHelper {

    private static FavoriteDBHelper singleton;
    private static SQLiteDatabase writabelDB;
    private Context context;
    private InputStream inputStream;
    private BufferedReader bufferedReader;

    /**
     * Helper private constructor
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    private FavoriteDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Helper private constructor
     *
     * @param context is the applcation context
     */
    private FavoriteDBHelper(Context context) {
        super(context, FavoriteDBMngr.DB_NAME, null, FavoriteDBMngr.DB_VERSION);
        this.context = context;
    }

    /**
     * Returns an instance of FavoriteDBHelper, to be shared between activities
     *
     * @param context is the applcation context
     * @return an instance of database
     */
    public static FavoriteDBHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new FavoriteDBHelper(context);
        }
        return singleton;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            inputStream = context.getResources().openRawResource(R.raw.build_favorite_db);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
            db.beginTransaction();
            String data = bufferedReader.readLine();
            while (data != null) {
                db.execSQL(data);
                data = bufferedReader.readLine();
            }
            db.setTransactionSuccessful();

        } catch (IOException e) {
            Log.e("I/O ERROR", e.getMessage());
        } catch (SQLiteException e) {
            Log.e("DATABASE ERROR", e.getMessage());
        } finally {
            db.endTransaction();
        }

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Returns a writable instance of the DB
     *
     * @return a writable instance of the DB
     */
    public SQLiteDatabase getWritabelDB() {
        if (writabelDB == null || !writabelDB.isOpen()) {
            writabelDB = this.getWritableDatabase();
        }
        return writabelDB;
    }

    /**
     * Close any open database object.
     */
    @Override
    public void close() {
        super.close();
        if (writabelDB != null) {
            writabelDB.close();
            writabelDB = null;
        }
    }
}
