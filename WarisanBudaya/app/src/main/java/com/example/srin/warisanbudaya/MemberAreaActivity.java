package com.example.srin.warisanbudaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Afkar on 6/26/2016.
 */
public class MemberAreaActivity extends AppCompatActivity implements View.OnClickListener{
    private Button viewCult, addCult, viewProf, exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberarea);

        initView();

        viewCult.setOnClickListener(this);
        addCult.setOnClickListener(this);
        viewProf.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void initView(){
        viewCult = (Button) findViewById(R.id.btLihatBudaya);
        addCult = (Button) findViewById(R.id.btTambahBudaya);
        viewProf = (Button) findViewById(R.id.btProfil);
        exit = (Button) findViewById(R.id.btExit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLihatBudaya:
				startActivity(new Intent(MemberAreaActivity.this, LihatBudayaActivity.class));
                break;
            case R.id.btTambahBudaya:
				startActivity(new Intent(MemberAreaActivity.this, TambahBudayaActivity.class));
                break;
            case R.id.btProfil:
				startActivity(new Intent(MemberAreaActivity.this, ProfileActivity.class));
                break;
            case R.id.btExit:
                SharedPreferences.Editor editor = getSharedPreferences(PrefData.PREF_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(PrefData.PREF_LOGIN, false);
                editor.commit();
                startActivity(new Intent(MemberAreaActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
