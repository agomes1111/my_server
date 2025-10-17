package com.my.server.service.data;

import android.content.Context;
import android.icu.text.SimpleDateFormat;

import com.my.server.domain.data.sources.LocalDb;

import java.util.Locale;

public class SqlLite implements LocalDb {

    private static final String DATABASE_NAME = "my_server_database.db";
    private static final int DATABASE_VERSION = 4;
//    private static final String TABLE_NAME = "tasks";
//
//    private static final String COL_ID = "id";
//    private static final String COL_USER_ID = "user_id";
//    private static final String COL_TITLE = "title";
//    private static final String COL_DESC = "dsc";
//    private static final String COL_DONE = "done";
//    private static final String COL_SYNC = "sync";
//    private static final String COL_DUE_UNTIL = "due_until";
//    private static final String COL_NOTIFY_TIME = "notify_time";

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public SqlLiteService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ArrayList<>) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_USER_ID + " TEXT, "
                + COL_TITLE + " TEXT, "
                + COL_DESC + " TEXT, "
                + COL_DONE + " INTEGER, "
                + COL_SYNC + " INTEGER, "
                + COL_DUE_UNTIL + " TEXT, "
                + COL_NOTIFY_TIME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //// CREATE
    public boolean insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, task.getId());
        values.put(COL_USER_ID, task.getUserId());
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_DESC, task.getDsc());
        values.put(COL_DONE, task.isDone() ? 1 : 0);
        values.put(COL_SYNC, task.isSynced() ? 1 : 0);
        values.put(COL_DUE_UNTIL, formatDate(task.getDueUntil()));
        values.put(COL_NOTIFY_TIME, formatDate(task.getNotifyTime()));
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    /// READs ALL
    public ArrayList<Task> getAllTasks(String userId) {
        ArrayList<Task> list = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                        parseDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DUE_UNTIL))),
                        parseDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIFY_TIME))),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_DONE)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_SYNC)) == 1
                );
                list.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    /// UPDATE
    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, task.getId());
        values.put(COL_USER_ID, task.getUserId());
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_DESC, task.getDsc());
        values.put(COL_DONE, task.isDone() ? 1 : 0);
        values.put(COL_DUE_UNTIL, formatDate(task.getDueUntil()));
        values.put(COL_NOTIFY_TIME, formatDate(task.getNotifyTime()));

        int rows = db.update(TABLE_NAME, values, COL_ID + "=?",
                new String[]{task.getId()});
        db.close();
        return rows > 0;
    }

    //// DELETE
    public boolean deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{id});
        db.close();
        return rows > 0;
    }

    /// Helpers
    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }

    private Date parseDate(String text) {
        try {
            return (text != null) ? dateFormat.parse(text) : null;
        } catch (ParseException e) {
            return null;
        }
    }
}
