package com.softwaremeth.group53.android53;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlbumList extends Activity {

    private ListView listView;
    private String[] albumNames;

    public static final String ALBUM_NAME_KEY = "album_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);

        // get listView
        listView = (ListView)findViewById(R.id.albums_list);

        // get albumNames from strings.xml
        albumNames = getResources().getStringArray(R.array.albums_array);

        // populate listView with albumNames
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.album_cell, albumNames);
        listView.setAdapter(adapter);

        // set listener to load album view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadAlbumView(position);
            }
        });
    }

    private void loadAlbumView(int pos) {

        // create bundle
        Bundle bundle = new Bundle();

        // put album name in the bundle
        bundle.putString(ALBUM_NAME_KEY, albumNames[pos]);

        // create intent and add bundle
        Intent intent = new Intent(this, AlbumView.class);
        intent.putExtras(bundle);

        // start AlbumView activity
        startActivity(intent);
    };

}
