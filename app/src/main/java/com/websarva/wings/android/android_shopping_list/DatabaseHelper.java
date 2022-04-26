package com.websarva.wings.android.android_shopping_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //データベースファイル名の定数ファイル
    private static final String DATABASE_NAME = "shopping_list.db";
    //バージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 1;

    //コンストラクタ
    public DatabaseHelper(Context context){
        //親クラスのコンストラクタの呼び出し
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル作成用SQL文字列の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE shopping_list(");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("item_name TEXT,");
        sb.append("quantity INTEGER,");
        sb.append("complete_flag INTEGER");
        sb.append(");");
        String sql = sb.toString();

        //SQLの実行
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
