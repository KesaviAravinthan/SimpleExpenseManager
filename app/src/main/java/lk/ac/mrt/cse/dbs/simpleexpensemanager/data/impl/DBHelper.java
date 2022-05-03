package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBHelper extends SQLiteOpenHelper {
    public static final String database_name = "190049P.db";
    public DBHelper(Context context){
        super(context, database_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE ACCOUNT (acc_no VARCHAR(20) PRIMARY KEY, bank VARCHAR(20), owner varchar(20), balance DECIMAL(10,2))"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE Trans (tr_id INTEGER PRIMARY KEY AUTOINCREMENT , acc_no VARCHAR(20) , date VARCHAR(20), amount  DECIMAL(10,2), type VARCHAR(20) )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ACCOUNT");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Trans");
        onCreate(sqLiteDatabase);
    }



    public boolean insertAccount (String acc_no, String bank, String owner, double balance) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_no", acc_no);
        contentValues.put("bank", bank);
        contentValues.put("owner", owner);
        contentValues.put("balance", balance);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{acc_no});

        // check already account exist
        if (cursor.getCount()== 0) {
            long isSuccess = sqLiteDatabase.insert("ACCOUNT", null, contentValues);
            if(isSuccess==-1){
                return false;
            }
            return true;
        }

        return false;
    }

    public boolean insertTransaction(String date, String acc_no, ExpenseType type, double amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_no", acc_no);
        contentValues.put("date", date);
        contentValues.put("amount", amount );
        contentValues.put("type", type.toString());

        long isSuccess = sqLiteDatabase.insert("Trans",null,contentValues);
        if(isSuccess==-1){
            return false;
        }
        return true;
    }
    public boolean updateBalance(String acc_no,  double balance) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", balance);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?",new String[]{acc_no});

        if (cursor.getCount() > 0) {
            long isSuccess = sqLiteDatabase.update("ACCOUNT", contentValues, "acc_no=?",new String[]{acc_no});
            if (isSuccess == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean deleteAccount(String acc_no) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?",new String[]{acc_no});

        if (cursor.getCount() > 0) {
            long isSuccess = sqLiteDatabase.delete("ACCOUNT", "acc_no=?", new String[]{acc_no});
            if (isSuccess == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Cursor getAccount(String acc_no){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{acc_no});
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAccounts(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNT", null);
        return cursor;
    }

    public Cursor getAccountNumber(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT acc_no FROM ACCOUNT", null);
        return cursor;
    }

    public Cursor getTransactions(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Trans", null);
        return cursor;
    }
    public Cursor getLimitedTransactions(int count){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Trans ORDER BY tr_id DESC LIMIT " +Integer.toString(count) , null);
        return cursor;
    }







}
