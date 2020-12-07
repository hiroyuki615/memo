package com.example.memo_app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 保存したメモの一覧
 */
/*Mainのクラス*/
public class MemoListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String mSearchWord = null;

    /**
     レイアウトのセット及びlistView変数への格納のみになります。
     MemoEditActivityから戻ってきた場合、onCreateメソッドは実行されない為、
     onResumeメソッドで、基本的な処理を記載するようにしています。
     ※ onCreateは1度実行されると、２度目の実行はされません。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);


    }

    /**
     このクラス内のsetDatabaseDataBySearchWordメソッドを呼び、
     そのメソッドで、データベースから値を取得しListViewにデータをセットしています。
     */
    @Override
    protected void onResume() {
        super.onResume();

        setDatabaseDataBySearchWord(mSearchWord);
    }

    /**
     オプションメニューの作成処理になります。
     */
    /*id=actionbar_serchの検索ボタン、actionbar_addの新規追加ボタン*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*自動的に呼び出されてinflateする*/
        getMenuInflater().inflate(R.menu.memo_list, menu);

        // SearchViewの作成
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionbar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // イベントリスナー処理
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

            // 検索窓から文字が入力される度に実行されます。
            @Override
            public boolean onQueryTextChange(String searchWord) {
                setDatabaseDataBySearchWord(searchWord);
                mSearchWord = searchWord;
                return true;
            }

            // 検索ボタンもしくは、キーボードでEnterされた際に実行されます。
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                return true;
            }
        };

        // イベントリスナーをセットします。
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    /**2番目に大事な処理！！
     メニューのアイテムが選択された際の処理です。
     新規ボタンがタップされていれば、MemoEditActivityへ遷移します。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 新規ボタン追加ボタンがタップされた処理
        if (id == R.id.actionbar_add) {
            Intent intent = new Intent(this.getApplicationContext(), MemoEditActivity.class);
            startActivity(intent);
            return true;
        }

        // 検索ボタンがタップされた処理
        if (id == R.id.actionbar_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     このActivity内での一番大事な検索処理になります。
     */
    private void setDatabaseDataBySearchWord(String searchWord) {
        List<HashMap<String, String>> memoLists = new ArrayList<HashMap<String, String>>();
        MemoDao dao = new MemoDao(this.getApplicationContext());
        dao.connection();



        if (TextUtils.isEmpty(searchWord)) {
            /*TextUtils.isEmpty(str)でnullまたは空文字を判定することができる*/
            /*searchWordに引数に値が入ったのかを確認する*/
            memoLists = dao.searchAll();
            /*dao.searchAll()で全部探索する*/
        } else {
            memoLists = dao.searchAllByWord(searchWord);
            /*dao.searchAllByWord()では文字で探索してくれる*/
        }
        dao.close();
        setListView(memoLists);
    }

    /**
     アダプタを介して、リストビューにデータをセットしています。
     */
    private void setListView(List<HashMap<String,String>> memoLists) {
        /*memoListsでリストの内容が渡される。memoLists⇒HashMapのmemoListがはいっている*/
        /*memoListsでリストの内容が渡される*/
        ListView listView = findViewById(R.id.ListView01);
        BaseAdapter memoListAdapter = new MemoListAdapter(this.getApplicationContext(),R.layout.activity_memo_list_row,memoLists);
        listView.setAdapter(memoListAdapter);// ListViewにadapterをセット
        listView.setOnItemClickListener(this);/*クリック時に飛ぶ処理*/

    }


    /**
     リストでメモがタップされた際の処理(後々、ファイル分割しやすいように内部クラスとして記述)になります。
     インテントを生成し、メモIDをセット後、MemoEditActivityへ遷移します。
     (このイベントリスナーである内部クラスは、上記のsetListViewメソッドで呼び出しています。)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*クリック時の処理を書く*/
        TextView idView = view.findViewById(R.id.memo_id);
        Intent intent = new Intent(this.getApplicationContext(), MemoEditActivity.class);
        intent.putExtra("memo_id", idView.getText().toString());
        /*MemoEditActivityへ遷移*/
        startActivity(intent);
    }


}





