package com.example.srin.warisanbudaya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

/**
 * Created by SRIN on 4/11/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    Button reg;
    EditText nmdepan, nmblkg, email, pwd1, pwd2, telp;
    CheckBox snk;
    DBHelper mydb;
    TextView klik;
    Validator val = new Validator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi);

        mydb = new DBHelper(this);

        nmdepan = (EditText) findViewById(R.id.etNamaDepan);
        nmblkg = (EditText) findViewById(R.id.etNamaBelakang);
        email = (EditText) findViewById(R.id.etEmail);
        telp = (EditText) findViewById(R.id.etTelp);
        pwd1 = (EditText) findViewById(R.id.etPwd);
        pwd2 = (EditText) findViewById(R.id.etPwd2);

        snk = (CheckBox) findViewById(R.id.cbSnK);

        klik = (TextView) findViewById(R.id.tvKlik);

        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void insertToDb(DBHelper mydb, String name, String phone, String email, String pass){
        mydb.insertUser(name,phone,email,pass);
    }

    public void checkRegister(View arg0){
        final String firstName = nmdepan.getText().toString();
        if (!val.isValidName(firstName) || TextUtils.isEmpty(firstName)){
            //Set error message for firstName field
            nmdepan.setError("Nama tidak valid");
        }

        final String lastName = nmblkg.getText().toString();
        if (!val.isValidName(lastName) || TextUtils.isEmpty(lastName)){
            //Set error message for lastName field
            nmblkg.setError("Nama tidak valid");
        }

        final String eml = email.getText().toString();
        if (!val.isValidEmail(eml)) {
            //Set error message for email field
            email.setError("Email tidak valid");
        }

        final String pass1 = pwd1.getText().toString();
        if (!val.isValidPassword(pass1)) {
            //Set error message for password field
            pwd1.setError("Password tidak boleh kosong (min 8 karakter)");
        }

        final String pass2 = pwd2.getText().toString();
        if (!val.isValidPassword(pass2)) {
            //Set error message for password field
            pwd2.setError("Password tidak boleh kosong (min 8 karakter)");
        }

        final String phone = telp.getText().toString();
        if (!val.isValidPhone(phone)){
            //Set error message for phone field
            telp.setError("Nomor tidak valid");
        }

        if (!snk.isChecked()){
            //Set error if snk is not checked
            snk.setError("");
        }

        final String name = firstName + " " + lastName;

        if(val.isValidEmail(eml) && val.isValidPassword(pass1) && val.isValidPassword(pass2)
                && val.isValidName(firstName) && val.isValidName(lastName)
                && val.isValidPhone(phone) && snk.isChecked())
        {
            if (!pass1.equals(pass2)){
                //password doesn't match
                Toast.makeText(RegisterActivity.this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
            } else {
                // Validation Completed
                insertToDb(mydb, name, phone, eml, pass1);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}
