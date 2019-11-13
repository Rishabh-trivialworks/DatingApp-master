package com.quintus.labs.datingapp.Utils;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class InfoScreenData {

    public String text;
    public int resource;
    public String url;

    public InfoScreenData(String text, int resource) {
        this.text = text;
        this.resource = resource;
    }
    public InfoScreenData(String text, String url) {
        this.text = text;
        this.url = url;
    }
}
