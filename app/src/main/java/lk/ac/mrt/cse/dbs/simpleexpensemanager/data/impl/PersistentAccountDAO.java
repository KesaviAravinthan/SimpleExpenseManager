package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;



public class PersistentAccountDAO implements AccountDAO{

    private final DBHelper db;
    public PersistentAccountDAO(DBHelper db) {

        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = db.getAccountNumber();
        List<String> accountNumberList = new ArrayList<String>();
        if(cursor.getCount() == 0){
            return accountNumberList;
        }
        while(cursor.moveToNext()){
            String accountNo = cursor.getString(0);
            accountNumberList.add(accountNo);

        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {

        Cursor cursor = db.getAccounts();
        List<Account> accountList = new ArrayList<Account>();
        if(cursor.getCount()==0){
            return accountList;
        }
        while(cursor.moveToNext()){
            String accountNo = cursor.getString(0);
            String bankName=cursor.getString(1);;
            String accountHolderName= cursor.getString(2);;
            Double balance = cursor.getDouble(3);
//            String accountNo, String bankName, String accountHolderName, double balance
            Account newAccount = new Account(accountNo,bankName,accountHolderName,balance);
            accountList.add(newAccount);

        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = db.getAccount(accountNo);
        if (cursor.getCount()>0) {
            String account_No = cursor.getString(0);
            String bankName=cursor.getString(1);;
            String accountHolderName= cursor.getString(2);;
            Double balance = cursor.getDouble(3);
            Account newAccount = new Account(account_No,bankName,accountHolderName,balance);
            return newAccount;
        }

        String msg = "Account Number : " + accountNo + " is Not Exist.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        String acc_no =   account.getAccountNo();
        String bank = account.getBankName();
        String owner = account.getAccountHolderName();
        Double balance = account.getBalance();
        db.insertAccount(acc_no,bank,owner,balance);
    }

    @Override
    public void removeAccount(String acc_no) throws InvalidAccountException {
        db.deleteAccount(acc_no);
    }

    @Override
    public void updateBalance(String acc_no, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(acc_no);
        if(!(account instanceof Account)){
            String msg = "Account Number : " + acc_no + " is Not Exist.";
            throw new InvalidAccountException(msg);

        }

        double updatedBalance = account.getBalance();

        switch (expenseType) {
            case EXPENSE:
                updatedBalance = account.getBalance() - amount;
                account.setBalance(updatedBalance);
                break;
            case INCOME:
                updatedBalance = account.getBalance() + amount;
                account.setBalance(updatedBalance);
                break;
        }

        db.updateBalance(acc_no,updatedBalance);
    }
}