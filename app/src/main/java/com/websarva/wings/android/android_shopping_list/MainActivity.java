package com.websarva.wings.android.android_shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
        _unCompleteList = getShoppingList();
        //SimpleAdapterを作成
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _unCompleteList, R.layout.row, FROM, TO);
        //アダプタの登録
        _lvItem.setAdapter(adapter);
//        Log.d("tag",_unCompleteList.get(0).get("item_name").toString());
//        Log.d("tag",_unCompleteList.get(1).get("item_name").toString());
//        Log.d("tag",_unCompleteList.get(2).get("item_name").toString());
//        Log.d("tag",_unCompleteList.get(3).get("item_name").toString());

        //追加ボタンオブジェクトを取得
        Button addBtn = findViewById(R.id.item_add_btn);
        //追加ボタンににリスナを設定
        addBtn.setOnClickListener(new btnClickListener());

        //チェックボックスのリスナを設定
        CheckBox chkBox = findViewById(R.id.cbCompleteRow);
        //チェックボタンにリスナを設定

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
        int idxItemName = 0;
        int idxQuantity;
        int idxComplete_flag;
        //アイテム名と数と完了フラグを取得する
        while(cursor.moveToNext()){
            idxItemName = cursor.getColumnIndex("item_name");
            idxQuantity = cursor.getColumnIndex("quantity");
            idxComplete_flag = cursor.getColumnIndex("complete_flag");
            item.put("item_name", cursor.getString(idxItemName));
            item.put("quantity", cursor.getString(idxQuantity));
            item.put("complete_flag", cursor.getString(idxComplete_flag));
            itemList.add(item);
            Log.d("tag",cursor.getString(idxItemName));
            //Mapを初期化
            item = new HashMap<>();
        }
        cursor.close();
        Log.d("tag",itemList.get(0).get("item_name").toString());
        Log.d("tag",itemList.get(1).get("item_name").toString());
        Log.d("tag",itemList.get(2).get("item_name").toString());
        Log.d("tag",itemList.get(3).get("item_name").toString());
        Log.d("tag",itemList.get(4).get("item_name").toString());
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
                }
            }
        }
    }

    //チェックボックスがオンになったときのリスナクラス
}