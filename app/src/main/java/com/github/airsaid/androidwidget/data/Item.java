package com.github.airsaid.androidwidget.data;

/**
 * @author airsaid
 */
public class Item {

    private String item;
    private Class  cls;

    public Item(String item, Class cls){
        this.item = item;
        this.cls  = cls;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
