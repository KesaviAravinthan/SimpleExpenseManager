package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DBHelper db;

    public PersistentTransactionDAO(DBHelper db){
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String stringDate = dateFormat.format(date);
        db.insertTransaction(stringDate,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        Cursor res = db.getTransactions();
        List<Transaction> transactionList = new ArrayList<Transaction>();
        if(res.getCount()==0){
            return transactionList;
        }

        while(res.moveToNext()){
            ExpenseType expenseType;
            String accountNo=res.getString(1);;
            String dateString = res.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
            Date date =formatter.parse(dateString);
            Double amount= res.getDouble(3);
            String expenseTypeString = res.getString(4);
            Log.d("myTag", expenseTypeString);

            expenseType = ExpenseType.valueOf(expenseTypeString);
            Transaction trans = new Transaction(date,accountNo,expenseType,amount);
            transactionList.add(trans);

        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        Cursor cursor = db.getLimitedTransactions(limit);
        List<Transaction> limitedTransactionList = new ArrayList<Transaction>();
        if(cursor.getCount()==0){
            return limitedTransactionList;
        }
        while(cursor.moveToNext()){
            String accountNo=cursor.getString(1);;
            String date_temp = cursor.getString(2);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
            Date date =formatter.parse(date_temp);
            Double amount= cursor.getDouble(3);
            String expenseTypeString = cursor.getString(4);
            ExpenseType expenseType = ExpenseType.valueOf(expenseTypeString);
//            Date date, String accountNo, ExpenseType expenseType, double amount
            Transaction trans = new Transaction(date,accountNo,expenseType,amount);
            limitedTransactionList.add(trans);

        }

        return limitedTransactionList;
    }
}
