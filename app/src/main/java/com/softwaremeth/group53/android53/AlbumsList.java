package com.softwaremeth.group53.android53;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.softwaremeth.group53.android53.R.id.parent;

public class AlbumsList extends Activity {

    private ListView listView;
    private String[] albumNames;

    public static final String ALBUM_NAME_KEY = "album_name";
    // public static final String ALBUM_PHOTOS_NAME_KEY = "route_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_list);

        listView = (ListView)findViewById(R.id.albums_list);
        albumNames = getResources().getStringArray(R.array.albums_array);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.albums, albumNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadPhotoNames(position);
            }
        });
    }

    private void loadPhotoNames(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_NAME_KEY, albumNames[pos]);
        // bundle.putStringArray(ALBUM_PHOTOS_NAME_KEY, albumPhotoNames);
        Intent intent = new Intent(this, AlbumsView.class);
        intent.putExtras(bundle);
        startActivity(intent);
    };

}
