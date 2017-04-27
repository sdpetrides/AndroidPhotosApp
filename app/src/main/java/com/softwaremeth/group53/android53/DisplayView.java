package com.softwaremeth.group53.android53;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.ImageView;

public class DisplayView extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final String PHOTO_POS_KEY = "photo_pos_key";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diplay_view);

        // verify permissions
        verifyStoragePermissions(this);

        // get photo object
        Photo photo = AlbumList.user.currentPhoto;

        // get path from photo object
        String path = photo.getPath();

        // set toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);

        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get imageview
        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        // create bitmap
        Bitmap bm = BitmapFactory.decodeFile(path);

        // set image bitmap
        imageView.setImageBitmap(bm);
    }

    public void verifyStoragePermissions(Activity activity) {

        // get permissions
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // check if we have write permission
        if (permission != PackageManager.PERMISSION_GRANTED) {

            // if not, ask user for permission
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
