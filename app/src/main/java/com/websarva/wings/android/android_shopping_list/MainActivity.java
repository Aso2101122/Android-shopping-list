package com.websarva.wings.android.android_shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //データベースヘルパーオブジェクト
    private DatabaseHelper _helper;

    private ListView _lvItem;
    private ListView _lvCompleteItem;
    private List<Item> _unCompleteList;
    private List<Item> _completeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _helper = new DatabaseHelper(MainActivity.this);

        //アイテムを表示するListViewを取得
        _lvItem = findViewById(R.id.item_unComplete_lv);
        //dbから買い物リストを取得
        _unCompleteList = getShoppingList(0);
        //MyAdapterを作成
        ItemAdapter adapter = new ItemAdapter(MainActivity.this, R.layout.row, _unCompleteList);
        //アダプタの登録
        _lvItem.setAdapter(adapter);

        //完了リスト
        _lvCompleteItem = findViewById(R.id.item_complete_lv);
        //dbから買い物リストを取得
        _completeList = getShoppingList(1);
        //ItemAdapterを作成
        adapter = new ItemAdapter(MainActivity.this, R.layout.row, _completeList);
        //アダプタの登録
        _lvCompleteItem.setAdapter(adapter);




        //追加ボタンオブジェクトを取得
        Button addBtn = findViewById(R.id.item_add_btn);
        //追加ボタンににリスナを設定
        addBtn.setOnClickListener(new btnClickListener());

        // リストがタップされた時のイベント
        _lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tag",String.valueOf(position)+"番目がクリックされました。");
                Intent intent = new Intent(MainActivity.this, ItemEditActivity.class);
                //アイテムidをLong型から、intにキャストする
                int putId = (int) id;

                //アイテム名を取得
                TextView tvItemName = view.findViewById(R.id.tvItemNameRow);
                String itemName = tvItemName.getText().toString();

                //数を取得
                TextView tvItemQuantity = view.findViewById(R.id.tvQuantityRow);
                String ItemQuantityStr = tvItemQuantity.getText().toString();
                int itemQuantity = Integer.parseInt(ItemQuantityStr);

                //データを取得
                Log.d("tag", String.valueOf(id)+"主キー");


                //データを格納
                intent.putExtra("itemId", putId);
                intent.putExtra("itemName", itemName);
                intent.putExtra("itemQuantity", itemQuantity);

                startActivity(intent);
            }
        });



        //更新ボタンが押された時のリスナを設定
        //追加ボタンオブジェクトを取得
        Button updateBtn = findViewById(R.id.bt_reacquire);
        //更新ボタンににリスナを設定
        updateBtn.setOnClickListener(new reacquireClickListener());
    }

    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放
        _helper.close();
        super.onDestroy();
    }

    //未完了の買い物リストデータを取得するメソッド
    protected List<Item> getShoppingList(int complete_flag){
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        String sql = "";
        //引数complete_flagが0の時は未完了、1の時は完了したアイテムを取得するSQL文を用意する
        if(complete_flag == 1){
            //完了済みのアイテムを取得するSQL文を用意
            sql = "SELECT * FROM shopping_list WHERE complete_flag = 1 ORDER BY item_id desc";
        }else if(complete_flag == 0){
            //未完了のアイテムを取得するSQL文を用意
            sql = "SELECT * FROM shopping_list WHERE complete_flag = 0  ORDER BY item_id desc";
        }

        //SQLを実行
        Cursor cursor = db.rawQuery(sql, null);
        //Adapterで使用するListオブジェクト
        List<Item> itemList = new ArrayList<>();
        //アイテムidとアイテム名と数と完了フラグを取得する
        while(cursor.moveToNext()){
            //カラムのインデックスの取得
            int idxItemId = cursor.getColumnIndex("item_id");
            int idxItemName = cursor.getColumnIndex("item_name");
            int idxQuantity = cursor.getColumnIndex("item_quantity");
            int idxComplete_flag = cursor.getColumnIndex("complete_flag");

            // インデックスをもとにデータを取得
            int itemId = cursor.getInt(idxItemId);
            String  itemName = cursor.getString(idxItemName);
            int itemQuantity = cursor.getInt(idxQuantity);
            int itemCompleteFlag = cursor.getInt(idxComplete_flag);

            // itemオブジェクトをインスタンス化して、データを格納する
            Item item = new Item(itemId, itemName, itemQuantity, itemCompleteFlag);

            //itemオブジェクトをListに格納する
            itemList.add(item);

            Log.d("tag",cursor.getString(idxItemName)+cursor.getLong(idxComplete_flag));
        }
        cursor.close();
        return itemList;
    }

    //追加ボタンを押したときのリスナクラス
    private class btnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            if(view.getId() == R.id.item_add_btn){
                //追加ボタンが押された時の処理
                //ボタンが押されているか
                Log.d("tag","Onclick");
                //アイテム名を取得する
                EditText edItemName = findViewById(R.id.item_name_et);
                String itemName = edItemName.getText().toString();
                if(itemName.length() >= 1) {
                    //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
                    SQLiteDatabase db = _helper.getWritableDatabase();
                    //インサート用SQL文字列の用意
                    String sqlInsert = "INSERT INTO shopping_list (item_name, item_quantity, complete_flag) VALUES (?,1,0)";
                    //SQL文字列をもとにプリペアドステートメントを取得
                    SQLiteStatement stmt = db.compileStatement(sqlInsert);
                    //変数のバインド
                    stmt.bindString(1, itemName);
                    //インサートSQLの実行
                    stmt.executeInsert();

                    edItemName.setText("");
                    Toast.makeText(MainActivity.this, itemName +"を追加しました。", Toast.LENGTH_SHORT).show();

                    //リストを再描画する
                    List<Item> newItemList = getShoppingList(0);
                    //ItemAdapterを作成
                    ItemAdapter adapter = new ItemAdapter(MainActivity.this, R.layout.row, newItemList);
                    //アダプタの登録
                    _lvItem.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    //更新ボタンを押された時のリスナクラス
    private class reacquireClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            List<Item> newItemList = getShoppingList(0);
            //ItemAdapterを作成
            ItemAdapter adapter = new ItemAdapter(MainActivity.this, R.layout.row, newItemList);
            //アダプタの登録
            _lvItem.setAdapter(adapter);

            //完了済みリストの更新
            newItemList = getShoppingList(1);
            //ItemAdapterを作成
            adapter = new ItemAdapter(MainActivity.this, R.layout.row, newItemList);
            //アダプタの登録
            _lvCompleteItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d("tag", "リストを再取得");
        }
    }

    //チェックボックスが押された時に画面を更新する
    private class checkboxClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            List<Item> newItemList = getShoppingList(0);
            //ItemAdapterを作成
            ItemAdapter adapter = new ItemAdapter(MainActivity.this, R.layout.row, newItemList);
            //アダプタの登録
            _lvItem.setAdapter(adapter);

            //完了済みリストの更新
            newItemList = getShoppingList(1);
            //ItemAdapterを作成
            adapter = new ItemAdapter(MainActivity.this, R.layout.row, newItemList);
            //アダプタの登録
            _lvCompleteItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d("tag", "リストを再取得");
        }
    }
}