package com.softwaremeth.group53.android53;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

public class DetailsView extends AppCompatActivity {

    EditText textView;

    Photo photo;

    private ListView locationListView;
    private ListView personListView;
    private Spinner albumSpinner;

    //true = location; false = person
    Boolean tagType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);

        tagType = true;

        // get photo
        photo = AlbumList.user.currentPhoto;

        // get listView
        locationListView = (ListView) findViewById(R.id.location_tags_list);
        personListView = (ListView) findViewById(R.id.person_tags_list);

        // Create and populate spinner
        albumSpinner = (Spinner) findViewById(R.id.albumSpinner);
        ArrayAdapter<Album> spinnerAdapter = new ArrayAdapter<Album>(this, android.R.layout.simple_spinner_dropdown_item, AlbumList.user.albums);
        albumSpinner.setAdapter(spinnerAdapter);

        // set toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        this.setSupportActionBar(toolbar);

        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get textView
        textView = (EditText) findViewById(R.id.edit_text);

        //populate listview
        updateTagsListView();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    public void add(View view) {

        String newTag = textView.getText().toString();
        if (newTag == null || newTag.trim().length() == 0) {
            textView.getText().clear();
            return;
        }

        if (tagType) {
            if (containsCaseInsensitive(newTag, photo.locationTags)) {
                return;
            } else {
                photo.locationTags.add(newTag);
            }
        } else {
            if (containsCaseInsensitive(newTag, photo.personTags)) {
                return;
            } else {
                photo.personTags.add(newTag);
            }
        }

        // save state
        AlbumList.user.saveState(this);

        updateTagsListView();

        setResult(RESULT_OK, null);
        textView.getText().clear();
        //finish();

    }

    private boolean containsCaseInsensitive(String str, ArrayList<String> list) {
        for (String s: list) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
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
                deleteTag(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteTag(AdapterView.AdapterContextMenuInfo info) {

        System.out.println("info:" + info.targetView.getId());
        System.out.println("info parent:" + ((ListView)info.targetView.getParent()).getId());
        System.out.println("location:" + locationListView.getId());
        System.out.println("person:" + personListView.getId());

        // remove tag
        if (((ListView)info.targetView.getParent()).getId() == locationListView.getId()) {
            photo.locationTags.remove(info.position);
        } else {
            photo.personTags.remove(info.position);
        }

        // save state
        AlbumList.user.saveState(this);

        // update listview
        updateTagsListView();
    }

    public void moveTo(View view) {

        Album target = (Album)albumSpinner.getSelectedItem();

        target.addPhoto(photo);

        AlbumList.user.currentAlbum.getPhotos().remove(photo);

        AlbumList.user.saveState(this);
    }
}
