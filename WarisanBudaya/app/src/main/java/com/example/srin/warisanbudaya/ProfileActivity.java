package com.example.srin.warisanbudaya;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.srin.warisanbudaya.app.AppConfig;
import com.example.srin.warisanbudaya.app.AppController;
import com.example.srin.warisanbudaya.app.PrefData;

public class ProfileActivity extends AppCompatActivity {
	private TextView dpn, blkg, telp, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

		initView();

		getDataFromPref();
    }

	private void initView(){
		dpn = (TextView) findViewById(R.id.tvNmDepan);
		blkg = (TextView) findViewById(R.id.tvNmBlkg);
		telp = (TextView) findViewById(R.id.tvTelp);
		email = (TextView) findViewById(R.id.tvEmail);
	}

	private void getDataFromPref(){
		SharedPreferences pref = getSharedPreferences(PrefData.PREF_NAME, MODE_PRIVATE);
		dpn.setText(pref.getString(PrefData.PREF_USER_NMDEPAN, ""));
		blkg.setText(pref.getString(PrefData.PREF_USER_NMBLKG, ""));
		telp.setText(pref.getString(PrefData.PREF_USER_TELP, ""));
		email.setText(pref.getString(PrefData.PREF_USER_EMAIL, ""));
	}
}
