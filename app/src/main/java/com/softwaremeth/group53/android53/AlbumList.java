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

    private static final int EDIT_MOVIE_CODE = 1;
    public static final String ALBUM_NAME_KEY = "album_name";
    public static final String ALBUM_POS_KEY = "album_pos_key";
    public static final String USER_KEY = "user_key";

    public static boolean isAddNotRename;
    private int renameId;

    private ListView listView;
    ArrayAdapter<String> adapter;

    private String[] albumNames;

    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);


        user = User.retrieveState(this);

        // get User object
        if (user == null) {
            user = new User();
            user.saveState(this);
        }

        // get albumNames from user
        albumNames = user.getAlbumsArray();

        // initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Albums");
        this.setSupportActionBar(toolbar);

        // get listView
        listView = (ListView) findViewById(R.id.albums_list);

        if (albumNames == null) {

            // set listView with empty element
            listView.setEmptyView(findViewById(R.id.emptyElement));

        } else {

            // update listview
            updateAlbumListView();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    loadAlbumView(position);
                }
            });
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
                isAddNotRename = true;
                loadAddRenameAlbum(-1);
                return true;
            case R.id.action_search:
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
                isAddNotRename = false;
                renameId = (int)info.id;
                loadAddRenameAlbum((int)info.id);
                return true;
            case R.id.delete:
                deleteAlbum(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /* MANAGE ADD/RENAME ACTIVITY */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode != RESULT_OK) { return; }

        Bundle bundle = intent.getExtras();
        if (bundle == null) { return; }

        String newAlbumName = bundle.getString(AddRenameAlbum.ALBUM_NAME);

        if (isAddNotRename) {
            addAlbum(newAlbumName);
        } else {
            renameAlbum(newAlbumName, renameId);
        }

    }

    /* MANAGE ALBUMS */

    private boolean addAlbum(String newAlbumName) {

        // create album
        Album a = new Album(newAlbumName);

        // add to albums
        user.albums.add(a);

        // save state
        user.saveState(this);

        // update listview
        updateAlbumListView();

        return true;
    }

    private boolean renameAlbum(String newAlbumName, long id) {

        // get album
        Album a = user.albums.get((int)id);

        // update name
        a.setAlbumName(newAlbumName);

        // save state
        user.saveState(this);

        // update listview
        updateAlbumListView();

        return true;
    }

    private void deleteAlbum(long id) {

        // remove album
        user.albums.remove((int)id);

        // save state
        user.saveState(this);

        // update listview
        updateAlbumListView();
    }

    private void updateAlbumListView() {

        // get updated albumlist
        albumNames = user.getAlbumsArray();

        System.out.println("after:  " + user.albums);

        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
        listView.setAdapter(adapter);

        // set context menu for listView items
        registerForContextMenu(listView);
    }
    
    /* CHANGE ACTIVITIES */

    private void loadAlbumView(int pos) {

        // create bundle
        Bundle bundle = new Bundle();

        // put album name in the bundle
        bundle.putString(ALBUM_NAME_KEY, albumNames[pos]);
        bundle.putInt(ALBUM_POS_KEY, pos);

        // create intent and add bundle
        Intent intent = new Intent(this, AlbumView.class);
        intent.putExtras(bundle);

        // start AlbumView activity
        startActivity(intent);
    };

    private void loadAddRenameAlbum(int pos) {

        // create bundle
        Bundle bundle = new Bundle();

        // put album name in the bundle
        if (isAddNotRename) {
            bundle.putString(AddRenameAlbum.ALBUM_NAME, null);
        } else {
            bundle.putString(AddRenameAlbum.ALBUM_NAME, albumNames[pos]);
        }

        // create intent and add bundle
        Intent intent = new Intent(this, AddRenameAlbum.class);
        intent.putExtras(bundle);

        // start AlbumView activity
        startActivityForResult(intent, EDIT_MOVIE_CODE);
    }

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
