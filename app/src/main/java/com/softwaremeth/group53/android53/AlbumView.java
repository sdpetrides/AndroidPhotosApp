package com.softwaremeth.group53.android53;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int DISPLAY_PHOTO_CODE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String imagePath;

    private String albumName;

    private int albumPos;

    GridView gridView;

    public ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        // verify permissions
        verifyStoragePermissions(this);

        // set album name and album position
        albumPos = AlbumList.user.albumPos;
        albumName = AlbumList.user.albums.get(albumPos).getAlbumName();

        // set toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(albumName);
        this.setSupportActionBar(toolbar);

        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get gridView and set image adapter
        myImgAdapter = new ImageAdapter(this, albumName, albumPos);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(myImgAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                loadDisplayPhoto(pos);
            }
        });

        // set context menu for listView items
        registerForContextMenu(gridView);
    }

    /* OPTIONS MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_view_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                return true;
            default:
                return false;
        }
    }

    /* CONTEXT MENU */

    @Override
    public void onCreateContextMenu(
            ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove:

                // remove picture
                myImgAdapter.removePicture((int)info.id);

                // save state
                AlbumList.user.saveState(this);

                // update gridview
                myImgAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                gridView.setAdapter(myImgAdapter);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {

            // get path from result
            Uri tempUri = intent.getData();
            getRealPathFromURI(tempUri);

            // add picture to album
            myImgAdapter.addPicture(imagePath);

            // save state
            AlbumList.user.saveState(this);

            // update gridview
            myImgAdapter.notifyDataSetChanged();
            gridView.invalidateViews();
            gridView.setAdapter(myImgAdapter);
        }
    }

    private String getRealPathFromURI(Uri tempUri) {

        String[] filePathArray = {
                MediaStore.Images.Media.DATA
        };

        Cursor cursor = getContentResolver().query(
                tempUri,
                filePathArray,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathArray[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;
    }

    private void loadDisplayPhoto(int photoPos) {

        // set photo position and current photo
        AlbumList.user.photoPos = photoPos;
        AlbumList.user.currentPhoto = AlbumList.user.albums.get(albumPos).getPhotoAt(photoPos);

        // create intent and add bundle
        Intent intent = new Intent(this, DisplayView.class);

        // start AlbumView activity
        startActivity(intent);
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