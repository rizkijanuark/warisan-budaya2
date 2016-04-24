package com.example.srin.warisanbudaya;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

/**
 * Created by SRIN on 4/11/2016.
 */
public class TambahBudayaActivity extends AppCompatActivity {
    Button submit, keluar, unggah;
    EditText nama, kabkot, kec, keldes, deskripsi, pelaku, deskfoto;
    RadioGroup kondisi;
    Spinner kategori, provinsi;
    TextView filePath;
    AlertDialog dialog;
    ImageView imgView;
    private static int RESULT_LOAD_IMG = 200;
    String imgDecodableString;
    final String PREF_NAME = "WBI_pref";
    final String PREF_LOGIN = "isLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambahbudaya);

        nama = (EditText) findViewById(R.id.etNama);
        kabkot = (EditText) findViewById(R.id.etKabKot);
        kec = (EditText) findViewById(R.id.etKec);
        keldes = (EditText) findViewById(R.id.etKelDes);
        deskripsi = (EditText) findViewById(R.id.etDeskripsi);
        pelaku = (EditText) findViewById(R.id.etPelaku);
        deskfoto = (EditText) findViewById(R.id.etPenjelasanGbr);

        provinsi = (Spinner) findViewById(R.id.spProv);
        kategori = (Spinner) findViewById(R.id.spKategori);
        kondisi = (RadioGroup) findViewById(R.id.rgKondisi);

        final EditText[] fields = new EditText[]{nama, kabkot, kec, keldes, deskripsi, pelaku, deskfoto};

        submit = (Button) findViewById(R.id.btTambah);
        keluar = (Button) findViewById(R.id.btKeluar);
        unggah = (Button) findViewById(R.id.btUnggah);

        imgView = (ImageView) findViewById(R.id.imgView);

        filePath = (TextView) findViewById(R.id.tvPath);


        dialog = successDialog();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields(fields) && kondisi.getCheckedRadioButtonId() != -1) {
                    dialog.show();
                } else {
                    Toast.makeText(TambahBudayaActivity.this, "Silakan lengkapi semua kolom isian dan pilihan!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        unggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(PREF_LOGIN, false);
                editor.commit();
                startActivity(new Intent(TambahBudayaActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String filename = selectedImage.getLastPathSegment();
                filePath.setText(filename);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(getScaledBitmap(imgDecodableString, 800, 800));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void resetFields() {
        nama.setText(null);
        kategori.setSelection(0);
        provinsi.setSelection(0);
        kabkot.setText(null);
        kec.setText(null);
        keldes.setText(null);
        deskripsi.setText(null);
        pelaku.setText(null);
        deskfoto.setText(null);
        filePath.setText(null);
        imgView.setImageBitmap(null);
        kondisi.clearCheck();
    }

    private boolean checkFields(EditText[] txtFields) {
        for (int i = 0; i < txtFields.length; i++) {
            EditText currField = txtFields[i];
            if (currField.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    private AlertDialog successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tambah Data Budaya Berhasil!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                resetFields();
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
