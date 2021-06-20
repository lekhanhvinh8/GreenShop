package hcmute.edu.vn.s18110395;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.s18110395.Helper.UserHelper;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView textViewForgotPass = (TextView) findViewById(R.id.tvForgotPassword);
        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PasswordRetrieval.class);
                startActivityForResult(intent, 1);
            }
        });

        TextView textViewRegister = (TextView) findViewById(R.id.tvRegisterNow);
        textViewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(intent, 1);
            }
        });

        Button buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UserHelper userHelper = new UserHelper(null, getFilesDir().getAbsolutePath());

                EditText edt_userName = (EditText) findViewById(R.id.edt_userName);
                EditText edt_password = (EditText) findViewById(R.id.edt_password);

                String userName = edt_userName.getText().toString();
                String password = edt_password.getText().toString();

                Boolean isTrue = userHelper.login(userName, password);

                if(!isTrue){

                    new AlertDialog.Builder(Login.this)
                            .setTitle("Đăng nhập thất bại")
                            .setMessage("Tên đăng nhập hoặc mật khẩu không đúng")

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

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserName", String.valueOf(userName));
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}
