package com.softwaremeth.group53.android53;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AlbumsView extends Activity {

    private ArrayList<String> albumPhotoNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumsview);

        // get the name and detail from bundle
        Bundle bundle = getIntent().getExtras();
        String albumName = bundle.getString(AlbumsList.ALBUM_NAME_KEY);

        // get the name and detail view objects
        TextView albumNameTextView = (TextView)findViewById(R.id.albumName);

        /*
        InputStream is = getResources().openRawResource(R.raw.photo_names);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        // albumPhotoNames = new ArrayList<String>();

        // fill arraylist with names of photos
        try {
            while (true) {
                String line = br.readLine();
                if (!line.startsWith("+")) {
                    String name = new String(line);
                    albumPhotoNames.add(name);
                } else {
                    break;
                }
            }
        } catch (IOException e) {}
        */



        // set name and detail on the views
        albumNameTextView.setText(albumName);
    }

}