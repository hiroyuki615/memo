package com.example.memo_app;

/*データベースを構築するためのSQL文を定義している*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    // データベースのバージョン
    private static final int DATABASE_VERSION = 2;

    // データーベース名
    /*テーブルのテーブル名と列名を定義する*/
    private static final String DATABASE_NAME = "memo.db";
    private static final String TABLE_NAME ="memo";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE ="content";
    private static final String COLUMN_NAME_SUBTITLE ="created";



    //実行するSQL文
    private static final String SQL_CREATE_ENTRIES =
            /*テーブルを作成するSQL文*/
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_SUBTITLE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            /*テーブルを削除する*/
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    // 継承している親クラスにデータベース名とバージョンをセット
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * ④テーブルの定義
     * テーブルの作成(初期データの投入も出来る)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブルを作成
        db.execSQL(
                SQL_CREATE_ENTRIES
        );

        Log.d("debug竹下create", "onCreate(SQLiteDatabase db)");
        //データベースのテーブルを作成する
        // ここで、初期データ挿入可能
    }


    /**
     * ⑤データベースのバージョンアップ時の処理
     * この例では、古いバージョン時には、createdがなかった為、新しく定義し直している。
     */
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }


//    protected SQLiteDatabase open() {
//        return super.getWritableDatabase();
//    }
    public void close(){
        super.close();
    }

}
