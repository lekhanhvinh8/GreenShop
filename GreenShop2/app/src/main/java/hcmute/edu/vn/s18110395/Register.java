package hcmute.edu.vn.s18110395;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import hcmute.edu.vn.s18110395.Helper.UserHelper;

public class Register extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    private ImageView selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView textViewRegister = (TextView) findViewById(R.id.tvSignIn);
        textViewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(intent, 1);
            }
        });

        selectImage = (ImageView) findViewById(R.id.select_image);
        selectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });

        Button buttonRegister = (Button) findViewById(R.id.register);
        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserHelper userHelper = new UserHelper(null, getFilesDir().getAbsolutePath());

                EditText edt_phoneNumber = (EditText) findViewById(R.id.phoneNumberRegister);
                EditText edt_password = (EditText) findViewById(R.id.passwordRegister);
                EditText edt_password_confirm = (EditText) findViewById(R.id.passwordConfirmRegister);

                String phoneNumber = edt_phoneNumber.getText().toString();
                String password = edt_password.getText().toString();
                String passwordConfirm = edt_password_confirm.getText().toString();

                if(!password.equals(passwordConfirm)){

                    new AlertDialog.Builder(Register.this)
                            .setTitle("Đăng ký thất bại")
                            .setMessage("Mật khẩu và xác nhận mật khẩu không khớp nhau")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            //.setNegativeButton("No", null)
                            .setIcon(R.drawable.beer)
                            .show();

                    return;
                }

                Boolean isUserExist = userHelper.isUserExist(phoneNumber);

                if(isUserExist){
                    new AlertDialog.Builder(Register.this)
                            .setTitle("Đăng ký thất bại")
                            .setMessage("Số điện thoại đã được sử dụng")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            //.setNegativeButton("No", null)
                            .setIcon(R.drawable.beer)
                            .show();

                    return;
                }

                boolean result = userHelper.register(phoneNumber, password);
                if(result == true)
                {
                    new AlertDialog.Builder(Register.this)
                      // Specifying a listener allows you to take an action before dismissing the dialog.
                            .setTitle("Thành công")
                            .setMessage("Đăng ký thành công")

                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Tới trang đăng nhập", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivityForResult(intent, 1);
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            //.setNegativeButton("No", null)
                            .setIcon(R.drawable.beer)
                            .show();

                    return;
                }

                new AlertDialog.Builder(Register.this)
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        .setTitle("Đăng ký thất bại")
                        .setMessage("Lỗi không xác định")

                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                startActivityForResult(intent, 1);
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        //.setNegativeButton("No", null)
                        .setIcon(R.drawable.beer)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String filename = UUID.randomUUID().toString();
            filename += ".jpg";

            String path = saveToInternalStorage(imageBitmap, filename);

            File f = new File(path, filename);
            try {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

                selectImage.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        // Create imageDir
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}