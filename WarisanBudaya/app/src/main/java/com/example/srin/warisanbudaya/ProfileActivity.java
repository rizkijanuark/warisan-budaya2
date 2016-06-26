package com.example.srin.warisanbudaya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
	private TextView dpn, blkg, telp, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

		initView();
    }

	private void initView(){
		dpn = (TextView) findViewById(R.id.tvNmDepan);
		blkg = (TextView) findViewById(R.id.tvNmBlkg);
		telp = (TextView) findViewById(R.id.tvTelp);
		email = (TextView) findViewById(R.id.tvEmail);
	}
}
