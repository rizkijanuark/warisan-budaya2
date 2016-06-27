package com.example.srin.warisanbudaya;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.srin.warisanbudaya.app.AppConfig;
import com.example.srin.warisanbudaya.app.AppController;
import com.example.srin.warisanbudaya.helper.DBHelper;
import com.example.srin.warisanbudaya.helper.Validator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SRIN on 4/11/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText nmdepan, nmblkg, email, pwd1, pwd2, telp;
    private CheckBox snk;
    private DBHelper mydb;
    private TextView klik, snkLink;
    private Validator val = new Validator();
    private ProgressDialog downloadProgressDialog, progressDialog;
    private final String DOWNLOAD_URL = "http://www.nus.edu.sg/comcen/gethelp/guide/itcare/wireless/NUS-WPA2%20Network%20Configuration%20Guide%20for%20Android%204.0.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi);

        initView();

        mydb = new DBHelper(this);
        downloadProgressDialog = new ProgressDialog(this);
        progressDialog = new ProgressDialog(this);

        downloadProgressDialog.setMessage("Mengunduh Syarat & Ketentuan");
        downloadProgressDialog.setIndeterminate(true);
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgressDialog.setCancelable(true);

        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				finish();
            }
        });

        snkLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this, SnKActivity.class));
//				final DownloadSNK downloadTask = new DownloadSNK(RegisterActivity.this);
//				downloadTask.execute(DOWNLOAD_URL);
//
//				downloadProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//					@Override
//					public void onCancel(DialogInterface dialog) {
//						downloadTask.cancel(true);
//					}
//				});
            }
        });
    }

    private void insertToDb(DBHelper mydb, String name, String phone, String email, String pass){
        mydb.insertUser(name,phone,email,pass);
    }

    private void initView(){
        nmdepan = (EditText) findViewById(R.id.etNamaDepan);
        nmblkg = (EditText) findViewById(R.id.etNamaBelakang);
        email = (EditText) findViewById(R.id.etEmail);
        telp = (EditText) findViewById(R.id.etTelp);
        pwd1 = (EditText) findViewById(R.id.etPwd);
        pwd2 = (EditText) findViewById(R.id.etPwd2);

        snk = (CheckBox) findViewById(R.id.cbSnK);

        klik = (TextView) findViewById(R.id.tvKlik);
        snkLink = (TextView) findViewById(R.id.tvSnK);
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
//                insertToDb(mydb, name, phone, eml, pass1);
				Map<String, String> params = new HashMap<>();
				params.put("email", eml);
				params.put("password", pass1);
				params.put("nama_depan", firstName);
				params.put("nama_belakang", lastName);
				params.put("no_telp", phone);
				register(params);
            }
        }
    }

	private void register(final Map<String, String> params){
		progressDialog.setMessage("Registering your account...");
		progressDialog.show();

		String url = AppConfig.URL_API + AppConfig.MEMBER_KEY + "register";
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
			url, new JSONObject(params),
			new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d("JSONObjectRequest", response.toString());
					progressDialog.hide();
					try {
						if (response.getInt("status") == 1){
							Toast.makeText(RegisterActivity.this, "Your account has been successfully registered!", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
							finish();
						} else {
							Toast.makeText(RegisterActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
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
				Toast.makeText(RegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

    private class DownloadSNK extends AsyncTask<String, Integer, String>{

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private String FILE_NAME = "syarat dan ketentuan WBI.pdf";
        private String FOLDER_NAME = "/sdcard/wbi/";
        private File outputFile;

        public DownloadSNK(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                File wbiDir = new File(FOLDER_NAME);
                wbiDir.mkdirs();
                outputFile = new File(wbiDir, FILE_NAME);
                output = new FileOutputStream(outputFile);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            downloadProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            downloadProgressDialog.setIndeterminate(false);
            downloadProgressDialog.setMax(100);
            downloadProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mWakeLock.release();
            downloadProgressDialog.dismiss();

            if (s!=null){
                Toast.makeText(context, "Download failed: " + s, Toast.LENGTH_LONG).show();
            } else {
                openFile(outputFile);
            }
        }
    }

    private void openFile(final File file){
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(file);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(RegisterActivity.this, "PDF Reader application is not installed in your device", Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }
}
