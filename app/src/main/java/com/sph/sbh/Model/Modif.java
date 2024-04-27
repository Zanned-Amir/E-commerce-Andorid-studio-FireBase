package com.sph.sbh.Model;

public class Modif {
    private String title;

    public Modif(String title, String count) {
        this.title = title;
        this.count = count;
    }

    private String count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
