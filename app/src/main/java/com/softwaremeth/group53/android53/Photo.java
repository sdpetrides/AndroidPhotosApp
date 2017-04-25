package com.softwaremeth.group53.android53;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {

    private String path;

    ArrayList<String> tags;

    private static final long serialVersionUID = 1L;

    public Photo (String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public String toString() {
        return path;
    }




}
