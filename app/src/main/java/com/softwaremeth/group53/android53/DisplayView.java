package com.softwaremeth.group53.android53;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class DisplayView extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Photo photo;

    private ListView locationListView;
    private ListView personListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_view);

        // verify permissions
        verifyStoragePermissions(this);

        // get photo object
        photo = AlbumList.user.currentPhoto;

        // get path from photo object
        String path = photo.getPath();

        // get listView
        locationListView = (ListView) findViewById(R.id.location_tags_list);
        personListView = (ListView) findViewById(R.id.person_tags_list);

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
    public void onStart() {

        super.onStart();
        getDelegate().onStart();

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

        // populate listview
        updateTagsListView();
        return;
    }

    private void loadPhotoDetailsView() {

        // create intent
        Intent intent = new Intent(this, DetailsView.class);

        // start AlbumView activity
        startActivity(intent);

    }

    private void loadSlideshow() {

        // create intent
        Intent intent = new Intent(this, SlideshowView.class);

        // start AlbumView activity
        startActivity(intent);

    }

    private void updateTagsListView() {

        ArrayAdapter<String> locationAdapter;
        ArrayAdapter<String> personAdapter;

        // populate listView with albumNames
        locationAdapter = new ArrayAdapter<String>(this, R.layout.album_cell, photo.locationTags);
        personAdapter = new ArrayAdapter<String>(this, R.layout.album_cell, photo.personTags);
        locationListView.setAdapter(locationAdapter);
        personListView.setAdapter(personAdapter);

        // set context menu for listView items
        registerForContextMenu(locationListView);
        registerForContextMenu(personListView);
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
