package com.softwaremeth.group53.android53;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.io.IOException;
import java.io.InputStream;

public class AlbumList extends AppCompatActivity {

    private ListView listView;
    private String[] albumNames;

    ArrayAdapter<String> adapter;

    public static final String ALBUM_NAME_KEY = "album_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        // get listView
        listView = (ListView)findViewById(R.id.albums_list);

        // get albumNames from album.json
        albumNames = getAlbumNames();

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

    /* MANAGE ALBUM NAMES */

    private String[] getAlbumNames() {
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

        try {

            jObject = new JSONObject(jsonRaw);

            jArray = jObject.getJSONArray("albumNames");

            albumNames = new String[jArray.length()];

            for (int i = 0; i < jArray.length(); i++) {
                albumNames[i] = (String) jArray.getJSONObject(i).get("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (albumNames == null) {
            System.out.println("No albums");
            System.exit(0);
        }
        return albumNames;
    }

    private void setAlbumNames(String[] albumNames) {

    }

    private boolean addAlbum() {
        return true;
    }

    private boolean renameAlbum(long id) {

        // modify JSON

        // modify/refresh listView

        adapter.notifyDataSetChanged();

        return true;
    }

    private void deleteAlbum(long id) {

        // modify JSON

        // modify/refresh listView

        adapter.notifyDataSetChanged();
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
