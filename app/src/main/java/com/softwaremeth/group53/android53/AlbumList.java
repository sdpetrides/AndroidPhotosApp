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

    private static final int EDIT_ALBUM_CODE = 1;

    public static final String ALBUM_NAME_KEY = "album_name";
    public static final String ALBUM_POS_KEY = "album_pos_key";

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

        // retrieve state
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
                AlbumList.user.albumPos = -1;
                loadAddRenameAlbum();
                return true;
            case R.id.action_search:
                System.out.println("Search clicked");
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
                AlbumList.user.albumPos = (int)info.id;
                loadAddRenameAlbum();
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
            renameAlbum(newAlbumName);
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

    private boolean renameAlbum(String newAlbumName) {

        // get id
        int id = AlbumList.user.albumPos;

        // get album
        Album a = user.albums.get(id);

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

        // populate listView with albumNames
        adapter = new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
        listView.setAdapter(adapter);

        // set context menu for listView items
        registerForContextMenu(listView);
    }
    
    /* CHANGE ACTIVITIES */

    private void loadAlbumView(int pos) {

        // put album name in user
        AlbumList.user.albumPos = pos;

        // create intent
        Intent intent = new Intent(this, AlbumView.class);

        // start AlbumView activity
        startActivity(intent);
    };

    private void loadAddRenameAlbum() {


        // put album name in user
        if (isAddNotRename) {
            AlbumList.user.currentAlbumName = null;
        } else {
            AlbumList.user.currentAlbumName = albumNames[AlbumList.user.albumPos];
        }

        // create intent
        Intent intent = new Intent(this, AddRenameAlbum.class);

        // start AlbumView activity
        startActivityForResult(intent, EDIT_ALBUM_CODE);
    }

    private void loadSearchView() {

        // create bundle
        Bundle bundle = new Bundle();

        // create intent and add bundle
        Intent intent = new Intent(this, SearchPhotos.class);
        intent.putExtras(bundle);

        System.out.println("Bundle Created");

        // start AlbumView activity
        startActivity(intent);
    }
}
