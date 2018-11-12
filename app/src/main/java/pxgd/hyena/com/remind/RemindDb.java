package pxgd.hyena.com.remind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RemindDb {
    private static final String TAG = "RemindDb";
    private static final String DATABASE_NAME = "db_reminds";
    private static final String TABLE_NAME = "reminds";

    //数据库版本（它控制DbHelper的onCreate还是onUpgrade运行）
    private static final int DATABASE_VERSION = 1;

    //字段名称和建表语句
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";

    //字段的索引
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;


    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    //构造函数保存一个上下文实例
    public RemindDb(Context ctx) {
        this.mCtx = ctx;
    }
    //打开数据库（获取数据库实例）
    public void open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //关闭数据库
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //创建一条记录
    public long createReminder(Remind reminder) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getImportant());
        return mDb.insert(TABLE_NAME, null, values);
    }
    public void createReminder(String content, boolean important) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, content);
        values.put(COL_IMPORTANT, important ? 1 : 0);
        mDb.insert(TABLE_NAME, null, values);
    }
    //读取指定记录
    public Remind fetchReminderById(int id) {
        Cursor cursor = mDb.query(
                TABLE_NAME,
                new String[]{COL_ID, COL_CONTENT, COL_IMPORTANT}, COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );
        if (cursor != null)
            cursor.moveToFirst();

        //返回对象实例
        return new Remind(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_CONTENT),
                cursor.getInt(INDEX_IMPORTANT)
        );
    }
    //读取所有记录
    public Cursor fetchAllReminders() {
        Cursor mCursor = mDb.query(
                TABLE_NAME,
                new String[]{COL_ID, COL_CONTENT, COL_IMPORTANT},
                null,
                null,
                null,
                null,
                null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //更新指定记录
    public void updateReminder(Remind reminder) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getImportant());
        mDb.update(TABLE_NAME,
                values,
                COL_ID + "=?",
                new String[]{
                        String.valueOf(reminder.getId())
                }
        );
    }
    //删除指定记录
    public void deleteReminderById(int nId) {
        mDb.delete(
                TABLE_NAME,
                COL_ID + "=?",
                new String[]{String.valueOf(nId)}
        );
    }
    //删除所有记录
    public void deleteAllReminders() {
        mDb.delete(TABLE_NAME, null, null);
    }





    //内部静态类
    private static class DbHelper extends SQLiteOpenHelper {

        //抅造函数：将数据库名称和版本号传给超类
        DbHelper(Context context) {
            super(
                    context,
                    DATABASE_NAME,
                    null,
                    DATABASE_VERSION
            );
        }
        //当需创建数据库时onCreate方法调用
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }
        //当需更新数据库时onUpgrade方法调用
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
