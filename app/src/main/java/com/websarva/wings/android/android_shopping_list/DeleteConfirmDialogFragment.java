package com.websarva.wings.android.android_shopping_list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmDialogFragment extends DialogFragment {

    //データベースヘルパーオブジェクト
    private DatabaseHelper _helper;

    private int _itemId;
    private String _itemName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 値を受け取る
        _itemId= getArguments().getInt("item_id", 0);
        _itemName = getArguments().getString("item_name", "");


        //ダイアログビルダーを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ダイアログログのタイトルを設定
        builder.setTitle(R.string.dialog_title);
        //ダイアログのメッセージを設定
        builder.setMessage("「" + _itemName + "」" + getString(R.string.dialog_msg));
        //Positive Buttonを設定
        builder.setPositiveButton(R.string.dialog_btn_ok, new DialogButtonClickListener());
        //Negative Buttonを設定
        builder.setNegativeButton(R.string.dialog_btn_ng, new DialogButtonClickListener());
        //ダイアログオブジェクトを生成し、リターン。
        AlertDialog dialog = builder.create();
        return dialog;
    }


    //ダイアログのアクションボタンがタップされた時の処理が記述されたメンバクラス
    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // トーストメッセージ用文字列変数を用意
            String msg = "";
            // タップされたアクションボタンで分離
            switch (which) {
                //Positive Buttonならば
                case DialogInterface.BUTTON_POSITIVE:
                    //DBから削除する

                    DatabaseHelper _helper = new DatabaseHelper(getContext());
                    //アイテム名が異なる場合は更新処理を行う
                    SQLiteDatabase db = _helper.getWritableDatabase();
                    //アップデート用SQL文字列の用意
                    String sqlDELETE = "DELETE FROM shopping_list WHERE item_id = ?";
                    //SQL文字列をもとにプリペアドステートメントを取得
                    SQLiteStatement stmt = db.compileStatement(sqlDELETE);
                    //変数のバインド
                    stmt.bindLong(1, _itemId);
                    //インサートSQLの実行
                    stmt.executeUpdateDelete();
                    msg = "「" + _itemName + "」" + "を削除しました";
                    Log.d("tag", "DELETEが実行");
                    getActivity().finish();
                    break;
                //Negative Buttonならば
                case DialogInterface.BUTTON_NEGATIVE:
                    //キャンセル用のメッセージを格納
                    msg = "キャンセルしました";
                    Log.d("tag", "DELETEをキャンセル");
                    break;
            }
            //トーストの表示
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}