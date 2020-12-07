package com.example.memo_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MemoDao extends DatabaseHelper{

    private Context mContext = null;
    private  SQLiteDatabase mDb = null;

    protected MemoDao(Context context) {
        super(context);//親クラスのコンストラクタを呼ぶ、contextを入れる
        mContext = context;
    }

    /*データベースオープン*/
    protected void connection() {
        DatabaseHelper helper = new DatabaseHelper(mContext);
        mDb = helper.getWritableDatabase();
    }

    /** データベースクローズ */
    public void close() {
        mDb.close();
        mDb = null;
    }

    /** データを投入 */
    protected long addMemo(String content){
        String dateNow = String.valueOf(DateFormat.format(
                "yyyy-MM-dd kk:mm:ss", Calendar.getInstance()));
        /*「String.valueOf()で括弧内を文字列型にキャストする。変換したい値がnullであってもNullPointerExceptionを発生させない」*/
        /*「toString()でも文字列型にキャストすることができる。変換したい値がnullであったらNullPointerExceptionが発生する。」*/
        /*「DateFormat.format()で引数で、フォーマットと日付を受け取りそのフォーマットに正規化する関数」*/
        /*「Calendar cal = Calendar.getInstance();などでインスタンスを生成したときに生成時点の日付/時刻を保持したインスタンスになる。」*/

        ContentValues val = new ContentValues();
        //「ContentValuesクラスでは、データの追加や更新を行うことができる」
        val.put("content",content);
        val.put("created",dateNow );


        return mDb.insert( "memo", null, val);
        /*「データを挿入するためには、データを一旦ContentValuesクラスのインスタンスに格納する。put()を使い、カラム名をキーに値を渡すことで実現できる。 そして格納したデータをSQLiteDatabaseクラスのinsert()メソッドで挿入する。」*/
    }


    /** データを読込む(検索機能)*/
    protected List searchAllByWord(String word){
        List memoLists = new ArrayList<>();
        String[] selectArgs = new String[]{ word };

        //「Cursorとは？→SQLiteDatabaseのデータを取り出す際に使用するやつ。」
        Cursor cursor = mDb.rawQuery("select * from memo where content Like '%' || ? || '%' order by created desc;", selectArgs);
            //「rawQuery()では、SQL文をそのまま使用するので何か細かい条件を指定することができる」
            while(cursor.moveToNext()) {
                //「データベースの結果をCursorに格納し、中身を個別に取り出したい場合」
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                //「縦列が"_id"であるところの値を取り出す」
                String content = cursor.getString(cursor.getColumnIndex("content"));
                //「縦列が"content"であるところの値を取り出す」
                String created = cursor.getString(cursor.getColumnIndex("created"));
                //「縦列が"created"であるところの値を取り出す」
                HashMap<String,String> memoList = new HashMap();
                //HashMapとは→「キー」と「値」で構成されている
                memoList.put("id", String.valueOf(id));
                memoList.put("content", content);
                memoList.put("created", created);
                memoLists.add(memoList);
            }
        cursor.close();

        return memoLists;
        /*memoListsの中には、HashMapのmemoListが入っている*/
    }

    /** データを読込む */
    protected List searchAll(){
        List<HashMap<String,String>> memoLists = new ArrayList<HashMap<String,String>>();

        Cursor  cursor = mDb.rawQuery("SELECT * FROM memo ORDER BY created DESC;", null);
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String created = cursor.getString(cursor.getColumnIndex("created"));
                Log.d("memo", "内容:"+content + "  作成日:" + created);
                /*「Log.d(String tag,String msg);で"tag"はあとからログを識別するためのキーを代入し、"msg"にログメッセージを代入します。」*/
                /*「Log.d(“onClick”,”変数 i は「” + i + “」”);で変数int iの値を確認したいとき」*/
                HashMap<String,String> memoList = new HashMap();/*HashMapを生成する*/
                memoList.put("id", String.valueOf(id));
                memoList.put("content", content);
                memoList.put("created", created);
                memoLists.add(memoList);
            }
                cursor.close();

        return memoLists;
    }

    /** メモIDで検索 */
    protected HashMap searchById( String memoId ){
        String[] selectArgs = new String[]{ memoId };

        HashMap map = new HashMap();/*HashMapを生成*/
        Cursor   cursor = mDb.rawQuery("select * from memo WHERE _id = ?", selectArgs);

            while(cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String created = cursor.getString(cursor.getColumnIndex("created"));
                map.put("id", id);
                map.put("content", content);
                map.put("created", created);
                Log.d("memo", "内容:"+content + "  作成日:" + created);
            }

            cursor.close();

        return map;
    }

    /** メモを更新 「更新」*/
    public int updateContentById( String memoId, String content ){
        String[] selectArgs = new String[]{ memoId };
        ContentValues val = new ContentValues();
        //ContextValuesクラスは、データの更新や追加で使うクラス
        val.put("content", content);

        return mDb.update( "memo", val, "_id = ?", selectArgs);
    }

    /** メモの内容を条件に削除 「削除」*/
    public int delById( String id ){
        String[] selectArgs = new String[]{ id };
        return mDb.delete( "memo", "_id = ?", selectArgs);
    }

    /** 全削除 */
    public int delAll(){
        return mDb.delete( "memo", null, null );
    }


}
