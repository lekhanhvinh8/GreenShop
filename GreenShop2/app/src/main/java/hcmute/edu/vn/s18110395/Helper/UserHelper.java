package hcmute.edu.vn.s18110395.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;

import java.io.File;

public class UserHelper extends SQLiteOpenHelper {
    private final static String TAG = "UserHelper";
    private final Context myContext;
    private static final String DATABASE_NAME = "greenshop.db";
    private static final int DATABASE_VERSION = 1;
    private String pathToSaveDBFile;

    public UserHelper(Context context, String filePath){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_NAME).toString();
    }

    public Boolean login(String phoneNumber, String password) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT id, phonenumber, password FROM Users Where Users.PhoneNumber='" + phoneNumber + "'";
        Cursor cursor = db.rawQuery(query, null);

        Boolean flag = false;

        while(cursor.moveToNext()) {
            if(cursor.getString(2).equals(password))
                flag = true;
        }

        db.close();

        return flag;
    }

    public Boolean register(String phoneNumber, String password){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        String query = "INSERT into Users(PhoneNumber, Password, RoleId) VALUES('" + phoneNumber + "', '" + password + "', 1)";

        Log.d("CCCCCCC", query);

        try {
            db.execSQL(query);
            db.close();

            return true;
        } catch(SQLiteException e) {
            db.close();
            return false;
        }
    }

    public Boolean isUserExist(String phoneNumber){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "select * from Users where Users.PhoneNumber = '" + phoneNumber + "'";

        try {
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.getCount() == 0){
                db.close();
                return false;
            }

            db.close();
            return true;
        } catch(SQLiteException e) {
            db.close();
            return false;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
