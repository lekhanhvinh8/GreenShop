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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import hcmute.edu.vn.s18110395.Domain.Product;
import hcmute.edu.vn.s18110395.Helper.ProductHelper;

public class CartActivity extends AppCompatActivity {

    private ProductHelper productHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

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
            this.productHelper = new ProductHelper(this, getFilesDir().getAbsolutePath());
            ArrayList<Product> products = this.productHelper.getProductOfCart(userName);

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

        //Get product Of cart
        ArrayList<Product> products = this.productHelper.getProductOfCart(userName);

        //display total price
        Integer price = 0;
        for(int i=0; i< products.size(); i++){
            price += products.get(i).getPrice();
        }

        TextView totalPrice = (TextView) findViewById(R.id.total_price);
        totalPrice.setText("T???ng gi??: " + Integer.toString(price) + "??");

        //Display products of cart
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
                    ViewGroup parent = (ViewGroup) v.getParent();
                    ViewGroup grandparent = (ViewGroup) parent.getParent();

                    grandparent.removeView(parent);

                    Integer userId = productHelper.getUserId(value);

                    Boolean result = productHelper.RemoveFromCart(userId, Integer.parseInt(v.getTag().toString()));

                    if(result){
                        ArrayList<Product> products = productHelper.getProductOfCart(value);
                        numCart.setText(Integer.toString(products.size()));

                        //display total price
                        Integer price = 0;
                        for(int i=0; i< products.size(); i++){
                            price += products.get(i).getPrice();
                        }

                        totalPrice.setText("T???ng gi??: " + Integer.toString(price) + "??");
                    }
                }
            }
        };

        for (int i = 0; i < products.size(); i ++)
        {
            //create 2 text view price and name
            TextView textPrice = new TextView(this);
            LinearLayout.LayoutParams textPriceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textPriceParams.gravity = Gravity.CENTER;
            textPrice.setLayoutParams(textPriceParams);
            textPrice.setText("Gi??: " + Integer.toString(products.get(i).getPrice()) + "??");

            TextView textName = new TextView(this);
            textName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textName.setText(products.get(i).getName());

            //create layout for price and name
            LinearLayout priceAndNameLayout = new LinearLayout(this);
            priceAndNameLayout.setOrientation(LinearLayout.VERTICAL);
            priceAndNameLayout.setWeightSum(1);
            LinearLayout.LayoutParams priceAndNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            priceAndNameParams.setMargins(convertDpToPx(5), 0, 0, 0);
            priceAndNameParams.gravity = Gravity.CENTER;
            priceAndNameLayout.setLayoutParams(priceAndNameParams);

            priceAndNameLayout.addView(textName);

            //Create button inside product
            MaterialButton button = new MaterialButton(this);
            Drawable buttonDrawable = button.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(buttonDrawable, Color.rgb(244,67,54));
            button.setBackground(buttonDrawable);
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, convertDpToPx(60));
            buttonLayoutParams.gravity = Gravity.CENTER;
            buttonLayoutParams.setMargins(convertDpToPx(5), 0, 0, 0);
            button.setLayoutParams(buttonLayoutParams);
            button.setText("X??a");
            button.setTextSize(12);
            button.setTag(Integer.toString(products.get(i).getId()));
            button.setOnClickListener(listener);

            //Create image button inside product
            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams imageButtonLayoutParams = new LinearLayout.LayoutParams(convertDpToPx(60), convertDpToPx(60));
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
            LinearLayout.LayoutParams productLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convertDpToPx(77));
            productLayoutParams.setMargins(convertDpToPx(20), convertDpToPx(10), convertDpToPx(30), 0);
            productLayout.setLayoutParams(productLayoutParams);
            productLayout.setWeightSum(1);

            //create a view buffer
            View viewBuffer1 = new View(this);
            LinearLayout.LayoutParams viewParams1 = new LinearLayout.LayoutParams(0, 0);
            viewParams1.weight = 1;
            viewBuffer1.setLayoutParams(viewParams1);

            //Add all child item to product view
            productLayout.addView(imageButton);
            productLayout.addView(priceAndNameLayout);
            productLayout.addView(viewBuffer1);
            productLayout.addView(textPrice);
            productLayout.addView(button);

            //Add 1 product to product area
            productAreaLayout.addView(productLayout);
        }

        //order handling
        View orderView = findViewById(R.id.order);
        orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productHelper.getProductOfCart(userName).size() == 0){
                    new AlertDialog.Builder(CartActivity.this)
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            .setTitle("Kh??ng ???????c ph??p")
                            .setMessage("Gi??? h??ng c???a b???n ch??a c?? s???n ph???m n??o")

                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            //.setNegativeButton("No", null)
                            .setIcon(R.drawable.ic_baseline_report_problem_24)
                            .show();

                    return;
                }

                new AlertDialog.Builder(CartActivity.this)
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        .setTitle("?????t h??ng")
                        .setMessage("B???n mu???n ?????t h??ng t???t c??? m??n h??ng ??? tr??n")

                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                productHelper.order(productHelper.getUserId(userName));

                                //remove all cart view
                                LinearLayout productArea = (LinearLayout) findViewById(R.id.product_area);
                                productArea.removeAllViews();

                                //reset cart product counter
                                ArrayList<Product> products = productHelper.getProductOfCart(userName);
                                numCart.setText(Integer.toString(products.size()));

                                //reset total price
                                Integer price = 0;
                                for(int i=0; i< products.size(); i++){
                                    price += products.get(i).getPrice();
                                }

                                totalPrice.setText("T???ng gi??: " + Integer.toString(price) + "??");
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("H???y", null)
                        .setIcon(R.drawable.ca)
                        .show();
            }
        });

    }

    private int convertDpToPx(float dp) {
        Context context = getApplicationContext();
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
