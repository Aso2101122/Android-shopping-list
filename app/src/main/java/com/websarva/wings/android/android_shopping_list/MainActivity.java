package com.websarva.wings.android.android_shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //データベースヘルパーオブジェクト
    private DatabaseHelper _helper;
    private ListView _lvItem;
    private List<Item> _unCompleteList;
    private static final String[] FROM = {"item_name","quantity"};
    private static final int[] TO = {R.id.tvItemNameRow,R.id.tvQuantityRow};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _helper = new DatabaseHelper(MainActivity.this);

        //アイテムを表示するListViewを取得
        _lvItem = findViewById(R.id.item_lv);
        //dbから買い物リストを取得
        _unCompleteList = getShoppingList();
        //MyAdapterを作成
        ItemAdapter adapter = new ItemAdapter(MainActivity.this, R.layout.row, _unCompleteList);
        //アダプタの登録
        _lvItem.setAdapter(adapter);

        //追加ボタンオブジェクトを取得
        Button addBtn = findViewById(R.id.item_add_btn);
        //追加ボタンににリスナを設定
        addBtn.setOnClickListener(new btnClickListener());

        // リストがタップされた時のイベント
        _lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.setTitle(String.valueOf(position)+"番目がクリックされました。");
            }
        });

        //チェックボックスのリスナーを設定

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
    protected List<Item> getShoppingList(){
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        //検索SQL文字列の用意
        String sql = "SELECT * FROM shopping_list WHERE complete_flag = 0";
        //SQLを実行
        Cursor cursor = db.rawQuery(sql, null);
        //Adapterで使用するListオブジェクト
        List<Item> itemList = new ArrayList<>();
        //アイテムidとアイテム名と数と完了フラグを取得する
        while(cursor.moveToNext()){
            //カラムのインデックスの取得
            int idxItemId = cursor.getColumnIndex("item_id");
            int idxItemName = cursor.getColumnIndex("item_name");
            int idxQuantity = cursor.getColumnIndex("quantity");
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

            Log.d("tag",cursor.getString(idxItemName));
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
                    String sqlInsert = "INSERT INTO shopping_list (item_name, quantity, complete_flag) VALUES (?,1,0)";
                    //SQL文字列をもとにプリペアドステートメントを取得
                    SQLiteStatement stmt = db.compileStatement(sqlInsert);
                    //変数のバインド
                    stmt.bindString(1, itemName);
                    //インサートSQLの実行
                    stmt.executeInsert();

                    edItemName.setText("");
                    Toast.makeText(MainActivity.this, "アイテムを追加しました。", Toast.LENGTH_SHORT).show();

                    //リストを再描画する
                    List<Item> newItemList = getShoppingList();
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
            List<Item> newItemList = getShoppingList();
            //ItemAdapterを作成
            ItemAdapter adapter = new ItemAdapter(MainActivity.this, R.layout.row, newItemList);
            //アダプタの登録
            _lvItem.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d("tag", "リストを再取得");
        }
    }
}