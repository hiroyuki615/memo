package com.example.memo_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MemoEditActivity extends AppCompatActivity {

    private MemoDao dao = null;

    /**
     メモIDを取得し、該当するメモ内容をデーターベースから該当するデータを取得しています。
     メモ内容を取得後、EditTextのViewにデータをセットして表示させています。
     又、メモIDが取得出来なかったら、新規メモの作成と見なし、そのままEditViewを表示しています。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);/*activity_memo_edit.xmlファイルを編集する*/
        // インテントの取得
        Intent data = getIntent();
        if (data != null) {
            /*インテントから得たデータがnullではないときには、memoIdを取得して処理をする*/
            String memoId = data.getStringExtra("memo_id");
            if (memoId != null) {
                // TextViewを取得し、メモIDをセット。TextViewのIDはmemo_idである。
                TextView textView = (TextView) findViewById(R.id.memo_id);
                textView.setText(memoId);

                // データ検索
                MemoDao memoDao = new MemoDao(getApplicationContext());
                memoDao.connection();
                HashMap memoMap = memoDao.searchById(memoId);/*memoDaoでmemoIdからメモの内容を取得する(型はHashMap)*/
                memoDao.close();

                // EditTextのViewにデータを格納。
                /*EditTextを取得するIDはedittext01*/
                EditText editText = (EditText) findViewById(R.id.edittext01);
                editText.setText(memoMap.get("content").toString());/*memoDaoから検索して取得したメモの内容をセットする。*/

            }

        }

    }


    /**
     更新と削除のメニューを作成しています。
     /app/src/main/res/menu/memo_edit.xml参照
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*勝手これは、実行されるメソッド→inflateされる→メニューバーが作成される*/
        getMenuInflater().inflate(R.menu.memo_edit, menu);
        return true;
    }

    /**
     アクションバーの更新ボタンと削除ボタンがタップされた処理が記載されています。
     まず、更新ボタンですが、メモ内容が入るEditViewを取得します。メモ内容がなければ、
     何もしません。メモ内容があれば、更に、TextViewにmemo_idが存在しているか確認します。
     存在していれば、前の画面でリストをタップしてきた状態(編集モード)となるので、データベースを
     更新します。なければ、新規にデータを追加するとなります。
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 保存ボタンがタップされた処理
        if (id == R.id.actionbar_save) {

            // EditTextのViewから入力内容を取得
            EditText edit = findViewById(R.id.edittext01);
            String content = edit.getText().toString();/*メモ帳の内容を取得*/
            if (content == null) return false;

            // TextViewを取得し、メモIDを取得
            TextView textView = findViewById(R.id.memo_id);
            String memoId = textView.getText().toString();/*memoIDはrowIDのこと*/

            MemoDao memoDao = new MemoDao(getApplicationContext());
            memoDao.connection();/*データベースにつながる*/
            /*データベース内にmemoIdがないことを確認できたら新規追加処理をする*/
            if (memoId == null || memoId.length() == 0) {
                // データ新規追加
                memoId = String.valueOf(memoDao.addMemo(content.trim()));
                /*context.trim()で文字列のコピーを返す。この時に先頭または末尾の空白は取り除く*/
                /*addMemo()をしたときの返り値をString型に変えてそれをmemoIdに入れて*/
                /*addMemo()の返り値は何か？→rowID(long型)*/
                textView.setText(String.valueOf(memoId));
            } else {
                // データ更新処理
                memoDao.updateContentById(memoId, content.trim());
            }
            memoDao.close();/*データベースを切る*/
            Toast.makeText(this, "保存しました。", Toast.LENGTH_SHORT).show();

            return true;

            // 削除ボタンがタップされた処理
        } else if (id == R.id.actionbar_delete) {
            TextView memoIdView = findViewById(R.id.memo_id);
            String memoId = memoIdView.getText().toString();
            // 削除処理
            MemoDao memoDao = new MemoDao(getApplicationContext());
            memoDao.connection();
            memoDao.delById(memoId);
            memoDao.close();
            Toast.makeText(this, "削除しました。", Toast.LENGTH_SHORT).show();
            finish();// activity終了
            return true;

            // アクションバーの戻るがタップされた処理
        } else if (id == android.R.id.home){
            finish();// activity終了
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
