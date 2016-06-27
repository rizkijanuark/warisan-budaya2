package com.example.srin.warisanbudaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.srin.warisanbudaya.app.AppConfig;
import com.example.srin.warisanbudaya.app.AppController;
import com.example.srin.warisanbudaya.app.PrefData;
import com.example.srin.warisanbudaya.helper.DBHelper;
import com.example.srin.warisanbudaya.helper.Validator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SRIN on 4/11/2016.
 */
public class LoginActivity extends AppCompatActivity {
	private SharedPreferences pref;
	private TextView klik;
	private EditText email,pwd;
	private DBHelper mydb;
	private CheckBox rememberMe;
	private Validator val = new Validator();
	private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pref = getSharedPreferences(PrefData.PREF_NAME, MODE_PRIVATE);

        mydb = new DBHelper(this);
		progressDialog = new ProgressDialog(this);

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
            // Validation Completed, Go Login
			Map<String, String> params = new HashMap<>();
			params.put("email", eml);
			params.put("pass", pass);
			login(params);
//            if (mydb.checkUser(eml, pass)){
//
//                //navigate to tambah budaya activity
//
//            } else {
//                //username password tidak cocok atau belum terdaftar
//                Toast.makeText(LoginActivity.this, "Email dan password tidak cocok, atau belum terdaftar", Toast.LENGTH_SHORT).show();
//            }
        }

    }

	private void login(final Map<String, String> params){
		progressDialog.setMessage("Logging in...");
		progressDialog.show();

		String url = AppConfig.URL_API + AppConfig.MEMBER_KEY + "login";
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
			url, new JSONObject(params),
			new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d("JSONObjectRequest", response.toString());
					progressDialog.hide();
					try {
						if (response.getInt("status") == 1){
							JSONObject result = response.getJSONObject("result");
							setDataToPref(result);
							startActivity(new Intent(LoginActivity.this, MemberAreaActivity.class));
							finish();
						} else {
							Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.hide();
				VolleyLog.e("Error: " + error.getMessage());
				Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	private void setDataToPref(JSONObject data){
		//if "remember me" checkbox is checked
		SharedPreferences.Editor editor = pref.edit();
		if (rememberMe.isChecked()){
			editor.putBoolean(PrefData.PREF_REMEMBER, true);
		} else {
			editor.putBoolean(PrefData.PREF_REMEMBER, false);
		}
		//success login
		editor.putBoolean(PrefData.PREF_LOGIN, true);
		//passing data to preferences
		try {
			editor.putString(PrefData.PREF_USER_EMAIL, data.getString("email"));
			editor.putString(PrefData.PREF_USER_NMDEPAN, data.getString("nama_depan"));
			editor.putString(PrefData.PREF_USER_NMBLKG, data.getString("nama_belakang"));
			editor.putString(PrefData.PREF_USER_TELP, data.getString("no_telp"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
}
