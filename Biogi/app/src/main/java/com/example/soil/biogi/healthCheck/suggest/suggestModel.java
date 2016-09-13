package com.example.soil.biogi.healthCheck.suggest;

/**
 * Created by soil on 2016/6/19.
 */
public class suggestModel {
    private String image,name, price,decri ;
    public suggestModel(String image, String name, String price, String decri){
        this.image = image ;
        this.name = name ;
        this.price = price;
        this.decri = decri ;
    }
    public void setName(String name){
        this.name = name ;
    }
    public String getName(){
        return name ;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return price;
    }

    public void setDecri(String value){
        this.decri = value ;
    }
    public String getDecri(){
        return decri ;
    }
}
