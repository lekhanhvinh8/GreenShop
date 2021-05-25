package hcmute.edu.vn.s18110395;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                    Toast toast = Toast.makeText(getApplicationContext(), "Password confirmation doesn't match password", 3);
                    toast.show();
                    return;
                }

                Boolean isUserExist = userHelper.isUserExist(phoneNumber);

                if(isUserExist){
                    Toast toast = Toast.makeText(getApplicationContext(), "Phone number already in use", 3);
                    toast.show();
                    return;
                }

                boolean result = userHelper.register(phoneNumber, password);
                if(result == true)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sign up success", 3);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivityForResult(intent, 1);

                    return;
                }

                Toast toast = Toast.makeText(getApplicationContext(), "registration failed", 3);
                toast.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectImage.setImageBitmap(imageBitmap);
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


}