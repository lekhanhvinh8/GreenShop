package hcmute.edu.vn.s18110395;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import hcmute.edu.vn.s18110395.Domain.Product;
import hcmute.edu.vn.s18110395.Helper.DatabaseHelper;
import hcmute.edu.vn.s18110395.Helper.ProductHelper;
import hcmute.edu.vn.s18110395.Helper.UserHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    DatabaseHelper dbHelper= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        dbHelper = new DatabaseHelper(this, getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        //go back home page
        View homePage = findViewById(R.id.home_page);
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //Go to login
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userName = sharedPreferences.getString("UserName", "");

        TextView numCart = (TextView) findViewById(R.id.num_cart);
        if (userName.equals("")) {
            // not having user name
        } else {
            //Get product Of cart
            ProductHelper productHelper = new ProductHelper(this, getFilesDir().getAbsolutePath());
            ArrayList<Product> products = productHelper.getProductOfCart(userName);

            numCart.setText(Integer.toString(products.size()));
        }

        ImageView imageViewLogin = (ImageView) findViewById(R.id.login);
        if (userName.equals("")) {
            // not having user name
            imageViewLogin.setImageResource(R.drawable.ic_baseline_login_24);
        } else {
            imageViewLogin.setImageResource(R.drawable.logout);
        }

        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userName = sharedPreferences.getString("UserName", "");

                if (userName.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivityForResult(intent, 1);
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserName", String.valueOf(""));
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        //go to cart
        View cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userName = sharedPreferences.getString("UserName", "");

                if (userName.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        //go to invoice
        View invoice = findViewById(R.id.invoice);
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userName = sharedPreferences.getString("UserName", "");

                if (userName.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(getApplicationContext(), InvoiceActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        //Go to search
        View search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searcht = (TextView) findViewById(R.id.key_search);

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("searchResult", searcht.getText().toString());
                startActivityForResult(intent, 1);
            }
        });


        View cate_rau = findViewById(R.id.cate_rau);
        View cate_ca = findViewById(R.id.cate_ca);
        View cate_thit = findViewById(R.id.cate_thit);
        View cate_dohop = findViewById(R.id.cate_dohop);
        View cate_donglanh = findViewById(R.id.cate_donglanh);
        View cate_bia = findViewById(R.id.cate_bia);
        View cate_nuocngot = findViewById(R.id.cate_nuocngot);


        cate_rau.setOnClickListener(this); // calling onClick() method
        cate_ca.setOnClickListener(this); // calling onClick() method
        cate_thit.setOnClickListener(this); // calling onClick() method
        cate_dohop.setOnClickListener(this); // calling onClick() method
        cate_donglanh.setOnClickListener(this); // calling onClick() method
        cate_bia.setOnClickListener(this); // calling onClick() method
        cate_nuocngot.setOnClickListener(this); // calling onClick() method

        // test save an image to internal storage
        //Bitmap bitmap = dbHelper.getBitmapFromAssetImages("caingot.jpg");
        //String path = dbHelper.saveImageToInternalStorage(bitmap, "caingot.jpg");
    }

    @Override
    public void onClick(View v) {
        // default method for handling onClick Events..
        switch (v.getId()) {
            case R.id.cate_rau:
                Log.d("BBBBBBB", v.getTag().toString());
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("cateId", Integer.toString(1));
                startActivityForResult(intent, 1);
                break;
            case R.id.cate_thit:
                Intent intent2 = new Intent(getApplicationContext(), ProductActivity.class);
                intent2.putExtra("cateId", Integer.toString(2));
                startActivityForResult(intent2, 1);
                break;

            case R.id.cate_ca:
                Intent intent3 = new Intent(getApplicationContext(), ProductActivity.class);
                intent3.putExtra("cateId", Integer.toString(3));
                startActivityForResult(intent3, 1);
                break;

            case R.id.cate_bia:
                Intent intent4 = new Intent(getApplicationContext(), ProductActivity.class);
                intent4.putExtra("cateId", Integer.toString(4));
                startActivityForResult(intent4, 1);
                break;

            case R.id.cate_dohop:
                Intent intent5 = new Intent(getApplicationContext(), ProductActivity.class);
                intent5.putExtra("cateId", Integer.toString(5));
                startActivityForResult(intent5, 1);
                break;

            case R.id.cate_donglanh:
                Intent intent6 = new Intent(getApplicationContext(), ProductActivity.class);
                intent6.putExtra("cateId", Integer.toString(6));
                startActivityForResult(intent6, 1);
                break;

            case R.id.cate_nuocngot:
                Intent intent7 = new Intent(getApplicationContext(), ProductActivity.class);
                intent7.putExtra("cateId", Integer.toString(7));
                startActivityForResult(intent7, 1);
                break;

            default:
                break;
        }
    }
}