package com.softwaremeth.group53.android53;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class myJSON {

    private Context context;
    String filename;

    public myJSON (Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    public String[] getAlbumNames() {

        JSONObject jObject = getJSONObject();

        if (jObject == null) {
            return null;
        }

        JSONArray jArray = null;
        String[] albumNames = null;

        try {

            jArray = jObject.getJSONArray("albumNames");
            albumNames = new String[jArray.length()];

            for (int i = 0; i < jArray.length(); i++) {
                albumNames[i] = (String) jArray.getJSONObject(i).get("name");
            }

        } catch (JSONException e) {}

        if (albumNames == null) {
            // System.out.println("No albums");
        }
        return albumNames;
    }

    public String getAlbumNameAt(long id) {

        JSONObject jObject = getJSONObject();

        if (jObject == null) {
            return null;
        }

        JSONArray jArray = null;
        String albumName = null;

        try {

            jArray = jObject.getJSONArray("albumNames");

            if (id >= jArray.length()) {
                return null;
            }

            albumName = (String) jArray.getJSONObject((int)id).get("name");

        } catch (JSONException e) {}

        return albumName;
    }

    public JSONObject getAlbumObjectAt(long id) {

        JSONObject jObject = getJSONObject();
        JSONObject jTarget = null;

        if (jObject == null) {
            return null;
        }

        JSONArray jArray = null;
        String albumName = null;

        try {

            jArray = jObject.getJSONArray("albumNames");

            if (id >= jArray.length()) {
                return null;
            }

            jTarget = jArray.getJSONObject((int)id);

        } catch (JSONException e) {}

        return jTarget;
    }

    public void setJSONAlbum() {




    }



    public JSONObject getJSONObject() {

        String jsonRaw = null;
        JSONObject jObject = null;

        try {
            FileInputStream in = context.openFileInput(filename);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            jsonRaw = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            FileOutputStream out = null;
            try {
                out = context.openFileOutput(filename, MODE_PRIVATE);
                out.write("{\"albumNames\":[], \"photos\":[]}".getBytes());
                out.close();
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            return null;
        }

        // System.out.println("GET jsonRaw: " + jsonRaw);

        try {
            jObject = new JSONObject(jsonRaw);
        } catch (JSONException e) {
            return null;
        }

        return jObject;
    }

    public void setJSONObject(JSONObject jObject) {

        String jsonRaw = jObject.toString();

        byte [] buffer = jsonRaw.getBytes();

        try {
            FileOutputStream out = context.openFileOutput(filename, MODE_PRIVATE);
            out.write(buffer);
            out.close();
        } catch (IOException ex) {}
    }
}
