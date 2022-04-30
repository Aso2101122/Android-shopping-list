package com.websarva.wings.android.android_shopping_list;

public class Item {
    private int itemId;
    private String itemName;
    private int itemQuantity;
    private int itemCompleteFlag;

    //コンストラクタ
    public Item(int itemId, String itemName, int itemQuantity, int itemCompleteFlag){
        setItemId(itemId);
        setItemName(itemName);
        setItemQuantity(itemQuantity);
        setItemCompleteFlag(itemCompleteFlag);
    }

    //セッター
    public void setItemId(int itemId){
        this.itemId = itemId;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    public void setItemQuantity(int itemQuantity){
        this.itemQuantity = itemQuantity;
    }
    private void setItemCompleteFlag(int itemCompleteFlag) {
        this.itemCompleteFlag = itemCompleteFlag;
    }
    //ゲッター
    public int getItemId(){
        return itemId;
    }
    public String getItemName(){
        return itemName;
    }
    public int getItemQuantity(){
        return itemQuantity;
    }
    public boolean getItemCompleteFlag() {
        if(itemCompleteFlag == 1){
            return true;
        }else{
            return false;
        }

    }

}
