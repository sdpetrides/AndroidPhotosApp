package com.softwaremeth.group53.android53;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayView extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Photo photo;

    ArrayAdapter<String> adapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.diplay_view);

        // verify permissions
        verifyStoragePermissions(this);

        // get photo object
        photo = AlbumList.user.currentPhoto;

        // get path from photo object
        String path = photo.getPath();

        // get listView
        listView = (ListView) findViewById(R.id.tags_list);

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

        // populate listView
        updateTagsListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_view_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_details:
                loadPhotoDetailsView();
                return true;
            case R.id.action_slideshow:
                loadSlideshow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {



        // save state
        AlbumList.user.saveState(this);

        return;
    }

    private void loadPhotoDetailsView() {

        // create intent
        Intent intent = new Intent(this, DetailsView.class);

        // start AlbumView activity
        startActivity(intent);

    }

    private void loadSlideshow() {

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

    private void updateTagsListView() {

        ArrayList<String> combine = new ArrayList<String>();

        combine.addAll(photo.locationTags);
        combine.addAll(photo.personTags);


        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.album_cell, combine);
        listView.setAdapter(adapter);

        // set context menu for listView items
        registerForContextMenu(listView);
    }
}
