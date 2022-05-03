package com.websarva.wings.android.android_shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ItemEditActivity extends AppCompatActivity {
    //データベースヘルパーオブジェクト
    private DatabaseHelper _helper;

    private int _itemId;
    private String _itemName;
    private int _itemQuantity;

    EditText _etItemName;
    EditText _etItemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_edit);
        //ヘルパーオブジェクトを取得
        _helper = new DatabaseHelper(this);

        //インテントオブジェクトを取得
        Intent intent = getIntent();
        //前画面から渡されたデータを取得
        _itemId = intent.getIntExtra("itemId",0);
        _itemName = intent.getStringExtra("itemName");
        _itemQuantity = intent.getIntExtra("itemQuantity",1);

        Log.d("dbtag", String.valueOf(_itemId));
        Log.d("dbtag", _itemName);
        Log.d("dbtag", String.valueOf(_itemQuantity));

        //各ビューにデータを設定する
        _etItemName = findViewById(R.id.et_ItemName);
        _etItemName.setText(_itemName);
        _etItemQuantity = findViewById(R.id.et_itemQuantity);
        _etItemQuantity.setText(String.valueOf(_itemQuantity));


        Button btSave = findViewById(R.id.bt_save);
        btSave.setOnClickListener(new saveBtnClickListener());
    }


    //戻るボタン
    public void onBackButtonClick(View view) {
        finish();
    }

    //保存ボタンのクリックリスナー
    //追加ボタンを押したときのリスナクラス
    private class saveBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //アイテム名を取得する
            String itemName = _etItemName.getText().toString();
            //アイテム数を取得
            Editable getQuantity = _etItemQuantity.getText();
            int itemQuantity =Integer.parseInt(getQuantity.toString());
            Log.d("tag", String.valueOf(itemQuantity));


            if(_itemName != itemName){
                //アイテム名が異なる場合は更新処理を行う
                SQLiteDatabase db = _helper.getWritableDatabase();
                //アップデート用SQL文字列の用意
                String sqlUPDATE = "UPDATE shopping_list SET item_name = ? WHERE item_id = ?";
                //SQL文字列をもとにプリペアドステートメントを取得
                SQLiteStatement stmt = db.compileStatement(sqlUPDATE);
                //変数のバインド
                stmt.bindString(1, itemName);
                stmt.bindLong(2, _itemId);
                //インサートSQLの実行
                stmt.executeUpdateDelete();
                Log.d("tag","nameUpdateが実行");
            }

            if(_itemQuantity != itemQuantity){
                //アイテム名が異なる場合は更新処理を行う
                SQLiteDatabase db = _helper.getWritableDatabase();
                //アップデート用SQL文字列の用意
                String sqlUPDATE = "UPDATE shopping_list SET item_quantity = ? WHERE item_id = ?";
                //SQL文字列をもとにプリペアドステートメントを取得
                SQLiteStatement stmt = db.compileStatement(sqlUPDATE);
                //変数のバインド
                stmt.bindLong(1, itemQuantity);
                stmt.bindLong(2, _itemId);
                //インサートSQLの実行
                stmt.executeUpdateDelete();
                Log.d("tag","個数Updateが実行");
            }
            finish();
        }
    }
}