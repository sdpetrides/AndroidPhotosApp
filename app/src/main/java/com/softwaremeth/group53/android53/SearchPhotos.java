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

    //true = location; false = person
    Boolean tagType;

    private ListView listView;

    private String[] photoNames;

    ArrayAdapter<String> adapter;

    GridView gridView;

    public ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        // sets tag type to Location
        tagType = true;

        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Search Photos");
        this.setSupportActionBar(toolbar);

        // get gridView and set image adapter
        myImgAdapter = new ImageAdapter(this, AlbumList.user.allPhotos);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(myImgAdapter);

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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.locationRadioButton:
                if (checked)
                    tagType = true;
                break;
            case R.id.personRadioButton:
                if (checked)
                    tagType = false;
                break;
        }
    }

    private void search(String tag) {

        Album temp = new Album("temp");

        for (Photo p: AlbumList.user.allPhotos.getPhotos()) {
            if (tagType) {
                for (String s: p.locationTags) {
                    if (s.startsWith(tag)) {
                        temp.addPhoto(p);
                        break;
                    }
                }
            } else {
                for (String s: p.personTags) {
                    if (s.startsWith(tag)) {
                        temp.addPhoto(p);
                        break;
                    }
                }
            }
        }

        myImgAdapter = new ImageAdapter(this, temp);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(myImgAdapter);
    }
}
