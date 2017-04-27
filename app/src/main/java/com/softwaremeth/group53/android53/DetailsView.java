package com.softwaremeth.group53.android53;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class DetailsView extends AppCompatActivity {

    EditText textView;

    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);

        // get photo
        photo = AlbumList.user.currentPhoto;

        // set toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        this.setSupportActionBar(toolbar);

        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get textView
        textView = (EditText) findViewById(R.id.edit_text);

    }

    public void save(View view) {

        setResult(RESULT_OK, null);
        finish();

    }

    public void cancel(View view) {

        setResult(RESULT_CANCELED);
        finish();

    }
}
