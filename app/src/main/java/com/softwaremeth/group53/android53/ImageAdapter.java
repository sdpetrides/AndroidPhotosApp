package com.softwaremeth.group53.android53;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<String> mThumbIds;

    private String album_name;

    private int pos;

    public ImageAdapter(Context c, String album_name, int pos) {
        this.mContext = c;
        this.album_name = album_name;
        this.pos = pos;
        mThumbIds = new ArrayList<String>();
        initThumbIds();
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

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        System.out.println("Position: " + position);
        imageView.setImageBitmap(BitmapFactory.decodeFile(mThumbIds.get(position)));
        return imageView;
    }

    private void initThumbIds() {

    }

    public void addPicture(String picture) {

    }

    public void removePicture(String picture) {

    }

}