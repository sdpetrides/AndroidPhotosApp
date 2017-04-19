package com.softwaremeth.group53.android53;


import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumView extends Activity {

    private ArrayList<String> albumPhotoNames;

    public ImageAdapter myImgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        // get the name and detail from bundle
        Bundle bundle = getIntent().getExtras();
        String albumName = bundle.getString(AlbumList.ALBUM_NAME_KEY);

        // get gridView and set image adapter
        myImgAdapter = new ImageAdapter(this, albumName);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(this, null));

        // get the name and detail view objects
        TextView albumNameTextView = (TextView)findViewById(R.id.albumName);

        // set name and detail on the views
        albumNameTextView.setText(albumName);

    }

}