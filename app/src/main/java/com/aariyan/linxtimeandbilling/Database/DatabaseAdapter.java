package com.aariyan.linxtimeandbilling.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.aariyan.linxtimeandbilling.Model.CustomerModel;
import com.aariyan.linxtimeandbilling.Model.TimingModel;
import com.aariyan.linxtimeandbilling.Model.UserListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {


    DatabaseHelper helper;
    private List<UserListModel> list = new ArrayList<>();
    private List<CustomerModel> customerList = new ArrayList<>();
    private List<TimingModel> timingList = new ArrayList<>();
    private List<TimingModel> allJob = new ArrayList<>();

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    //Insert user data
    public long insertUserData(String uID, String pinCode, String name, String companyId) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.uID, uID);
        contentValues.put(DatabaseHelper.strPinCode, pinCode);
        contentValues.put(DatabaseHelper.strName, name);
        contentValues.put(DatabaseHelper.intCompanyID, companyId);

        long id = database.insert(DatabaseHelper.USER_TABLE_NAME, null, contentValues);
        return id;
    }

    //Getting all the user
    public List<UserListModel> getUserData() {

        list.clear();
        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.uID, DatabaseHelper.strPinCode, DatabaseHelper.strName, DatabaseHelper.intCompanyID};
        Cursor cursor = database.query(DatabaseHelper.USER_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            UserListModel model = new UserListModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            list.add(model);
        }
        return list;

    }

    //Insert customer data
    public long insertCustomerData(String strCustName, String strCustDesc, String Uid) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.strCustName, strCustName);
        contentValues.put(DatabaseHelper.strCustDesc, strCustDesc);
        contentValues.put(DatabaseHelper.Uid, Uid);

        long id = database.insert(DatabaseHelper.CUSTOMER_TABLE_NAME, null, contentValues);
        return id;
    }

    //Getting all the Customer
    public List<CustomerModel> getAlLCustomer() {

        customerList.clear();
        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.strCustName, DatabaseHelper.strCustDesc, DatabaseHelper.Uid};
        Cursor cursor = database.query(DatabaseHelper.CUSTOMER_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            CustomerModel model = new CustomerModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            customerList.add(model);
        }
        return customerList;

    }

    //Insert Timing data
    public long insertTimingData(String USER_NAME, String CUSTOMER_NAME, String START_DATE, String BILLABLE_TIME,
                                 String STATUS, String TOTAL_TIME, String WORK_TYPE, String DESCRIPTION) {

        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_NAME, USER_NAME);
        contentValues.put(DatabaseHelper.CUSTOMER_NAME, CUSTOMER_NAME);
        contentValues.put(DatabaseHelper.START_DATE, START_DATE);
        contentValues.put(DatabaseHelper.BILLABLE_TIME, BILLABLE_TIME);
        contentValues.put(DatabaseHelper.STATUS, STATUS);
        contentValues.put(DatabaseHelper.TOTAL_TIME, TOTAL_TIME);
        contentValues.put(DatabaseHelper.WORK_TYPE, WORK_TYPE);
        contentValues.put(DatabaseHelper.DESCRIPTION, DESCRIPTION);

        long id = database.insert(DatabaseHelper.TIMING_TABLE_NAME, null, contentValues);
        return id;
    }


    //getTiming
    public List<TimingModel> getTiming(String userName, String customerName) {

        timingList.clear();
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from tableName where name = ? and customerName = ?:
        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
        String selection = DatabaseHelper.USER_NAME + "=?" + " and " + DatabaseHelper.CUSTOMER_NAME + "=?";

        Log.d("NAME_TEST", userName + " -> " + customerName);

        String[] args = {userName, customerName};
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.USER_NAME, DatabaseHelper.CUSTOMER_NAME, DatabaseHelper.START_DATE,
                DatabaseHelper.BILLABLE_TIME, DatabaseHelper.STATUS, DatabaseHelper.TOTAL_TIME, DatabaseHelper.WORK_TYPE, DatabaseHelper.DESCRIPTION};

        Cursor cursor = database.query(DatabaseHelper.TIMING_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {

            TimingModel model = new TimingModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            timingList.add(model);
        }
        return timingList;

    }

    //getTiming
    public List<TimingModel> getAllJob() {

        allJob.clear();
        SQLiteDatabase database = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.USER_NAME, DatabaseHelper.CUSTOMER_NAME, DatabaseHelper.START_DATE,
                DatabaseHelper.BILLABLE_TIME, DatabaseHelper.STATUS, DatabaseHelper.TOTAL_TIME, DatabaseHelper.WORK_TYPE, DatabaseHelper.DESCRIPTION};

        Cursor cursor = database.query(DatabaseHelper.TIMING_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            TimingModel model = new TimingModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            allJob.add(model);
        }
        return allJob;

    }

    //Delete timing
    public long deleteTiming(String userName, String customerName, int id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String selection = DatabaseHelper.USER_NAME + " LIKE ? AND " + DatabaseHelper.CUSTOMER_NAME + " LIKE ? AND " + DatabaseHelper.UID + " LIKE ?";

        String[] args = {userName, customerName, ""+id};
        long ids = database.delete(DatabaseHelper.TIMING_TABLE_NAME, selection, args);

        return ids;
    }

//
//    public int updatePoLines(String name, int quantity) {
//
//        SQLiteDatabase database = helper.getWritableDatabase();
//        //drop the table if exist:
//        //Create table:
////        try {
////
////            if (vendorCheck == 0) {
////                database.execSQL(DatabaseHelper.DROP_VENDORS_TABLE);
////                database.execSQL(DatabaseHelper.CREATE_VENDORS_TABLE);
////            }
////            vendorCheck++;
////        } catch (Exception e) {
////            e.printStackTrace();
////
////        }
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.decBuyQtyScanned, quantity);
//        String[] args = {name};
//        int id = database.update(DatabaseHelper.PO_LINE_TABLE_NAME, contentValues, DatabaseHelper.strPartNumber + " =? ", args);
//        return id;
//    }


    class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        private static final String DATABASE_NAME = "linx_billing.db";
        private static final int VERSION_NUMBER = 6;

        //User Table:
        private static final String USER_TABLE_NAME = "users";
        private static final String UID = "_id";
        private static final String uID = "uID";
        private static final String strPinCode = "strPinCode";
        private static final String strName = "strName";
        private static final String intCompanyID = "strVendDesc";
        //Creating the table:
        private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + uID + " VARCHAR(255),"
                + strPinCode + " VARCHAR(255),"
                + strName + " VARCHAR(255),"
                + intCompanyID + " VARCHAR(255));";
        private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;

        //Customer table:
        private static final String CUSTOMER_TABLE_NAME = "customers";
        private static final String strCustName = "strCustName";
        private static final String strCustDesc = "strCustDesc";
        private static final String Uid = "Uid";
        //Creating the table:
        private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + strCustName + " VARCHAR(255),"
                + strCustDesc + " VARCHAR(255),"
                + Uid + " VARCHAR(255));";
        private static final String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + CUSTOMER_TABLE_NAME;


        //Timing table:
        private static final String TIMING_TABLE_NAME = "timing";
        private static final String USER_NAME = "userName";
        private static final String CUSTOMER_NAME = "customerName";
        private static final String START_DATE = "startDate";
        private static final String BILLABLE_TIME = "billableTime";
        private static final String STATUS = "status";
        private static final String TOTAL_TIME = "totalTime";
        private static final String WORK_TYPE = "workType";
        private static final String DESCRIPTION = "description";
        //Creating the table:
        private static final String CREATE_TIMING_TABLE = "CREATE TABLE " + TIMING_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_NAME + " VARCHAR(255),"
                + CUSTOMER_NAME + " VARCHAR(255),"
                + START_DATE + " VARCHAR(255),"
                + BILLABLE_TIME + " VARCHAR(255),"
                + STATUS + " VARCHAR(255),"
                + TOTAL_TIME + " VARCHAR(255),"
                + WORK_TYPE + " VARCHAR(255),"
                + DESCRIPTION + " VARCHAR(255));";
        private static final String DROP_TIMING_TABLE = "DROP TABLE IF EXISTS " + TIMING_TABLE_NAME;


        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUMBER);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Create table:
            try {
                db.execSQL(CREATE_USER_TABLE);
                db.execSQL(CREATE_CUSTOMER_TABLE);
                db.execSQL(CREATE_TIMING_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_USER_TABLE);
                db.execSQL(DROP_CUSTOMER_TABLE);
                db.execSQL(DROP_TIMING_TABLE);
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
