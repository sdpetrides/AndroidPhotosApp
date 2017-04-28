package com.softwaremeth.group53.android53;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class DetailsView extends AppCompatActivity {

    EditText textView;

    Photo photo;

    ArrayAdapter<String> adapter;

    private ListView listView;

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
        listView = (ListView) findViewById(R.id.tags_list);

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

        if (tagType) {
            if (photo.locationTags.contains(newTag)) {
                return;
            } else {
                photo.locationTags.add(newTag);
            }
        } else {
            if (photo.personTags.contains(newTag)) {
                return;
            } else {
                photo.personTags.add(newTag);
            }
        }

        // save state
        AlbumList.user.saveState(this);

        updateTagsListView();

        setResult(RESULT_OK, null);
        //finish();

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
