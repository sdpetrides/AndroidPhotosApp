package com.softwaremeth.group53.android53;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddRenameAlbum extends AppCompatActivity {

    public static final String ALBUM_NAME = "album_name";

    EditText albumNameTextView;

    private String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rename_album_view);

        // get toolbar and textview
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        albumNameTextView = (EditText) findViewById(R.id.album_name);

        // get the name and detail from bundle
        // Bundle bundle = getIntent().getExtras();
        // String albumName = bundle.getString(ALBUM_NAME);
        albumName = AlbumList.user.currentAlbumName;

        // set title and hint if renaming
        if (AlbumList.isAddNotRename) {
            toolbar.setTitle("Add Album");
            albumNameTextView.setHint("Name");
        } else {
            toolbar.setTitle("Rename Album");
            albumNameTextView.setHint(albumName);
        }

        // set toolbar
        this.setSupportActionBar(toolbar);

        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void save(View view) {

        // get new name from xml
        String name = albumNameTextView.getText().toString();

        // TODO: alert dialog

        // create bundle and add new name to bundle
        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_NAME, name);

        // create intent and add bundle
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);

        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
