package hcmute.edu.vn.s18110395;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hcmute.edu.vn.s18110395.Domain.Product;
import hcmute.edu.vn.s18110395.Helper.ProductHelper;

public class ProductActivity extends AppCompatActivity {

    private int cateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

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
                Log.d("Main", "fegweg");

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("searchResult", searcht.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

        //Get cate ID
        this.cateId = Integer.parseInt(getIntent().getStringExtra("cateId"));
        ProductHelper productHelper = new ProductHelper(this, getFilesDir().getAbsolutePath());

        ArrayList<Product> products = productHelper.getProductOfCate(this.cateId);

        LinearLayout productAreaLayout = (LinearLayout) findViewById(R.id.product_area);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("BBBBBBB", v.getTag().toString());
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String value = sharedPreferences.getString("UserName", "");
                if (value.equals("")) {
                    // not having user id
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivityForResult(intent, 1);
                } else {
                    Integer userId = productHelper.getUserId(value);
                    Log.d("UserID", Integer.toString(userId));

                    String productId = v.getTag().toString();

                    if(productHelper.IsCartExist(userId, Integer.parseInt(productId))){
                        new AlertDialog.Builder(ProductActivity.this)
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                .setTitle("Không được phép")
                                .setMessage("Mặt hàng này đã có trong giỏ của bạn")

                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                //.setNegativeButton("No", null)
                                .setIcon(R.drawable.ic_baseline_report_problem_24)
                                .show();
                    }
                    else {
                        Boolean result = productHelper.addToCart(userId, Integer.parseInt(productId));

                        if(result){
                            ArrayList<Product> products = productHelper.getProductOfCart(userName);
                            numCart.setText(Integer.toString(products.size()));
                        }
                    }
                }
            }
        };

        for (int i = 0; i < products.size(); i ++)
        {
            //create 2 text view price and name
            TextView textPrice = new TextView(this);
            LayoutParams textPriceParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textPriceParams.setMargins(0, 0, 0, convertDpToPx(5));
            textPrice.setLayoutParams(textPriceParams);
            textPrice.setText("Giá: " + Integer.toString(products.get(i).getPrice()) + "đ");

            TextView textName = new TextView(this);
            textName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textName.setText(products.get(i).getName());

            //create a view buffer
            View viewBuffer = new View(this);
            LayoutParams viewParams = new LayoutParams(0, 0);
            viewParams.weight = 1;
            viewBuffer.setLayoutParams(viewParams);

            //create layout for price and name
            LinearLayout priceAndNameLayout = new LinearLayout(this);
            priceAndNameLayout.setOrientation(LinearLayout.VERTICAL);
            priceAndNameLayout.setWeightSum(1);
            LayoutParams priceAndNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            priceAndNameParams.setMargins(convertDpToPx(5), 0, 0, 0);
            priceAndNameLayout.setLayoutParams(priceAndNameParams);

            priceAndNameLayout.addView(viewBuffer);
            priceAndNameLayout.addView(textName);
            priceAndNameLayout.addView(textPrice);

            //Create button inside product
            MaterialButton button = new MaterialButton(this);
            Drawable buttonDrawable = button.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(buttonDrawable, Color.rgb(76,175,80));
            button.setBackground(buttonDrawable);
            LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, convertDpToPx(60));
            buttonLayoutParams.gravity = Gravity.CENTER;
            button.setLayoutParams(buttonLayoutParams);
            button.setText("Order");
            button.setTextSize(12);
            button.setTag(Integer.toString(products.get(i).getId()));
            button.setOnClickListener(listener);

            //Create image button inside product
            ImageButton imageButton = new ImageButton(this);
            LayoutParams imageButtonLayoutParams = new LayoutParams(convertDpToPx(60), convertDpToPx(60));
            imageButtonLayoutParams.gravity = Gravity.CENTER;
            imageButton.setLayoutParams(imageButtonLayoutParams);
            imageButton.setBackground(getDrawable(R.drawable.roundedbutton));
            imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageButton.setAdjustViewBounds(true);
            imageButton.setPadding(convertDpToPx(2), convertDpToPx(2), convertDpToPx(2), convertDpToPx(2));
            //imageButton.setImageResource(R.drawable.ic_baseline_storefront_24);
            // load image
            try {
                String filePath = "images/" + products.get(i).getImageName();
                // get input stream
                InputStream ims = getAssets().open(filePath);
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                imageButton.setImageDrawable(d);
            }
            catch(IOException ex) {
                return;
            }

            //Create 1 product view
            LinearLayout productLayout = new LinearLayout(this);
            productLayout.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams productLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, convertDpToPx(77));
            productLayoutParams.setMargins(convertDpToPx(20), convertDpToPx(10), convertDpToPx(30), 0);
            productLayout.setLayoutParams(productLayoutParams);
            productLayout.setWeightSum(1);

            //create a view buffer
            View viewBuffer1 = new View(this);
            LayoutParams viewParams1 = new LayoutParams(0, 0);
            viewParams1.weight = 1;
            viewBuffer1.setLayoutParams(viewParams1);

            //Add all child item to product view
            productLayout.addView(imageButton);
            productLayout.addView(priceAndNameLayout);
            productLayout.addView(viewBuffer1);
            productLayout.addView(button);

            //Add 1 product to product area
            productAreaLayout.addView(productLayout);
        }
    }

    private int convertDpToPx(float dp) {
        Context context = getApplicationContext();
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
