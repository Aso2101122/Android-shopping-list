package com.websarva.wings.android.android_shopping_list;

import android.app.LauncherActivity;
import android.content.Context;
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


//        cbItemComplete.setOnClickListener(new View.OnClickListener() {  //分かりやすいように、ラムダ式をあえて避けました
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
