package com.softwaremeth.group53.android53;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AlbumList extends AppCompatActivity {

    private ListView listView;
    private String[] albumNames;
    private String[] arrPath;

    ArrayAdapter<String> adapter;

    myJSON myJson;

    public static final String ALBUM_NAME_KEY = "album_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);

        myJson = new myJSON(this, "album_4.json");

        // getImageFilePaths();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        // get listView
        listView = (ListView) findViewById(R.id.albums_list);

        // get albumNames from album.json
        albumNames = myJson.getAlbumNames();

        if (albumNames == null) {

            // set listView with empty element
            listView.setEmptyView(findViewById(R.id.emptyElement));

        } else {

            // populate listView with albumNames
            adapter = new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
            listView.setAdapter(adapter);

            // set context menu for listView items
            registerForContextMenu(listView);

            // set listener to load album view
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    loadAlbumView(position);
                }
            });
        }
    }

    /* GET IMAGE FILEPATHS */

    private void getImageFilePaths() {

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        // Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        // Total number of images
        int count = cursor.getCount();

        // Create an array to store path to all the images
        arrPath = new String[count];

        System.out.println("Before loading paths: " + count);

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            // Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);

            System.out.println(arrPath[i]);

            Log.i("PATH", arrPath[i]);
        }
    }

    /* OPTIONS MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_list_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                System.out.println("Add album button pressed");
                if (!addAlbum()) {
                    // handle user error
                }
                return true;
            case R.id.action_search:
                System.out.println("Search button pressed");
                loadSearchView();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* CONTEXT MENU */

    @Override
    public void onCreateContextMenu(
            ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.rename:
                if (!renameAlbum(info.id)) {
                    // handle user error
                }
                return true;
            case R.id.delete:
                deleteAlbum(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /* MANAGE ALBUMS */

    private boolean addAlbum() {

        JSONObject jObject = myJson.getJSONObject();
        JSONArray jArray = null;

        if (jObject == null) {
            // System.out.println("jObject is null");
            return false;
        }

        String strTemp = "{\"name\":\"NewAlbum\", \"photoIds\":\"[]\"}";

        // System.out.println("strTemp: " + strTemp);

        try {
            JSONObject objTemp = new JSONObject(strTemp);
            jArray = jObject.getJSONArray("albumNames");
            jArray.put(objTemp);
        } catch (JSONException e) {
            return false;
        }

        myJson.setJSONObject(jObject);

        // get albumNames from album.json
        albumNames = myJson.getAlbumNames();

        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
        listView.setAdapter(adapter);

        // set context menu for listView items
        registerForContextMenu(listView);

        // set listener to load album view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadAlbumView(position);
            }
        });

        return true;
    }

    private boolean renameAlbum(long id) {

        JSONObject jObject = myJson.getJSONObject();
        JSONArray jArray = null;

        if (jObject == null) {
            return false;
        }

        try {
            jArray = jObject.getJSONArray("albumNames");
            JSONObject jObjectTemp = jArray.getJSONObject((int) id);
            jObjectTemp.putOpt("name", "New Name");
            jObject.remove("albumNames");
            jObject.put("albumNames", jArray);
        } catch (JSONException e) {}

        myJson.setJSONObject(jObject);

        // get albumNames from album.json
        albumNames = myJson.getAlbumNames();

        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
        listView.setAdapter(adapter);

        // set context menu for listView items
        registerForContextMenu(listView);

        // set listener to load album view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadAlbumView(position);
            }
        });

        return true;
    }

    private void deleteAlbum(long id) {

        JSONObject jObject = myJson.getJSONObject();
        JSONArray jArray = null;

        if (jObject == null) {
            return;
        }

        try {
            jArray = jObject.getJSONArray("albumNames");
            jArray.remove((int) id);
            jObject.remove("albumNames");
            jObject.put("albumNames", jArray);
        } catch (JSONException e) {}

        myJson.setJSONObject(jObject);

        // get albumNames from album.json
        albumNames = myJson.getAlbumNames();

        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
        listView.setAdapter(adapter);

        // set context menu for listView items
        registerForContextMenu(listView);

        // set listener to load album view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadAlbumView(position);
            }
        });
    }


    /* CHANGE ACTIVITIES */

    private void loadAlbumView(int pos) {

        // create bundle
        Bundle bundle = new Bundle();

        // put album name in the bundle
        bundle.putString(ALBUM_NAME_KEY, albumNames[pos]);

        // create intent and add bundle
        Intent intent = new Intent(this, AlbumView.class);
        intent.putExtras(bundle);

        // start AlbumView activity
        startActivity(intent);
    };

    private void loadSearchView() {

        // create bundle
        Bundle bundle = new Bundle();

        // create intent and add bundle
        Intent intent = new Intent(this, SearchPhotos.class);
        intent.putExtras(bundle);

        // start AlbumView activity
        startActivity(intent);

    }

}
