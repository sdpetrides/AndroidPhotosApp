package com.softwaremeth.group53.android53;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SlideshowView extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String DEBUG_TAG = "test";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Album currentAlbum;
    private int photoPos;

    private ImageView imageView;

    private ImageView leftImageView;
    private ImageView rightImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow_view);

        // verify permissions
        verifyStoragePermissions(this);

        // set toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);

        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get current album and photo position
        currentAlbum = AlbumList.user.currentAlbum;
        photoPos = AlbumList.user.photoPos;

        // get imageview
        imageView = (ImageView) findViewById(R.id.image_view);

        // set arrow buttons
        setArrowButtons();

        // display photo
        displayPhoto();
    }

    private void setArrowButtons() {

        // get imageviews
        leftImageView = (ImageView) findViewById(R.id.left_button);
        rightImageView = (ImageView) findViewById(R.id.right_button);

        // set image resources
        leftImageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        rightImageView.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
    }


    public void displayPhoto() {

        // create bitmap
        Bitmap bm = BitmapFactory.decodeFile(currentAlbum.getPhotoAt(photoPos).getPath());

        // set image bitmap
        imageView.setImageBitmap(bm);
    }

    public void moveLeft(View view) {

        // update photo position
        if (photoPos > 0) {
            photoPos--;
        } else {
            photoPos = currentAlbum.getPhotos().size() - 1;
        }

        // update current photo
        AlbumList.user.currentPhoto = currentAlbum.getPhotos().get(photoPos);

        // display photo
        displayPhoto();

    }

    public void moveRight(View view) {

        int lastIndex = currentAlbum.getPhotos().size() - 1;

        // update photo position
        if (photoPos < lastIndex) {
            photoPos++;
        } else {
            photoPos = 0;
        }

        // update current photo
        AlbumList.user.currentPhoto = currentAlbum.getPhotos().get(photoPos);

        // display photo
        displayPhoto();
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
