package hcmute.edu.vn.s18110395.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hcmute.edu.vn.s18110395.Domain.Invoice;
import hcmute.edu.vn.s18110395.Domain.Product;

public class ProductHelper extends SQLiteOpenHelper {
    private final static String TAG = "UserHelper";
    private final Context myContext;
    private static final String DATABASE_NAME = "greenshop.db";
    private static final int DATABASE_VERSION = 1;
    private String pathToSaveDBFile;

    public ProductHelper(Context context, String filePath){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_NAME).toString();
    }

    public ArrayList<Invoice> getInvoices(Integer userId){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "select * from Orders where UserID = " + Integer.toString(userId) + " Order by Id DESC";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Invoice> invoices = new ArrayList<Invoice>();
        while(cursor.moveToNext()) {
            Invoice invoice = new Invoice();
            invoice.setOrderId(cursor.getInt(0));
            invoice.setUserId(cursor.getInt(1));
            invoice.setDate(cursor.getString(2));

            invoices.add(invoice);
        }

        for(int i = 0; i< invoices.size(); i ++){
            query = "select p.Id, p.Name, p.Price, p.ImageFileName\n" +
                    "from Products p inner join OrderProducts o on p.Id = o.ProductId \n" +
                    "where o.OrderId = " + Integer.toString(invoices.get(i).getOrderId());

            cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()) {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getInt(2));
                product.setImageName(cursor.getString(3));

                invoices.get(i).products.add(product);
            }
        }

        db.close();

        return invoices;
    }

    public ArrayList<Product> getProductSearch(String key){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "select Id, Name, Price, ImageFileName from Products where Name = '" + key + "'";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Product> products = new ArrayList<Product>();
        while(cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setPrice(cursor.getInt(2));
            product.setImageName(cursor.getString(3));

            products.add(product);
        }
        db.close();

        return products;
    }

    public ArrayList<Product> getProductOfCart(String userName){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "select p.Id, p.Name, p.Price, p.ImageFileName\n" +
                "from Carts c inner join Products p on c.ProductId = p.Id\n" +
                "\t\t\t inner join Users u on u.Id = c.UserId \n" +
                "where u.PhoneNumber = '"+ userName  +"'";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Product> products = new ArrayList<Product>();
        while(cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setPrice(cursor.getInt(2));
            product.setImageName(cursor.getString(3));

            products.add(product);
        }
        db.close();

        return products;
    }

    public ArrayList<Product> getProductOfCate(int cateId){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM Products Where CategoryId='" + Integer.toString(cateId) + "'";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Product> products = new ArrayList<Product>();
        while(cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setPrice(cursor.getInt(2));
            product.setQuantity(cursor.getInt(3));
            product.setImageName(cursor.getString(4));
            product.setCateId(cursor.getInt(5));

            products.add(product);
        }
        db.close();

        return products;
    }

    public int getUserId(String phoneNumber){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "select * from Users where Users.PhoneNumber = '" + phoneNumber + "'";

        try {
            Cursor cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()) {

                return cursor.getInt(0);
            }

            db.close();
        } catch(SQLiteException e) {
            db.close();
            return -1;
        }

        return -1;
    }

    public boolean RemoveFromCart(int userId, int productId){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        String query = "delete from Carts where UserID = " + Integer.toString(userId) + " and ProductId = " + Integer.toString(productId);

        try {
            db.execSQL(query);

            db.close();
            return true;
        } catch(SQLiteException e) {
            db.close();
            return false;
        }
    }

    public boolean order(int userId){

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);

        //Add order
        String query = "Insert into Orders (UserID, OrderTime) values (" + Integer.toString(userId) + ", '" + date + "')";

        db.execSQL(query);

        //Get order just adding.
        query = "select * from Orders where UserID = " + Integer.toString(userId) + " and OrderTime = '" + date + "'";

        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            Integer orderId = cursor.getInt(0);

            //Add product to order
            String query1 = "select * from Carts where UserID = " + Integer.toString(userId);
            Cursor cursor1 = db.rawQuery(query1, null);

            while(cursor1.moveToNext()) {
                Integer productId = cursor1.getInt(1);

                String query2 = "INSERT INTO OrderProducts VALUES (" + Integer.toString(orderId) + "," + Integer.toString(productId) + ");";
                db.execSQL(query2);
            }

            //Delete product from carts
            query1 = "Delete from Carts where UserID = " + Integer.toString(userId);
            db.execSQL(query1);

            break;
        }

        db.close();

        return true;
    }

    public boolean addToCart(int userId, int productId){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        String query = "INSERT INTO Carts values(" + Integer.toString(userId) + ", " + Integer.toString(productId) + " )";

        try {
            db.execSQL(query);

            db.close();
            return true;
        } catch(SQLiteException e) {
            db.close();
            return false;
        }
    }

    public boolean IsCartExist(int userId, int productId){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "select * from Carts where UserID = " + Integer.toString(userId) + " and ProductId = " + Integer.toString(productId);

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
