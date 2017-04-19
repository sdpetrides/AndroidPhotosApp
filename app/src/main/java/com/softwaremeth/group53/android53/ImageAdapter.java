package com.softwaremeth.group53.android53;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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

    // references to our images
    // private Integer[] mThumbIds = { R.drawable.sample_0 };

    private void initThumbIds(String album_name) {

        String jsonRaw = null;

        try {
            InputStream is = mContext.getAssets().open("album.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonRaw = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        JSONObject jObject = null;
        JSONArray jAlbumNameArray = null;
        JSONArray jPhotoIdArray = null;

        try {

            jObject = new JSONObject(jsonRaw);
            jAlbumNameArray = jObject.getJSONArray("albumNames");

            for (int i = 0; i < jAlbumNameArray.length(); i++) {

                if (jAlbumNameArray.getJSONObject(i).get("name").equals(album_name)) {

                    jPhotoIdArray = (JSONArray) jAlbumNameArray.getJSONObject(i).get("photoIds");
                    for (int j = 0; j < jPhotoIdArray.length(); j++) {

                        String photoIdString = (String) jPhotoIdArray.get(j);
                        Integer k = mContext.getResources().getIdentifier(
                                photoIdString, "drawable", mContext.getPackageName());
                        mThumbIds.add(k);
                    }
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}