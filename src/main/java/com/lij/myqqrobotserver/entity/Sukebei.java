package com.lij.myqqrobotserver.entity;

/**
 * @author Celphis
 */
public class Sukebei {
    private String title;
    private String magnet;
    private String size;
    private String date;

    public Sukebei(String title, String magnet, String size, String date) {
        this.title = title;
        this.magnet = magnet;
        this.size = size;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
