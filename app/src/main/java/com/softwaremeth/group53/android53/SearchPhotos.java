package com.softwaremeth.group53.android53;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SearchPhotos extends AppCompatActivity {

    Boolean tagType;
        // true = location;
        // false = person

    GridView gridView;

    public ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        // sets tag type to Location
        tagType = true;

        // set toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Search Photos");
        this.setSupportActionBar(toolbar);

        // Display all photos
        displayAllPhotos();

        // set actionbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void displayAllPhotos() {

        // display all photos
        myImgAdapter = new ImageAdapter(this, AlbumList.user.allPhotos);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(myImgAdapter);
    }

    /* OPTIONS MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate toolbar menu
        getMenuInflater().inflate(R.menu.search_photos_toolbar_menu, menu);

        // get search manager
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        // set up searchview
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        // on collapse display all photos
                        displayAllPhotos();
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    public void onRadioButtonClicked(View view) {

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.locationRadioButton:
                tagType = true;
                break;
            case R.id.personRadioButton:
                tagType = false;
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query =
                    intent.getStringExtra(SearchManager.QUERY);
            search(query);
        } else {
            displayAllPhotos();
        }
    }

    private void search(String tag) {

        // create album for search results
        Album temp = new Album("tempAlbum");

        // add photos to album according to query
        for (Photo p: AlbumList.user.allPhotos.getPhotos()) {
            if (tagType) {
                for (String s: p.locationTags) {
                    if (s.toLowerCase().startsWith(tag.toLowerCase())) {
                        temp.addPhoto(p);
                        break;
                    }
                }
            } else {
                for (String s: p.personTags) {
                    if (s.toLowerCase().startsWith(tag.toLowerCase())) {
                        temp.addPhoto(p);
                        break;
                    }
                }
            }
        }

        // display all photos
        myImgAdapter = new ImageAdapter(this, temp);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(myImgAdapter);
    }
}
