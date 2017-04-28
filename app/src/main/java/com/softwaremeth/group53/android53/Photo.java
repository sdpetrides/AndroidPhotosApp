package com.softwaremeth.group53.android53;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {

    private String path;

    ArrayList<String> personTags;

    ArrayList<String> locationTags;

    private static final long serialVersionUID = 1L;

    public Photo (String path) {
        this.path = path;
        this.locationTags = new ArrayList<String>();
        this.personTags = new ArrayList<String>();

    }

    public String getPath(){
        return path;
    }

    public String toString() {
        return path;
    }


}
