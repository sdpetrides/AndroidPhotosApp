package com.softwaremeth.group53.android53;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AlbumView extends AppCompatActivity {

    private ArrayList<String> albumPhotoNames;

    ImageView targetImage;

    public ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        // get the name and detail from bundle
        Bundle bundle = getIntent().getExtras();
        String albumName = bundle.getString(AlbumList.ALBUM_NAME_KEY);

        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(albumName);
        this.setSupportActionBar(toolbar);

        //
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // get gridView and set image adapter

        myImgAdapter = new ImageAdapter(this, albumName);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(myImgAdapter);




        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        targetImage = (ImageView)findViewById(R.id.targetimage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                }
            }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            Uri targetUri = data.getData();

            System.out.println(Environment.getExternalStorageDirectory());

            System.out.println(targetUri.toString());
            System.out.println(targetUri.getPath());

            File file = new File(targetUri.toString());
            File file2 = new File(targetUri.getPath());
            System.out.println(file.getAbsolutePath());
            System.out.println(file2.getAbsolutePath());

            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                File finalFile = new File(getRealPathFromURI(tempUri));


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri tempUri) {
        Cursor cursor = getContentResolver().query(tempUri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Uri getImageUri(Context applicationContext, Bitmap targetImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        targetImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                applicationContext.getContentResolver(), targetImage, "Title", null);
        return Uri.parse(path);
    }


}