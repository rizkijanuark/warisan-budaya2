package com.example.srin.warisanbudaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SRIN on 4/11/2016.
 */
public class LoginActivity extends AppCompatActivity {
    final String PREF_NAME = "WBI_pref";
    final String PREF_LOGIN = "isLogin";
    SharedPreferences pref;
    TextView klik;
    EditText email,pwd;
    DBHelper mydb;
    CheckBox rememberMe;
    Validator val = new Validator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        mydb = new DBHelper(this);

        email = (EditText) findViewById(R.id.etEmail);
        pwd = (EditText) findViewById(R.id.etPwd);

        rememberMe = (CheckBox) findViewById(R.id.cbRemember);

        klik = (TextView) findViewById(R.id.tvKlik);

        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    public void checkLogin(View arg0) {

        final String eml = email.getText().toString();
        if (!val.isValidEmail(eml)) {
            //Set error message for email field
            email.setError("Email tidak valid");
        }

        final String pass = pwd.getText().toString();
        if (!val.isValidPassword(pass)) {
            //Set error message for password field
            pwd.setError("Password tidak boleh kosong (min 8 karakter)");
        }

        if(val.isValidEmail(eml) && val.isValidPassword(pass))
        {
            // Validation Completed
            if (mydb.checkUser(eml, pass)){
                //if "remember me" checkbox is checked
                SharedPreferences.Editor editor = pref.edit();
                if (rememberMe.isChecked()){
                    editor.putBoolean(PREF_LOGIN, true);
                } else {
                    editor.putBoolean(PREF_LOGIN, false);
                }
                editor.commit();
                //navigate to tambah budaya activity
                startActivity(new Intent(LoginActivity.this, TambahBudayaActivity.class));
                finish();
            } else {
                //username password tidak cocok atau belum terdaftar
                Toast.makeText(LoginActivity.this, "Email dan password tidak cocok, atau belum terdaftar", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
