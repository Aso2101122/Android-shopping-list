package com.websarva.wings.android.android_shopping_list;

import android.app.LauncherActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter {
    //フィールド変数
    private int resource;
    private List<Item> itemList;
    private final LayoutInflater inflater;
    private DatabaseHelper _helper;


    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource リソースID
     * @param itemList リストビューの要素
     */
    public ItemAdapter(Context context,int resource, List<Item> itemList){
        super(context, resource, itemList);
        this.resource = resource;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //リストに表示するデータの個数です
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        //引数で指定された位置にある定食の情報を返します
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //引数で指定された位置にあるアイテムののIDを返します
        return itemList.get(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初回は作成される、既にビューがある場合は使いまわす
        if(convertView != null){
            convertView = inflater.inflate(resource, null);
        }
        else{
            convertView = inflater.inflate(resource, null);
        }
        //一行ずつ情報を紐づけていきます
        Item item = itemList.get(position);

        //アイテム名を設定
        TextView tvItemName = convertView.findViewById(R.id.tvItemNameRow);
        tvItemName.setText(item.getItemName());

        //個数を設定
        TextView tvItemQuantity = convertView.findViewById(R.id.tvQuantityRow);
        tvItemQuantity.setText(String.valueOf(item.getItemQuantity()));

        //チェックボックス情報を設定
        CheckBox cbItemComplete = convertView.findViewById(R.id.cbCompleteRow);
        cbItemComplete.setChecked(item.getItemCompleteFlag());


        //チェックボックスが押された時のリスナーを設定
        cbItemComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //チェックボックスからカラムのビューを取得する

                /* 親ビューを取得  */
                View parentView = (View)v.getParent();
                TextView itemName = parentView.findViewById(R.id.tvItemNameRow);
                String itemNameStr = itemName.getText().toString();
                Log.d("tag",itemNameStr+"のチェックボックスがタップされました。"+position);
                //タップされたリストデータを取得
                Item item = itemList.get(position);
                int item_id = item.getItemId();
                //コンプリートフラグの値を取得
                CheckBox chkbox = (CheckBox)v;
                boolean complete_val = chkbox.isChecked();
                int complete_flag_int;
                if(complete_val){
                    complete_flag_int = 1;
                }else{
                    complete_flag_int = 0;
                }

                //ログを生成
                String complete_str = String.valueOf(complete_val);
                Log.d("tag",itemNameStr+complete_str);

                //データベースのコンプリートフラグを更新する
                _helper = new DatabaseHelper(getContext());
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
                SQLiteDatabase db = _helper.getWritableDatabase();
                //インサート用SQL文字列の用意
                String sqlUPDATE = "UPDATE shopping_list SET complete_flag = ? WHERE item_id = ?";
                //SQL文字列をもとにプリペアドステートメントを取得
                SQLiteStatement stmt = db.compileStatement(sqlUPDATE);
                //変数のバインド
                stmt.bindLong(1, complete_flag_int);
                stmt.bindLong(2, item_id);
                //インサートSQLの実行
                stmt.executeUpdateDelete();
            }
        });
//        btItemMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(item.getItemQuantity() >= 1){
//                    item.setItemQuantity(item.getItemQuantity() - 1);      //個数を減らしています
//                }
//                if(item.getItemQuantity() < 1){         //個数が1より小さい、つまり0個になったら-ボタンを押せなくします
//                    btItemMinus.setEnabled(false);
//                }
//                tvItemQuantity.setText(String.valueOf(item.getItemQuantity()));
//            }
//        });

        return convertView;
    }
}
