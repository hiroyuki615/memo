package com.example.memo_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class MemoListAdapter extends BaseAdapter {
    static class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private List<HashMap<String,String>> memoLists;

    MemoListAdapter(Context context, int itemLayoutId, List<HashMap<String,String>> memoLists) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;/*inflater.inflate(itemLayoutId, parent, false)*/
        this.memoLists = memoLists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView1 = convertView.findViewById(R.id.content);/*メモ帳のタイトルに当たる部分*//*id=textViewはどこに当たるのか？*/
            holder.textView2 = convertView.findViewById(R.id.created);
            holder.textView3 = convertView.findViewById(R.id.memo_id);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 現在の position にあるファイル名リストを holder の textView にセット

        String content=memoLists.get(position).get("content");
        if(content.length()>=10){/*リストに表示したいcontentの内容を10文字に制限する*/
            holder.textView1.setText(content.substring(0,10)+"...");
        }
        else{
            holder.textView1.setText(content);
        }
        holder.textView2.setText(memoLists.get(position).get("created"));
        holder.textView3.setText((memoLists.get(position).get("id")));
        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return memoLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
