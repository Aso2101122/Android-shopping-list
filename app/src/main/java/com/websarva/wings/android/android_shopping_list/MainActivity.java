package com.websarva.wings.android.android_shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //データベースヘルパーオブジェクト
    private DatabaseHelper _helper;
    private ListView _lvItem;
    private List<Map<String, Object>> _unCompleteList;
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
        List<Map<String, Object>> _unCompleteList = getShoppingList();
        //SimpleAdapterを作成
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _unCompleteList, R.layout.row, FROM, TO);
        //アダプタの登録
        _lvItem.setAdapter(adapter);
    }

    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放
        _helper.close();
        super.onDestroy();
    }

    //未完了の買い物リストデータを取得するメソッド
    protected List<Map<String, Object>> getShoppingList(){
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();
        //検索SQL文字列の用意
        String sql = "SELECT * FROM shopping_list WHERE complete_flag = 0";
        //SQLを実行
        Cursor cursor = db.rawQuery(sql, null);
        //Adapterで使用するListオブジェクト
        List<Map<String,Object>> itemList = new ArrayList<>();
        //マップオブジェクトの用意とitemListへのデータ登録
        Map<String, Object> item = new HashMap<>();
        //アイテム名と数と完了フラグを取得する
        while(cursor.moveToNext()){
            int idxItem_name = cursor.getColumnIndex("item_name");
            int idxQuantity = cursor.getColumnIndex("item_name");
            int idxComplete_flag = cursor.getColumnIndex("item_name");
            item.put("item_name", cursor.getString(idxItem_name));
            item.put("quantity", cursor.getString(idxQuantity));
            item.put("complete_flag", cursor.getString(idxComplete_flag));
            itemList.add(item);
        }
        return itemList;
    }


}