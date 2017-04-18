package com.softwaremeth.group53.android53;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlbumsList extends Activity {

    private ListView listView;
    private String[] albumNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_list);

        listView = (ListView)findViewById(R.id.albums_list);
        albumNames = getResources().getStringArray(R.array.albums_array);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.albums, albumNames);
        listView.setAdapter(adapter);

    }

}
