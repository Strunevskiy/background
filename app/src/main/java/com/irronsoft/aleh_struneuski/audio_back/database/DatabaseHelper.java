package com.irronsoft.aleh_struneuski.audio_back.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alehstruneuski on 5/1/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "com_irronsoft_background.db";
    private static final String TABLE_NAME = "tracks";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ID_SOUND = "_id_sound";
    private static final String COLUMN_TITLE = "_title";
    private static final String COLUMN_STREAM_URL = "_stream_url";
    private static final String COLUMN_ARTWORK_URL = "_artwork_url";
    private static final String COLUMN_SOUND_FILEPATH = "_sound_filepath";
    private static final String COLUMN_IMAGE_FILEPATH = "_image_filepath";

    /*Convenience INDEXES. DO NOT USE for anything else other than extracting
     *from a cursor that has all columns. Helps with
     *faster lookup*/
    public static final int INDEX_COLUMN_ID = 0;
    public static final int INDEX_COLUMN_ID_SOUND = 1;
    public static final int INDEX_COLUMN_TITLE = 2;
    public static final int INDEX_COLUMN_STREAM_URL = 3;
    public static final int INDEX_COLUMN_ARTWORK_URL = 4;
    public static final int INDEX_COLUMN_SOUND_FILEPATH = 5;
    public static final int INDEX_COLUMN_IMAGE_FILEPATH = 6;

    static final int EMPTY_COLUMN_VALUE = -1;

    private static DatabaseHelper databaseHelper;

    private final SQLiteDatabase db;
    private boolean loggingEnabled = true;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME,null,VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID_SOUND + " INTEGER NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_STREAM_URL + " TEXT NOT NULL, "
                + COLUMN_ARTWORK_URL + " TEXT NOT NULL, "
                + COLUMN_SOUND_FILEPATH + " TEXT NOT NULL, "
                + COLUMN_IMAGE_FILEPATH + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if(databaseHelper != null) {
            return databaseHelper;
        }
        if(context == null) {
            throw new NullPointerException("Context cannot be null");
        }
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        return databaseHelper;
    }

    public synchronized boolean containRecordByTitle(String title) {
        String[] args = { title };
        boolean found = false;
        try {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " = ?", args);
            if(cursor != null && cursor.getCount() > 0) {
                found = true;
                cursor.close();
            }
        } catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
            found = false;
        }
        return found;
    }

    public  String getInsertStatement(long soundId, String title, String streamUrl, String artworkUrl,
                              String soundPath, String imagePath) {
        return "INSERT INTO " + TABLE_NAME +
                " (" + COLUMN_ID_SOUND + "," + COLUMN_TITLE + "," + COLUMN_STREAM_URL + " , " + COLUMN_ARTWORK_URL + ", " + COLUMN_SOUND_FILEPATH + ", " + COLUMN_IMAGE_FILEPATH + ")" +
                "VALUES " + "('" + soundId+ "','" + title + "', '" + streamUrl + "', '" + artworkUrl + "', '" + soundPath + "', '" + imagePath + "' )";
    }

    public synchronized boolean insert(long soundId, String title, String streamUrl, String artworkUrl, String soundPath, String imagePath) {

        String statement = getInsertStatement(soundId, title,streamUrl, artworkUrl, soundPath,imagePath);

        List<String> insertStatements = new ArrayList<>(1);
        insertStatements.add(statement);

        return insert(insertStatements);
    }

    public synchronized boolean insert(List<String> statements) {
        boolean inserted = false;
        if(statements == null) {
            return inserted;
        }
        try {
            db.beginTransaction();
            for (String statement : statements) {
                db.execSQL(statement);
            }
            db.setTransactionSuccessful();
        }catch (Exception e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }

        try {
            db.endTransaction();
            inserted = true;
        }catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        return inserted;
    }

    public synchronized boolean deleteByTitle(String title) {
        String[] args = { title };
        boolean removed = false;
        try {
            db.beginTransaction();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " = ?", args);
            db.setTransactionSuccessful();
        }catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }

        try {
            db.endTransaction();
            removed = true;
        }catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        return removed;
    }

    public synchronized boolean deleteByStreamUrl(String streamUrl) {
        String[] args = { streamUrl };

        boolean removed = false;
        try {
            db.beginTransaction();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_STREAM_URL + " = ?", args);
            db.setTransactionSuccessful();
        }catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }

        try {
            db.endTransaction();
            removed = true;
        }catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        return removed;
    }

    public synchronized Cursor getByTitle(String title) {
        try {
            String[] args = { title };
            return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " = ?", args);
        }catch (SQLiteException e) {

            if(loggingEnabled) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public synchronized Cursor getByStreamUrl(String streamUrl) {
        try {
            String[] args = { streamUrl };
            return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STREAM_URL + " = ?", args);
        }catch (SQLiteException e) {
            if(loggingEnabled) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public synchronized Cursor get() {
        try {
            return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        }catch (SQLiteException e) {

            if(loggingEnabled) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public synchronized boolean updateImageData(String streamUrl, String imageFilepath) {
        String[] args = { imageFilepath, streamUrl };

        boolean updated = false;
        try {
            db.beginTransaction();
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_IMAGE_FILEPATH + " = ?" + " WHERE " + COLUMN_STREAM_URL + " = ?", args);
            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        try {
            db.endTransaction();
            updated = true;
        }catch (SQLiteException e) {

            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        return updated;
    }

    public synchronized boolean updateSoundData(String streamUrl, String soundFilepath) {
        String[] args = { soundFilepath, streamUrl };

        boolean updated = false;
        try {
            db.beginTransaction();
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_SOUND_FILEPATH + " = ?" +" WHERE " + COLUMN_STREAM_URL + " = ?" , args);

            db.setTransactionSuccessful();
        }catch (SQLiteException e){

            if(loggingEnabled) {
                e.printStackTrace();
            }
        }

        try {
            db.endTransaction();
            updated = true;
        }catch (SQLiteException e) {

            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        return updated;
    }

    public synchronized boolean removeByTitle(String title) {
        String[] args = { title };
        boolean remove = false;
        try {
            db.beginTransaction();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " = ?", args);
            db.setTransactionSuccessful();
        }catch (SQLiteException e){

            if(loggingEnabled) {
                e.printStackTrace();
            }
        }

        try {
            db.endTransaction();
            remove = true;
        }catch (SQLiteException e) {

            if(loggingEnabled) {
                e.printStackTrace();
            }
        }
        return remove;
    }

}
