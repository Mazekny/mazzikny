package edu.aucegypt.mazzikny;

import java.io.Serializable;

public class ItemsModel implements Serializable {
    private String name;
    private String price;
    private String desc;
    private String cont;
    private int image;

    public ItemsModel(String name, String price, String desc, String cont, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.desc = desc;
        this.cont = cont;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
