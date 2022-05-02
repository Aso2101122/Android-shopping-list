package com.websarva.wings.android.android_shopping_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ItemEditActivity extends AppCompatActivity {

    private int _itemId;
    private String _itemName;
    private int _itemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        //インテントオブジェクトを取得
        Intent intent = getIntent();
        //前画面から渡されたデータを取得
        _itemId = intent.getIntExtra("itemId",0);
        _itemName = intent.getStringExtra("itemName");
        _itemQuantity = intent.getIntExtra("itemQuantity",1);

        //各ビューにデータを設定する
        EditText etItemName = findViewById(R.id.et_ItemName);
        etItemName.setText(_itemName);
        EditText etItemQuantity = findViewById(R.id.et_itemQuantity);
        etItemQuantity.setText(String.valueOf(_itemQuantity));


    }


    //戻るボタン
    public void onBackButtonClick(View view) {
        finish();
    }
}