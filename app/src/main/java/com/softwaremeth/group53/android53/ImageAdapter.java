package com.softwaremeth.group53.android53;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<Integer> mThumbIds;

    public ImageAdapter(Context c, String album_name) {
        mContext = c;
        mThumbIds = new ArrayList<Integer>();
        initThumbIds(album_name);
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        // if it's not recycled, initialize some attributes
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(115, 115));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }

    public int getImgId(int position) {
        return mThumbIds.get(position);
    }

    /* references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
    };
    */

    private void initThumbIds(String album_name) {

        InputStream is = mContext.getResources().openRawResource(R.raw.photo_names);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // fill arraylist with photo identifiers
        try {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    continue;
                } else if (line.equals(album_name)) {
                    line = br.readLine();
                    Integer i = mContext.getResources().getIdentifier(line, "drawable", mContext.getPackageName());
                    if (i == 0) {
                        break;
                    }
                    mThumbIds.add(i);
                    break;
                }
            }
        } catch (IOException e) {}
    }
}