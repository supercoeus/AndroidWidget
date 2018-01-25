package com.github.airsaid.androidwidget.data;

/**
 * @author airsaid
 */
public class Item {

    private String title;
    private String desc;
    private Class  cls;

    public Item(String title, String desc, Class cls){
        this.title = title;
        this.desc  = desc;
        this.cls   = cls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", cls=" + cls +
                '}';
    }
}
