package com.softwaremeth.group53.android53;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SearchPhotos extends AppCompatActivity {

    private ListView listView;

    private String[] photoNames;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Search Photos");
        this.setSupportActionBar(toolbar);

        // get gridView and set image adapter
        listView = (ListView)findViewById(R.id.albums_list);

        // get albumNames from album.json
        photoNames = getPhotoNames();

        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.photo_cell, photoNames);
        listView.setAdapter(adapter);

        //
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    /* OPTIONS MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_photos_toolbar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                System.out.println("Search button pressed");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* MANAGE PHOTOS NAMES */

    private String[] getPhotoNames() {

        String jsonRaw = null;

        try {
            InputStream is = getAssets().open("album.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonRaw = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        JSONObject jObject = null;
        JSONArray jArray = null;
        String[] albumNames = null;
        String[] photoNames = null;

        try {

            jObject = new JSONObject(jsonRaw);

            jArray = jObject.getJSONArray("albumNames");

            albumNames = new String[jArray.length()];

            for (int i = 0; i < jArray.length(); i++) {
                if (jArray.getJSONObject(i).get("name").equals("__allPhotos__")) {

                    jArray = (JSONArray) jArray.getJSONObject(i).get("photoIds");

                    photoNames = new String[jArray.length()];

                    for (int j = 0; j < jArray.length(); j++) {
                        photoNames[j] = (String) jArray.get(j);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (photoNames == null) {
            System.out.println("No photos");
            System.exit(0);
        }

        System.out.println(photoNames[1]);



        return photoNames;
    }

    private void setAlbumNames(String[] albumNames) {

    }
}
